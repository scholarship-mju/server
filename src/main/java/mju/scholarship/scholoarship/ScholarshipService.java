package mju.scholarship.scholoarship;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.config.JwtUtil;
import mju.scholarship.embedding.PineconeService;
import mju.scholarship.member.entity.Member;
import mju.scholarship.member.entity.MemberGot;
import mju.scholarship.member.entity.ScholarshipStatus;
import mju.scholarship.member.repository.MemberGotRepository;
import mju.scholarship.member.repository.MemberInterRepository;
import mju.scholarship.member.repository.MemberRepository;
import mju.scholarship.member.entity.MemberInterest;
import mju.scholarship.result.exception.*;
import mju.scholarship.s3.S3UploadService;
import mju.scholarship.scholoarship.dto.*;
import mju.scholarship.scholoarship.repository.ScholarShipRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScholarshipService {

    private final ScholarShipRepository scholarShipRepository;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final MemberInterRepository memberInterRepository;
    private final MemberGotRepository memberGotRepository;
    private final S3UploadService s3UploadService;
    private final StringRedisTemplate redisTemplate;
    private final PineconeService pineconeService;
    private static final int BATCH_SIZE = 1; // 100개씩 모아서 실행
    private final JdbcTemplate jdbcTemplate;
    private final ConcurrentHashMap<Long, AtomicInteger> localCounter = new ConcurrentHashMap<>();

    private static final String VIEW_COUNT_KEY = "scholarship:viewCount:";

    // 매일 자정 실행
    @Scheduled(cron = "0 0 0 * * ?") // cron 표현식: 매일 00:00:00
    @Transactional
    public void updateProgressStatus() {
        List<Scholarship> scholarships = scholarShipRepository.findAll();

        for (Scholarship scholarship : scholarships) {
            scholarship.updateProgressStatus(); // 상태 업데이트
        }

        scholarShipRepository.saveAll(scholarships); // 일괄 저장
        System.out.println("Scholarship progressStatus updated at: " + LocalDate.now());
    }

    @Scheduled(cron = "0 */5 * * * *") // 5분마다
    @Transactional
    public void syncViewCounts() {
        Set<String> ids = redisTemplate.opsForSet().members("dirty_scholarship_ids");
        List<Object[]> batchArgs = new ArrayList<>();
        List<String> keysToDelete = new ArrayList<>();

        if (ids == null || ids.isEmpty()) return;

        // Redis 파이프라인으로 view count 한번에 가져오기
        List<Object> viewCounts = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (String idStr : ids) {
                String key = (VIEW_COUNT_KEY + idStr);
                connection.get(key.getBytes(StandardCharsets.UTF_8));
            }
            return null;
        });

        int index = 0;
        for (String idStr : ids) {
            Object countObj = viewCounts.get(index++);
            if (countObj == null) {
                log.warn("View count 없음: {}", VIEW_COUNT_KEY + idStr);
                continue;
            }

            int viewCount = Integer.parseInt(new String((byte[]) countObj, StandardCharsets.UTF_8));
            Long id = Long.parseLong(idStr);

            batchArgs.add(new Object[]{viewCount, id});
            keysToDelete.add(VIEW_COUNT_KEY + idStr);
        }

        if (batchArgs.isEmpty()) return;

        try {
            jdbcTemplate.batchUpdate(
                    "UPDATE scholarship SET view_count = ? WHERE id = ?",
                    batchArgs
            );
            redisTemplate.delete("dirty_scholarship_ids"); //  성공했을 때만 삭제
        } catch (Exception e) {
            throw new ViewCountUpdateException();
        }

    }

    @Transactional
    public void addGotScholarships(Long scholarshipId) {
        Member loginMember = jwtUtil.getLoginMember();

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        boolean exists = memberGotRepository.existsByMemberAndScholarship(loginMember, scholarship);
        if (exists) {
            throw new AlreadyGotScholarshipException(); // 커스텀 예외
        }

        MemberGot memberGot = MemberGot.builder()
                .member(loginMember)
                .scholarship(scholarship)
                .build();

        memberGotRepository.save(memberGot);
    }

    @Transactional
    public void addInterestScholarship(Long scholarshipId) {
        Member loginMember = jwtUtil.getLoginMember();

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        boolean exists = memberInterRepository.existsByMemberAndScholarship(loginMember, scholarship);
        if (exists) {
            throw new AlreadyInterestedScholarshipException(); // 커스텀 예외
        }

        MemberInterest memberInterest = MemberInterest.builder()
                .member(loginMember)
                .scholarship(scholarship)
                .build();

        memberInterRepository.save(memberInterest);
    }

    public ScholarshipResponse getOneScholarshipInRedis(Long scholarshipId) {

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        incrementViewCountDirect(scholarshipId);

        int viewCount = getViewCount(scholarshipId);

        return ScholarshipResponse.builder()
                .id(scholarship.getId())
                .name(scholarship.getName())
                .supportDetails(scholarship.getSupportDetails())
                .departmentType(scholarship.getDepartmentType())
                .organizationType(scholarship.getOrganizationType())
                .productType(scholarship.getProductType())
                .endDate(scholarship.getEndDate())
                .scholarshipUrl(scholarship.getScholarshipUrl())
                .eligibilityRestriction(scholarship.getEligibilityRestriction())
                .financialAidType(scholarship.getFinancialAidType())
                .gradeRequirement(scholarship.getGradeRequirement())
                .gradeType(scholarship.getGradeType())
                .incomeRequirement(scholarship.getIncomeRequirement())
                .organizationName(scholarship.getOrganizationName())
                .startDate(scholarship.getStartDate())
                .residencyRequirement(scholarship.getResidencyRequirement())
                .selectionCount(scholarship.getSelectionCount())
                .selectionMethod(scholarship.getSelectionMethod())
                .specialQualification(scholarship.getSpecialQualification())
                .submitDocumentDetail(scholarship.getSubmitDocumentDetail())
                .universityType(scholarship.getUniversityType())
                .recommendationRequired(scholarship.getRecommendationRequired())
                .viewCount(viewCount)
                .scholarshipImage(scholarship.getScholarshipImage())
                .build();
    }

    public void incrementViewCount(Long scholarshipId) {
        String key = VIEW_COUNT_KEY + scholarshipId;  // 고유 키 생성
        redisTemplate.opsForValue().increment(key);
    }

    public void incrementViewCountBatch(Long scholarshipId) {
        String key = VIEW_COUNT_KEY + scholarshipId;
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.stringCommands().incr(key.getBytes()); // 여러 요청을 한 번에 처리
            return null;
        });
    }

    public void incrementViewCountDirect(Long scholarshipId) {
        String key = VIEW_COUNT_KEY + scholarshipId;
        redisTemplate.opsForValue().increment(key);  // 바로 Redis에 증가

        // 변경된 ID 기록
        redisTemplate.opsForSet().add("dirty_scholarship_ids", scholarshipId.toString());
    }

    public void incrementViewCountBatchAndPipe(Long scholarshipId) {
        localCounter.putIfAbsent(scholarshipId, new AtomicInteger(0));
        int currentCount = localCounter.get(scholarshipId).incrementAndGet();

        // 한번에 10개씩 모아서 전송
        if (currentCount >= BATCH_SIZE) {
            synchronized (this) {
                if (localCounter.get(scholarshipId).get() >= BATCH_SIZE) {
                    String key = VIEW_COUNT_KEY + scholarshipId;

                    // Pipeline 적용: 네트워크 오버헤드 최소화
                    redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                        connection.stringCommands().incrBy(key.getBytes(), currentCount);
                        return null;
                    });

                    localCounter.get(scholarshipId).set(0); // 로컬 카운터 초기화
                }
            }
        }
    }

    public int getViewCount(Long scholarshipId) {
        String key = VIEW_COUNT_KEY + scholarshipId;
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Integer.parseInt(value) : 0;
    }

    public List<GotScholarshipResponse> getAllGotScholarships() {
        Member loginMember = jwtUtil.getLoginMember();

        // 브릿지 테이블(MemberGot)에서 로그인한 회원의 모든 정보 조회
        List<MemberGot> memberGots = memberGotRepository.findByMember(loginMember);

        // MemberGot -> GotScholarshipResponse 변환
        return memberGots.stream()
                .map(got -> {
                    Scholarship scholarship = got.getScholarship();

                    return GotScholarshipResponse.builder()
                            .id(scholarship.getId())
                            .name(scholarship.getName())
                            .supportDetails(scholarship.getSupportDetails())
                            .status(got.getStatus())
                            .viewCount(getViewCount(scholarship.getId()))
                            .build();

                })
                .collect(Collectors.toList());
    }


    public List<InterestedScholarshipResponse> getAllInterestScholarships() {

        Member loginMember = jwtUtil.getLoginMember();

        // 관심 장학금 조회 (브릿지 테이블을 통한 조회)
        List<MemberInterest> interests = memberInterRepository.findByMember(loginMember);

        // MemberInterest -> ScholarshipResponse 변환
        return interests.stream()
                .map(interest -> {
                    Scholarship scholarship = interest.getScholarship();
                    return InterestedScholarshipResponse.builder()
                            .id(scholarship.getId())
                            .name(scholarship.getName())
                            .supportDetails(scholarship.getSupportDetails())
                            .progressStatus(scholarship.getProgressStatus())
                            .organizationName(scholarship.getOrganizationName())
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteInterestScholarship(Long scholarshipId) {
        Member loginMember = jwtUtil.getLoginMember();

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        MemberInterest memberInterest = memberInterRepository.findByMemberAndScholarship(loginMember, scholarship)
                .orElseThrow(InterestedScholarshipNotFoundException::new);

        memberInterRepository.delete(memberInterest);
    }

    //Todo : 장학금 삭제하면 받은 장학금이나 찜한 장학금 목록에서도 다 삭제
    @Transactional
    public void deleteScholarship(Long scholarshipId) {
        log.info("id = {}", scholarshipId);
        Member loginMember = jwtUtil.getLoginMember();

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);
    }

    public List<Scholarship> getMyScholarship() {
        Member loginMember = jwtUtil.getLoginMember();

        List<Scholarship> myScholarships = scholarShipRepository.findMyScholarship(loginMember);

        return myScholarships;
    }

    @Transactional
    public void validGotScholarship(Long scholarshipId, List<MultipartFile> files) {

        Member loginMember = jwtUtil.getLoginMember();

        String sid = String.valueOf(scholarshipId);

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        MemberGot memberGot = memberGotRepository.findByMemberAndScholarship(loginMember, scholarship)
                .orElseThrow(MemberNotFoundException::new);

        files.forEach(file -> {
            try {
                String url = s3UploadService.upload(file, "valid", loginMember.getId(), scholarshipId);
                log.info("url = {}", url);
                memberGot.addImageUrl(url);
            } catch (IOException e) {
                throw new FileUploadException();
            }
            memberGot.changeStatus(ScholarshipStatus.IN_PROGRESS);
        });
    }

    @Transactional
    public void deleteGotScholarship(Long scholarshipId) {
        Member loginMember = jwtUtil.getLoginMember();

        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        MemberGot memberGot = memberGotRepository.findByMemberAndScholarship(loginMember, scholarship)
                .orElseThrow(InterestedScholarshipNotFoundException::new);

        memberGotRepository.delete(memberGot);
    }

    public List<SearchScholarshipResponse> searchScholarship(String scholarshipName) {
        List<Scholarship> byScholarshipName = scholarShipRepository.findByScholarshipName(scholarshipName);

        List<SearchScholarshipResponse> response = new ArrayList<>();

        for (Scholarship scholarship : byScholarshipName) {
            SearchScholarshipResponse searchScholarshipResponse = SearchScholarshipResponse.builder()
                    .scholarshipName(scholarship.getName())
                    .scholarshipId(scholarship.getId())
                    .build();

            response.add(searchScholarshipResponse);
        }

        return response;
    }

    //
    public List<Scholarship> getRecommendScholarshipByDB() {
        List<String> recommendedIds = pineconeService.searchScholarshipByDB();

        List<Long> scholarshipIds = recommendedIds.stream()
                .map(Long::parseLong) // String -> Long 변환
                .toList();

        // DB에서 장학금 가져오고, 진행 중/예정인 것만 필터링
        return scholarShipRepository.findAllById(scholarshipIds).stream()
                .filter(s -> s.getProgressStatus() != ScholarshipProgressStatus.ENDED)
                .limit(9) // 여기서 정확히 9개만 잘라서 사용
                .toList();


    }

    public List<Scholarship> getRecommendScholarshipByPinecone() {

        List<String> recommendedIds = pineconeService.searchScholarshipByPinecone();

        List<Long> scholarshipIds = recommendedIds.stream()
                .map(Long::parseLong) // String -> Long 변환
                .toList();

        return scholarShipRepository.findAllById(scholarshipIds);
    }

    public Page<AllScholarshipResponse> getAllScholarships(String keyword, List<String>  qualification, ScholarshipProgressStatus status, int page) {
        // 현재 로그인된 사용자 가져오기
        Member loginMember = jwtUtil.getLoginMember();

        // 관심 장학금 ID 리스트 가져오기
        List<Long> interestedIds = memberInterRepository.findScholarshipIdByMember(loginMember);

        Pageable pageable = PageRequest.of(page, 18, Sort.by(Sort.Direction.DESC, "viewCount"));

        // 전체 장학금 조회 및 관심 여부 설정
        Page<Scholarship> scholarships = scholarShipRepository.findAllByFilter(keyword, qualification, status, pageable);

        return scholarships.map(scholarship ->
                AllScholarshipResponse.builder()
                        .id(scholarship.getId())
                        .supportDetails(scholarship.getSupportDetails())
                        .name(scholarship.getName())
                        .isInterested(interestedIds.contains(scholarship.getId()))
                        .progressStatus(scholarship.getProgressStatus())
                        .viewCount(getViewCount(scholarship.getId()))
                        .organizationName(scholarship.getOrganizationName())
                        .scholarshipImage(scholarship.getScholarshipImage())
                        .build()
        );
    }

    public Page<AllScholarshipResponse> getAllScholarshipsByAnonymous(String keyword, List<String> qualification, ScholarshipProgressStatus status, int page) {

        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "viewCount"));

        // 전체 장학금 조회 및 관심 여부 설정
        Page<Scholarship> scholarships = scholarShipRepository.findAllByFilter(keyword, qualification, status, pageable);

        return scholarships.map(scholarship ->
                AllScholarshipResponse.builder()
                        .id(scholarship.getId())
                        .supportDetails(scholarship.getSupportDetails())
                        .name(scholarship.getName())
                        .progressStatus(scholarship.getProgressStatus())
                        .viewCount(getViewCount(scholarship.getId()))
                        .organizationName(scholarship.getOrganizationName())
                        .scholarshipImage(scholarship.getScholarshipImage())
                        .build());
    }

    public List<UnivScholarshipResponse> getUnivScholarship(String keyword) {

        Member loginMember = jwtUtil.getLoginMember();
        String university = loginMember.getUniversity();

        List<Scholarship> allByUniversity = scholarShipRepository.findAllByUniversity(university, keyword);

        List<UnivScholarshipResponse> univScholarshipResponses = new ArrayList<>();

        for (Scholarship scholarship : allByUniversity) {
            UnivScholarshipResponse response = UnivScholarshipResponse.builder()
                    .id(scholarship.getId())
                    .supportDetails(scholarship.getSupportDetails())
                    .name(scholarship.getName())
                    .progressStatus(scholarship.getProgressStatus())
                    .viewCount(getViewCount(scholarship.getId()))
                    .scholarshipImage(scholarship.getScholarshipImage())
                    .organizationName(scholarship.getOrganizationName())
                    .build();

            univScholarshipResponses.add(response);
        }

        return univScholarshipResponses;
    }


//    public List<Scholarship> getMyScholarship() {
        // 내 정보 가져오기

        /**
         * 내 정보랑 장학금 비교 아마 sql 짜야 될듯
         */
//    }
}

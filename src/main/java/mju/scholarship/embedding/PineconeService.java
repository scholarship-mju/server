package mju.scholarship.embedding;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.config.JwtUtil;
import mju.scholarship.member.entity.Member;
import mju.scholarship.member.repository.MemberRepository;
import mju.scholarship.result.exception.MemberNotFoundException;
import mju.scholarship.result.exception.ScholarshipNotFoundException;
import mju.scholarship.scholoarship.Scholarship;
import mju.scholarship.scholoarship.repository.ScholarShipRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class PineconeService {

    private final ScholarShipRepository scholarShipRepository;
    private final JwtUtil jwtUtil;
    @Value("${pinecone.api-key}")
    private String apiKey;

    @Value("${pinecone.scholarship.index-name}")
    private String scholarshipHost;

    @Value("${pinecone.member.index-name}")
    private String memberHost;


    private final EmbeddingService embeddingService;
    private final MemberRepository memberRepository;


    private final RestTemplate restTemplate = new RestTemplate();

    public void saveScholarshipVector(Long scholarshipId) {


        //pinecone에 저장할 id와 vector data
        String id = String.valueOf(scholarshipId);
        List<Float> vector = embeddingService.embeddingScholarship(scholarshipId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of("vectors", List.of(Map.of("id", id, "values", vector)));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        restTemplate.exchange(scholarshipHost + "/vectors/upsert", HttpMethod.POST, entity, String.class);
    }

    public void saveMemberVector(Long memberId) {

        //pinecone에 저장할 id와 vector data
        String id = String.valueOf(memberId);
        List<Float> vector = embeddingService.embeddingMember(memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of("vectors", List.of(Map.of("id", id, "values", vector)));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        restTemplate.exchange(memberHost + "/vectors/upsert", HttpMethod.POST, entity, String.class);
    }

    public void saveAllMember() {
        List<Member> allMember = memberRepository.findAll();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Map<String, Object>> vectorList = allMember.stream()
                .map(member -> {
                    String id = String.valueOf(member.getId());
                    List<Float> vector = embeddingService.embeddingMember(member.getId());
                    return Map.of("id", id, "values", vector);
                })
                .toList();

        Map<String, Object> request = Map.of("vectors", vectorList);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        restTemplate.exchange(
                memberHost + "/vectors/upsert",
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    public void saveAllScholarshipVector() {

        List<Scholarship> allScholarships = scholarShipRepository.findAll();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2️⃣ Pinecone에 저장할 벡터 리스트 생성
        List<Map<String, Object>> vectorList = allScholarships.stream()
                .map(scholarship -> {
                    String id = String.valueOf(scholarship.getId()); // Long -> String 변환
                    List<Float> vector = embeddingService.embeddingScholarship(scholarship.getId());
                    return Map.of("id", id, "values", vector);
                })
                .toList();

        Map<String, Object> requestBody = Map.of("vectors", vectorList);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // 3️⃣ Pinecone API 호출하여 모든 장학금 벡터 저장
        restTemplate.exchange(
                scholarshipHost + "/vectors/upsert",
                HttpMethod.POST,
                entity,
                String.class
        );

    }

    /**
     * 검색어를 기반으로 장학금 추천
     */
    public List<String> searchScholarshipByWord(String userQuery) {
        List<Float> userEmbedding = embeddingService.getEmbedding(userQuery);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of(
                "queries", List.of(Map.of("values", userEmbedding)),
                "topK", 5
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(scholarshipHost + "/query", HttpMethod.POST, entity, Map.class);

        List<Map<String, Object>> matches = (List<Map<String, Object>>) response.getBody().get("matches");
        return matches.stream().map(match -> (String) match.get("id")).toList();
    }

    /**
     * 유저 데이터 기준으로 장학금 추천
     * 유저 데이터를 DB에서 가져와서 벡터화 후 벡터화된 데이터를
     * @return
     */
    public List<String> searchScholarshipByDB() {

        Member loginMember = jwtUtil.getLoginMember();

        List<Float> vector = embeddingService.embeddingMember(loginMember.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        //  Pinecone 벡터 검색 요청 데이터 생성 (유저 벡터 기반 검색)
        Map<String, Object> requestBody = Map.of(
                "vector", vector,
                "topK", 5
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Pinecone의 `query` API 호출하여 유사한 장학금 검색
        ResponseEntity<Map> response = restTemplate.exchange(
                scholarshipHost + "/query",
                HttpMethod.POST,
                entity,
                Map.class
        );

        // Pinecone에서 반환된 유사한 장학금 ID 리스트 추출
        List<Map<String, Object>> matches = (List<Map<String, Object>>) response.getBody().get("matches");
        return matches.stream().map(match -> (String) match.get("id")).toList();
    }

    /**
     * 유저 데이터 기준으로 장학금 추천
     * pinecone에 member index에서 유저 데이터 가져온 후
     * pinecone에 scholarhsip index에 추천 장학금 요청
     */
    public List<String> searchScholarshipByPinecone() {

        Member loginMember = jwtUtil.getLoginMember();

        // 1️⃣ Pinecone에서 유저 벡터 가져오기
        List<Float> userEmbedding = getUserEmbeddingFromPinecone(loginMember.getId());
        if (userEmbedding == null) {
            throw new RuntimeException("유저 벡터를 찾을 수 없습니다. Pinecone에 저장되어 있는지 확인하세요.");
        }

        // 2️⃣ Pinecone API 요청을 위한 HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3️⃣ Pinecone 벡터 검색 요청 데이터 생성 (유저 벡터 기반 검색)
        Map<String, Object> requestBody = Map.of(
                "queries", List.of(Map.of("values", userEmbedding)),
                "topK", 5 // 가장 유사한 5개의 장학금 추천
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // 4️⃣ Pinecone의 `query` API 호출하여 유사한 장학금 검색
        ResponseEntity<Map> response = restTemplate.exchange(
                 scholarshipHost + "/query",
                HttpMethod.POST,
                entity,
                Map.class
        );

        // 5️⃣ Pinecone에서 반환된 유사한 장학금 ID 리스트 추출
        List<Map<String, Object>> matches = (List<Map<String, Object>>) response.getBody().get("matches");
        return matches.stream().map(match -> (String) match.get("id")).toList();
    }


    /**
     * pinecone에서 유저 데이터 가져오는 메소드
     * @param memberId 멤버 아이디
     * @return
     */
    public List<Float> getUserEmbeddingFromPinecone(Long memberId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of(
                "ids", List.of(String.valueOf(memberId))
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                memberHost + "/vectors/fetch",
                HttpMethod.POST,
                entity,
                Map.class
        );

        log.info("Pinecone API 응답: {}", response.getBody());

        if (response.getBody() == null || !response.getBody().containsKey("vectors")) {
            log.warn("Pinecone 응답에 'vectors' 필드가 없습니다.");
            return null;
        }

        Map<String, Object> vectors = (Map<String, Object>) response.getBody().get("vectors");
        log.info("Pinecone 응답 vector 키: {}", vectors.keySet());

        Object vectorData = vectors.get(String.valueOf(memberId));
        if (vectorData == null) {
            vectorData = vectors.get(memberId); // 정수형 키일 가능성 고려
        }

        if (vectorData instanceof Map) {
            Map<String, Object> vectorMap = (Map<String, Object>) vectorData;
            if (vectorMap.containsKey("values")) {
                return (List<Float>) vectorMap.get("values");
            }
        }

        log.warn("Pinecone 응답에 'values' 필드가 없습니다.");
        return null;
    }




}

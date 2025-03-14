package mju.scholarship.embedding;

import lombok.RequiredArgsConstructor;
import mju.scholarship.member.entity.Member;
import mju.scholarship.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PineconeService {

    @Value("${pinecone.api-key}")
    private String apiKey;
    @Value("${pinecone.scholarship.index-name}")
    private String scholarshipIndexName;

    @Value("${pinecone.member.index-name}")
    private String memberIndexName;

    private final EmbeddingService embeddingService;
    private final MemberRepository memberRepository;


    private final RestTemplate restTemplate = new RestTemplate();

    public void saveScholarshipVector(String id, List<Float> vector) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of("vectors", List.of(Map.of("id", id, "values", vector)));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        restTemplate.exchange("https://" + scholarshipIndexName + ".svc.pinecone.io/vectors/upsert", HttpMethod.POST, entity, String.class);
    }

    public void saveMemberVector(String id, List<Float> vector) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of("vectors", List.of(Map.of("id", id, "values", vector)));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        restTemplate.exchange("https://" + memberIndexName + ".svc.pinecone.io/vectors/upsert", HttpMethod.POST, entity, String.class);
    }


    /**
     * 검색어를 기반으로 장학금 추천
     */
    public List<String> searchSimilarScholarships(String userQuery) {
        List<Float> userEmbedding = embeddingService.getEmbedding(userQuery);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of(
                "queries", List.of(Map.of("values", userEmbedding)),
                "topK", 5
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange("https://" + scholarshipIndexName + ".svc.pinecone.io/query", HttpMethod.POST, entity, Map.class);

        List<Map<String, Object>> matches = (List<Map<String, Object>>) response.getBody().get("matches");
        return matches.stream().map(match -> (String) match.get("id")).toList();
    }

    /**
     * 유저 데이터 기준으로 장학금 추천
     * 유저 데이터를 DB에서 가져와서 벡터화 후 벡터화된 데이터를
     * @param userId
     * @return
     */
    public List<String> searchScholarshipByDB(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));

        String memberText = embeddingService.generateEmbeddingMember(member);
        List<Float> userEmbedding = embeddingService.getEmbedding(memberText);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        //  Pinecone 벡터 검색 요청 데이터 생성 (유저 벡터 기반 검색)
        Map<String, Object> requestBody = Map.of(
                "queries", List.of(Map.of("values", userEmbedding)),
                "topK", 5 // 가장 유사한 5개의 장학금 추천
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Pinecone의 `query` API 호출하여 유사한 장학금 검색
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://" + scholarshipIndexName + ".svc.pinecone.io/query",
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
    public List<String> sarchScholarshipByPinecone(Long userId) {
        // 1️⃣ Pinecone에서 유저 벡터 가져오기
        List<Float> userEmbedding = getUserEmbeddingFromPinecone(userId);
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
                "https://" + scholarshipIndexName + ".svc.pinecone.io/query",
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
        // 1️⃣ Pinecone API 요청을 위한 HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2️⃣ Pinecone 벡터 검색 요청 데이터 생성 (유저 ID를 기반으로 검색)
        Map<String, Object> requestBody = Map.of(
                "ids", List.of(String.valueOf(memberId))
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // 3️⃣ Pinecone에서 유저 벡터 검색
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://" + memberIndexName + ".svc.pinecone.io/vectors/fetch",
                HttpMethod.POST,
                entity,
                Map.class
        );

        // 4️⃣ Pinecone 응답에서 유저 벡터 추출
        Map<String, Object> vectors = (Map<String, Object>) response.getBody().get("vectors");
        if (vectors == null || vectors.isEmpty()) {
            return null;
        }

        return (List<Float>) vectors.get(String.valueOf(memberId));
    }


}

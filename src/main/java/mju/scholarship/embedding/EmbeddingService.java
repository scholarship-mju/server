package mju.scholarship.embedding;

import lombok.RequiredArgsConstructor;
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
public class EmbeddingService {

    private final MemberRepository memberRepository;
    @Value("${openai.api-key}")
    private  String apiKey;

    private final ScholarShipRepository scholarShipRepository;


    private final RestTemplate restTemplate;
    private static final String openaiUrl = "https://api.openai.com/v1/embeddings";

    // 텍스트를 임베딩
    public List<Float> getEmbedding(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of("model", "text-embedding-3-small", "input", List.of(text));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange("https://api.openai.com/v1/embeddings", HttpMethod.POST, entity, Map.class);

        return (List<Float>) ((List<Map<String, Object>>) response.getBody().get("data")).get(0).get("embedding");
    }

    public List<Float> embeddingScholarship(Long scholarshipId) {
        Scholarship scholarship = scholarShipRepository.findById(scholarshipId)
                .orElseThrow(ScholarshipNotFoundException::new);

        String scholarshipText = generateScholarshipText(scholarship);
       return getEmbedding(scholarshipText);
    }

    // 유저 임베딩
    public List<Float> embeddingMember(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        String memberText = generateMemberText(member);
        return getEmbedding(memberText);
    }

    public String generateMemberText(Member member){
        return member.getUniversity() + " " +
                member.getAge() + " " +
                member.getGender() + " " +
                member.getProvince() + " " +
                member.getCity() + " " +
                member.getDepartment() + " " +
                member.getGrade() + " " +
                "소득분위 " + member.getIncomeQuantile();
    }

    private String generateScholarshipText(Scholarship scholarship) {
        return scholarship.getName() + " " +
                scholarship.getOrganizationType() + " " +
                scholarship.getProductType() + " " +
                scholarship.getFinancialAidType() + " " +
                scholarship.getUniversityType() + " " +
                scholarship.getGradeType() + " " +
                scholarship.getDepartmentType() + " " +
                scholarship.getGradeRequirement() + " " +
                scholarship.getIncomeRequirement() + " " +
                scholarship.getSupportDetails() + " " +
                scholarship.getSpecialQualification() + " " +
                scholarship.getResidencyRequirement() + " " +
                scholarship.getSelectionMethod() + " " +
                scholarship.getEligibilityRestriction() + " " +
                (scholarship.getRecommendationRequired() ? "추천 필요" : "추천 없음") + " " +
                scholarship.getSubmitDocumentDetail();
    }
}

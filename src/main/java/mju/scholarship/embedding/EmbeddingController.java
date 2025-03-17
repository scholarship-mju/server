package mju.scholarship.embedding;

import lombok.RequiredArgsConstructor;
import mju.scholarship.result.ResultResponse;
import mju.scholarship.result.code.ResultCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/embedding")
public class EmbeddingController {

    private final EmbeddingService embeddingService;
    private final PineconeService pineconeService;

    // 특정 장학금 임베딩
    @PostMapping("/{scholarshipId}")
    public ResponseEntity<ResultResponse> embeddingScholarship(@PathVariable Long scholarshipId) {
        pineconeService.saveScholarshipVector(scholarshipId);
        return ResponseEntity.ok().body(ResultResponse.of(ResultCode.EmbeddingScholarshipSuccess));
    }

    // 전체 장학금 임베딩
    @PostMapping("/scholarship/all")
    public ResponseEntity<ResultResponse> embeddingAllScholarship() {
        pineconeService.saveAllScholarshipVector();
        return ResponseEntity.ok().body(ResultResponse.of(ResultCode.EmbeddingScholarshipSuccess));
    }

    @PostMapping("/member")
    public ResponseEntity<ResultResponse> embeddingAllMember() {
        pineconeService.saveAllMember();
        return ResponseEntity.ok().body(ResultResponse.of(ResultCode.EmbeddingScholarshipSuccess));
    }
}

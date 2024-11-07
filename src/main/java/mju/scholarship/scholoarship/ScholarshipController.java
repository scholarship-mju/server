package mju.scholarship.scholoarship;

import lombok.RequiredArgsConstructor;
import mju.scholarship.result.ResultResponse;
import mju.scholarship.scholoarship.dto.CreateScholarshipRequest;
import mju.scholarship.scholoarship.dto.ScholarshipResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static mju.scholarship.result.code.ResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("scholarship")
public class ScholarshipController {

    private final ScholarshipService scholarshipService;


    // 장학금 생성
    @PostMapping("/create")
    public ResponseEntity<ResultResponse> createScholarship(@RequestBody CreateScholarshipRequest scholarship) {
        scholarshipService.createScholarship(scholarship);
        return ResponseEntity.ok(ResultResponse.of(CreateScholarshipSuccess));
    }

    // 이미 받은 장학금 등록
    @PostMapping("{scholarshipId}/got")
    public ResponseEntity<ResultResponse> addGotScholarships(@PathVariable Long scholarshipId) {
        scholarshipService.addGotScholarships(scholarshipId);
        return ResponseEntity.ok(ResultResponse.of(AddGotScholarshipSuccess));
    }

    // 이미 받은 장학금 조회
    @GetMapping("got")
    public ResponseEntity<List<ScholarshipResponse>> getAllGotScholarships() {
        return ResponseEntity.ok().body(scholarshipService.getAllGotScholarships());
    }

    // 장학금 찜하기
    @PostMapping("{scholarshipId}/interest")
    public ResponseEntity<ResultResponse> addInterestScholarship(@PathVariable Long scholarshipId){
        scholarshipService.addInterestScholarship(scholarshipId);
        return ResponseEntity.ok(ResultResponse.of(InterestScholarshipSuccess));
    }

    // 찜한 장학금 조회
    @GetMapping("interest")
    public ResponseEntity<List<ScholarshipResponse>> getAllInterestScholarships(){
        return ResponseEntity.ok().body(scholarshipService.getAllInterestScholarships());
    }

    // 찜한 장학금에서 제외
    @DeleteMapping("{scholarshipId}/interest")
    public ResponseEntity<ResultResponse> deleteInterestScholarship(@PathVariable Long scholarshipId){
        scholarshipService.deleteInterestScholarship(scholarshipId);
        return ResponseEntity.ok(ResultResponse.of(DeleteInterestScholarshipSuccess));
    }

    // 전체 장학금 조회
    @GetMapping("/all")
    public ResponseEntity<List<Scholarship>> getAllScholarships() {
        return ResponseEntity.ok().body(scholarshipService.getAllScholarships());
    }

    // 장학금 단건 조회
    @GetMapping("{scholarshipId}")
    public ResponseEntity<ScholarshipResponse> getOneScholarship(@PathVariable Long scholarshipId) {
        return ResponseEntity.ok().body(scholarshipService.getOneScholarship(scholarshipId));
    }

//    @GetMapping("my-scholarship")
//    public ResponseEntity<List<Scholarship>> getMyScholarship() {
//        return ResponseEntity.ok().body(scholarshipService.getMyScholarship());
//    }
}

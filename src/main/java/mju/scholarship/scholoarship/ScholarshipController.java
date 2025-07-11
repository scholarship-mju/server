package mju.scholarship.scholoarship;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.member.entity.ScholarshipStatus;
import mju.scholarship.result.ErrorResponse;
import mju.scholarship.result.ResultResponse;
import mju.scholarship.scholoarship.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static mju.scholarship.result.code.ResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scholarship")
@Tag(name = "Scholarship", description = "장학금 관리 API")
@Slf4j
public class ScholarshipController {

    private final ScholarshipService scholarshipService;

//    @Operation(summary = "장학금 생성", description = "새로운 장학금을 생성합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "장학금 생성 성공",
//                    content = @Content(schema = @Schema(implementation = ResultResponse.class))),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
//    })
//    @PostMapping("/create")
//    public ResponseEntity<ResultResponse> createScholarship(@RequestBody @Parameter(description = "생성할 장학금 정보") CreateScholarshipRequest scholarship) {
//        scholarshipService.createScholarship(scholarship);
//        return ResponseEntity.ok(ResultResponse.of(CreateScholarshipSuccess));
//    }

    @Operation(summary = "받은 장학금 등록", description = "이미 받은 장학금을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "받은 장학금 등록 성공",
                    content = @Content(schema = @Schema(implementation = ResultResponse.class))),
            @ApiResponse(responseCode = "404", description = "장학금 ID를 찾을 수 없음", content = @Content)
    })
    @PostMapping("/got/{scholarshipId}")
    public ResponseEntity<ResultResponse> addGotScholarships(@PathVariable @Parameter(description = "장학금 ID") Long scholarshipId) {
        scholarshipService.addGotScholarships(scholarshipId);
        return ResponseEntity.ok(ResultResponse.of(AddGotScholarshipSuccess));
    }

    @Operation(summary = "받은 장학금 조회", description = "이미 받은 장학금을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "받은 장학금 조회 성공",
                    content = @Content(schema = @Schema(implementation = List.class))),
    })
    @GetMapping("/got")
    public ResponseEntity<List<GotScholarshipResponse>> getAllGotScholarships() {
        return ResponseEntity.ok().body(scholarshipService.getAllGotScholarships());
    }

    @Operation(summary = "받은 장학금 제거", description = "이미 받은 장학금을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "S207", description = "받은 장학금 제거 성공",
                    content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "해당 id의 장학금 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
    })
    @DeleteMapping("/got/{scholarshipId}")
    public ResponseEntity<ResultResponse> deleteGotScholarship(@PathVariable Long scholarshipId) {
        scholarshipService.deleteGotScholarship(scholarshipId);
        return ResponseEntity.ok().body(ResultResponse.of(DeleteGotScholarshipSuccess));
    }

    @Operation(summary = "장학금 찜하기", description = "특정 장학금을 찜 목록에 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "찜한 장학금 추가 성공",
                    content = @Content(schema = @Schema(implementation = ResultResponse.class))),
            @ApiResponse(responseCode = "404", description = "장학금 ID를 찾을 수 없음", content = @Content)
    })
    @PostMapping("/interest/{scholarshipId}")
    public ResponseEntity<ResultResponse> addInterestScholarship(@PathVariable @Parameter(description = "장학금 ID") Long scholarshipId) {
        scholarshipService.addInterestScholarship(scholarshipId);
        return ResponseEntity.ok(ResultResponse.of(InterestScholarshipSuccess));
    }

    @Operation(summary = "찜한 장학금 조회", description = "찜한 장학금을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "찜한 장학금 조회 성공",
                    content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "데이터 없음", content = @Content)
    })
    @GetMapping("/interest")
    public ResponseEntity<List<InterestedScholarshipResponse>> getAllInterestScholarships() {
        return ResponseEntity.ok().body(scholarshipService.getAllInterestScholarships());
    }

    @Operation(summary = "찜한 장학금에서 제외", description = "찜한 장학금을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "찜한 장학금 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ResultResponse.class))),
            @ApiResponse(responseCode = "404", description = "장학금 ID를 찾을 수 없음", content = @Content)
    })
    @DeleteMapping("/interest/{scholarshipId}")
    public ResponseEntity<ResultResponse> deleteInterestScholarship(@PathVariable @Parameter(description = "장학금 ID") Long scholarshipId) {
        scholarshipService.deleteInterestScholarship(scholarshipId);
        return ResponseEntity.ok(ResultResponse.of(DeleteInterestScholarshipSuccess));
    }

    @Operation(summary = "전체 장학금 조회", description = "모든 장학금을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 장학금 조회 성공",
                    content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "데이터 없음", content = @Content)
    })
    @GetMapping("/all")
    public ResponseEntity<Page<AllScholarshipResponse>> getAllScholarships(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> qualification,
            @RequestParam(required = false) String status,
            @RequestParam int page
    ) {
        ScholarshipProgressStatus scholarshipStatus = (status != null) ? ScholarshipProgressStatus.fromValue(status) : null;

        return ResponseEntity.ok().body(scholarshipService.getAllScholarships(keyword, qualification, scholarshipStatus, page));
    }

    @Operation(summary = "장학금 단건 조회", description = "특정 장학금을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장학금 조회 성공",
                    content = @Content(schema = @Schema(implementation = ScholarshipResponse.class))),
            @ApiResponse(responseCode = "404", description = "장학금 ID를 찾을 수 없음", content = @Content)
    })
    @GetMapping("{scholarshipId}")
    public ResponseEntity<ScholarshipResponse> getOneScholarship(@PathVariable @Parameter(description = "장학금 ID") Long scholarshipId) {
        return ResponseEntity.ok().body(scholarshipService.getOneScholarshipInRedis(scholarshipId));
    }


    @Operation(summary = "맞춤 장학금 조회", description = "맞춤 장학금 조회.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장학금 조회 성공",
                    content = @Content(schema = @Schema(implementation = Scholarship.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("my-scholarship")
    public ResponseEntity<List<Scholarship>> getMyScholarship() {
        return ResponseEntity.ok().body(scholarshipService.getMyScholarship());
    }

    @Operation(summary = "장학금 검증 (사진 첨부)", description = "유저가 특정 장학금을 획득했는지 확인합니다.")
    @PostMapping(path = "/got/{scholarshipId}/valid", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> validGotScholarship(
            @Parameter(description = "증빙 파일", required = true)
            @PathVariable Long scholarshipId,
            @RequestParam("file") List<MultipartFile> files
    ) {
        scholarshipService.validGotScholarship(scholarshipId, files);
        return ResponseEntity.ok().body(ResultResponse.of(ValidGotScholarshipSuccess));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchScholarshipResponse>> searchScholarship(@RequestParam String scholarshipName) {
        return ResponseEntity.ok().body(scholarshipService.searchScholarship(scholarshipName));
    }

    //추천 장학금 by 유저 DB
    @GetMapping("/recommend/db")
    public ResponseEntity<List<Scholarship>> getRecommendScholarshipByDB() {
        return ResponseEntity.ok().body(scholarshipService.getRecommendScholarshipByDB());
    }

    //추천 장학금 by 유저 Pinecone
//    @GetMapping("/recommend/pinecone")
//    public ResponseEntity<List<Scholarship>> getRecommendScholarshipByPinecone(){
//        return ResponseEntity.ok().body(scholarshipService.getRecommendScholarshipByPinecone());
//    }


    @GetMapping("/anonymous/all")
    public ResponseEntity<Page<AllScholarshipResponse>> getAllScholarshipsByAnonymous(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> qualification,
            @RequestParam(required = false) String status,
            @RequestParam int page
    ) {

        ScholarshipProgressStatus scholarshipStatus = (status != null) ? ScholarshipProgressStatus.fromValue(status) : null;

        return ResponseEntity.ok().body(scholarshipService.getAllScholarshipsByAnonymous(keyword, qualification, scholarshipStatus, page));
    }

    // 교내 장학금 조회
    @GetMapping("/myUniv")
    public ResponseEntity<List<UnivScholarshipResponse>> getUnivScholarship(
            @RequestParam(required = false) String keyword) {

        return ResponseEntity.ok().body(scholarshipService.getUnivScholarship(keyword));
    }

}

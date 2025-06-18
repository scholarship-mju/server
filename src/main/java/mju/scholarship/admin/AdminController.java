package mju.scholarship.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import mju.scholarship.admin.dto.MemberGotResponse;
import mju.scholarship.admin.dto.ScholarshipCrawlingRequest;
import mju.scholarship.admin.dto.ScholarshipInUnivRequest;
import mju.scholarship.member.entity.ScholarshipStatus;
import mju.scholarship.result.ResultResponse;
import mju.scholarship.result.code.ResultCode;
import mju.scholarship.scholoarship.dto.ValidAddScholarshipRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static mju.scholarship.result.code.ResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    //받은 장학금 검증 안된것들 조회 API
    @GetMapping("/got/{status}")
    public ResponseEntity<List<MemberGotResponse>> gotScholarshipConfirm(@PathVariable Integer status) {

        ScholarshipStatus scholarshipStatus = (status != null) ? ScholarshipStatus.fromValue(status) : null;
        return ResponseEntity.ok().body(adminService.gotScholarshipConfirm(scholarshipStatus));
    }

    // 검증 안된 받은 장학금 검증 확인 해주는 API
    @PutMapping("/got/validAdd")
    public ResponseEntity<ResultResponse> validAddGotScholarship(@RequestBody ValidAddScholarshipRequest request) {
        adminService.validAddGotScholarship(request);
        return ResponseEntity.ok(ResultResponse.of(ValidAddGotScholarshipSuccess));
    }

    // 장학금 csv 올리는 API
    @PostMapping("/upload/csv")
    public ResponseEntity<ResultResponse> uploadScholarshipCsv(@RequestParam("file") MultipartFile file) {
        adminService.uploadScholarshipCsv(file);
        return ResponseEntity.ok().body(ResultResponse.of(UploadScholarshipSuccess));
    }

    // 크롤링한 장학금 등록
    @PostMapping("/upload/crawling")
    public ResponseEntity<ResultResponse> uploadScholarshipCrawling(
            @RequestPart("data") ScholarshipCrawlingRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        adminService.uploadScholarshipCrawling(request, file);
        return ResponseEntity.ok().body(ResultResponse.of(UploadScholarshipSuccess));
    }

    //
    @PostMapping("/upload/inUniv")
    public ResponseEntity<ResultResponse> uploadScholarshipInUniv(@RequestBody ScholarshipInUnivRequest request) {
        adminService.uploadScholarshipInUniv(request);
        return ResponseEntity.ok().body(ResultResponse.of(UploadScholarshipSuccess));
    }

    // 모든 장학금의 상태를 최신화 하는 API
    @PostMapping("/progressStatus")
    public ResponseEntity<ResultResponse> setProgressStatus() {
        adminService.setProgressStatus();
        return ResponseEntity.ok().body(ResultResponse.of(SetProgressStatusSuccess));
    }

    @PostMapping("/progressStatus/async")
    public ResponseEntity<ResultResponse> setProgressStatusAsync() {
        adminService.setProgressStatusByAsync();
        return ResponseEntity.ok().body(ResultResponse.of(SetProgressStatusSuccess));
    }

    // 현재 접근하는 사용자가 admin인지 확인하는 API
    @GetMapping("/isAdmin")
    public ResponseEntity<ResultResponse> isAdmin() {
        return ResponseEntity.ok().body(ResultResponse.of(AdminAccessSuccess));
    }

    //받은 장학금 검증 사진 조회
    @GetMapping("/got/valid/{scholarshipId}/image")
    public ResponseEntity<String> getScholarshipImage(@PathVariable Long scholarshipId){
        return ResponseEntity.ok().body(adminService.getScholarshipImage(scholarshipId));
    }

}

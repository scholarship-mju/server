package mju.scholarship.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import mju.scholarship.admin.dto.MemberGotResponse;
import mju.scholarship.member.entity.ScholarshipStatus;
import mju.scholarship.result.ResultResponse;
import mju.scholarship.scholoarship.dto.ValidAddScholarshipRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static mju.scholarship.result.code.ResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    //받은 장학금 검증 안된것들 조회 API
    @GetMapping("/got/{status}")
    public ResponseEntity<List<MemberGotResponse>> gotScholarshipConfirm(@PathVariable Integer status) {

        ScholarshipStatus scholarshipStatus = (status != null) ? ScholarshipStatus.fromValue(status) : null;
        return ResponseEntity.ok().body(adminService.gotScholarshipConfirm(scholarshipStatus));
    }

    // 검증 안된 받은 장학금 검증 확인 해주는 API
    @PostMapping("/got/validAdd")
    public ResponseEntity<ResultResponse> validAddGotScholarship(@RequestBody ValidAddScholarshipRequest request) {
        adminService.validAddGotScholarship(request);
        return ResponseEntity.ok(ResultResponse.of(ValidAddGotScholarshipSuccess));
    }

    // 장학금 csv 올리는 API
    @PostMapping("/upload")
    public ResponseEntity<ResultResponse> uploadScholarshipCsv(@RequestParam("file") MultipartFile file) {
        adminService.uploadScholarshipCsv(file);
        return ResponseEntity.ok().body(ResultResponse.of(UploadScholarshipSuccess));
    }

    // TODO : 나중에는 스케줄링으로 돌리기 (자정에)
    @PostMapping("/progressStatus")
    public ResponseEntity<ResultResponse> setProgressStatus(){
        adminService.setProgressStatus();
        return ResponseEntity.ok().body(ResultResponse.of(SetProgressStatusSuccess));
    }

    @GetMapping("/isAdmin")
    public ResponseEntity<ResultResponse> isAdmin(){
        return ResponseEntity.ok().body(ResultResponse.of(AdminAccessSuccess));
    }
}

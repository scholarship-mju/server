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

import java.util.List;

import static mju.scholarship.result.code.ResultCode.ValidAddGotScholarshipSuccess;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/got/{status}")
    public ResponseEntity<List<MemberGotResponse>> gotScholarshipConfirm(@PathVariable Integer status) {

        ScholarshipStatus scholarshipStatus = (status != null) ? ScholarshipStatus.fromValue(status) : null;
        return ResponseEntity.ok().body(adminService.gotScholarshipConfirm(scholarshipStatus));
    }


    @PostMapping("/got/validAdd")
    public ResponseEntity<ResultResponse> validAddGotScholarship(@RequestBody ValidAddScholarshipRequest request) {
        adminService.validAddGotScholarship(request);
        return ResponseEntity.ok(ResultResponse.of(ValidAddGotScholarshipSuccess));
    }

}

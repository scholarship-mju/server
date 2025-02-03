package mju.scholarship.admin;

import lombok.RequiredArgsConstructor;

import mju.scholarship.admin.dto.MemberGotResponse;
import mju.scholarship.member.entity.ScholarshipStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}

package mju.scholarship.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.member.dto.LoginDto;
import mju.scholarship.member.dto.MemberInfoRequest;
import mju.scholarship.member.dto.SignupDto;
import mju.scholarship.member.dto.UpdateMemberInfoRequest;
import mju.scholarship.result.ResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static mju.scholarship.result.code.ResultCode.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("login")
    public String login(@RequestBody LoginDto loginDto) {
        memberService.login(loginDto);
        return "login success";
    }

    @PutMapping("/update-info")
    public ResponseEntity<ResultResponse> updateInfo(UpdateMemberInfoRequest request){
        memberService.updateInfo(request);
        return ResponseEntity.ok(ResultResponse.of(ProfileUpdateSuccess));
    }

    @PostMapping("/{scholarshipId}/interest")
    public ResponseEntity<ResultResponse> interestScholarship(@PathVariable Long scholarshipId){
        memberService.interestScholarship(scholarshipId);
        return ResponseEntity.ok(ResultResponse.of(InterestScholarshipSuccess));
    }


    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody SignupDto signupDto) {
        memberService.signup(signupDto);
        return ResponseEntity.ok("signup success");
    }

    @PostMapping("create-info")
    public ResponseEntity<String> createInfo(@RequestBody MemberInfoRequest memberInfoRequest){
        memberService.createInfo(memberInfoRequest);
        return ResponseEntity.ok("create info success");
    }


}

package mju.scholarship.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.member.dto.*;
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



    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody SignupDto signupDto) {
        memberService.signup(signupDto);
        return ResponseEntity.ok("signup success");
    }

    // 내 정보 생성
    @PostMapping("create-info")
    public ResponseEntity<ResultResponse> createInfo(@RequestBody MemberInfoRequest memberInfoRequest){
        memberService.createInfo(memberInfoRequest);
        return ResponseEntity.ok().body(ResultResponse.of(CreateInfoSuccess));
    }

    // 내 정보 변경
    @PostMapping("/my")
    public ResponseEntity<MemberResponse> updateInfo(@RequestBody UpdateMemberInfoRequest request){
        log.info("request = {}", request.getUniversity());
        memberService.updateInfo(request);
        return ResponseEntity.ok().body(memberService.updateInfo(request));
    }

    // 내 정보 조회
    @GetMapping("/my")
    public ResponseEntity<MemberResponse> getMyInfo(){
        return ResponseEntity.ok().body(memberService.getMyInfo());
    }


}

package mju.scholarship.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
@Tag(name = "Member", description = "회원 관리 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "사용자 로그인", description = "주어진 자격 증명으로 사용자를 로그인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
    })
    @PostMapping("login")
    public String login(@RequestBody @Parameter(description = "로그인 정보") LoginDto loginDto) {
        memberService.login(loginDto);
        return "login success";
    }

    @Operation(summary = "사용자 회원가입", description = "새 사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody @Parameter(description = "회원가입 정보") SignupDto signupDto) {
        memberService.signup(signupDto);
        return ResponseEntity.ok("signup success");
    }

    @Operation(summary = "내 정보 생성", description = "사용자의 개인 정보를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 생성 성공",
                    content = @Content(schema = @Schema(implementation = ResultResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    @PostMapping("create-info")
    public ResponseEntity<ResultResponse> createInfo(@RequestBody @Parameter(description = "생성할 정보") MemberInfoRequest memberInfoRequest) {
        memberService.createInfo(memberInfoRequest);
        return ResponseEntity.ok().body(ResultResponse.of(CreateInfoSuccess));
    }

    @Operation(summary = "내 정보 수정", description = "사용자의 개인 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 수정 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    })
    @PostMapping("/my")
    public ResponseEntity<MemberResponse> updateInfo(@RequestBody @Parameter(description = "수정할 정보") UpdateMemberInfoRequest request) {
        log.info("request = {}", request.getUniversity());
        memberService.updateInfo(request);
        return ResponseEntity.ok().body(memberService.updateInfo(request));
    }

    @Operation(summary = "내 정보 조회", description = "사용자의 개인 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원 정보 없음", content = @Content)
    })
    @GetMapping("/my")
    public ResponseEntity<MemberResponse> getMyInfo() {
        return ResponseEntity.ok().body(memberService.getMyInfo());
    }
}

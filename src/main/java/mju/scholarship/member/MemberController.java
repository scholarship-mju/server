package mju.scholarship.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(summary = "첫번째 로그인 사용자 데이터 추가", description = "처음 로그인 한 유저의 정보 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 등록 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "400", description = "사용자 정보 등록 실패", content = @Content)
    })
    @PostMapping("/first-login")
    public ResponseEntity<ResultResponse> firstLogin(@RequestBody CreateNewUserRequest createNewUserRequest){
        memberService.firstLogin(createNewUserRequest);
        return ResponseEntity.ok().body(ResultResponse.of(FirstLoginSuccess));
    }

    @Operation(summary = "사용자 랭킹 조회", description = "장학금 많이 받은 순위 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "랭킹 조회 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/rank")
    @CrossOrigin(origins = {"https://v0-scholarship-qj0ukyqoa2i-7lelo3qjo-51taeks-projects.vercel.app", "http://ec2-15-164-84-210.ap-northeast-2.compute.amazonaws.com", "http://ec2-43-201-128-122.ap-northeast-2.compute.amazonaws.com"})
    public ResponseEntity<RankResponse> getRank(){
        return ResponseEntity.ok().body(memberService.getRank());
    }


    @Operation(summary = "사용자 랭킹 조회 mini", description = "장학금 많이 받은 순위 조회 mini")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "랭킹 조회 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/rank/mini")
    @CrossOrigin(origins = {"https://v0-scholarship-qj0ukyqoa2i-7lelo3qjo-51taeks-projects.vercel.app", "http://ec2-15-164-84-210.ap-northeast-2.compute.amazonaws.com", "http://ec2-43-201-128-122.ap-northeast-2.compute.amazonaws.com"})
    public ResponseEntity<RankResponse> getRankMini(){
        return ResponseEntity.ok().body(memberService.getRankMini());
    }
}

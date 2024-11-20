package mju.scholarship.result.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    CreateInfoSuccess(200, "M202", "회원 정보 등록 성공"),
    ProfileUpdateSuccess(200, "M203", "프로필 변경 성공"),
    CreateScholarshipSuccess(200, "S201", "장학금 생성 성공"),
    InterestScholarshipSuccess(200, "S205", "장학금 찜하기 성공"),
    DeleteInterestScholarshipSuccess(200, "S206", "찜한 장학금 제거 성공"),
    AddGotScholarshipSuccess(200, "S206", "받은 장학금 등록 성공"),
    DeleteGotScholarshipSuccess(200, "S207", "받은 장학금 제거 성공"),
    FirstLoginSuccess(200, "L201", "첫번째 로그인 성공"),
    ;

    private final int status;
    private final String code;
    private final String message;
}

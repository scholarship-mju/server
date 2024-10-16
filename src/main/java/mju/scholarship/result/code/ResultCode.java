package mju.scholarship.result.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    ProfileUpdateSuccess(200, "M002", "프로필 변경 성공"),
    InterestScholarshipSuccess(200, "S005", "장학금 찜하기 성공"),
    ;

    private final int status;
    private final String code;
    private final String message;
}

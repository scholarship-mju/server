package mju.scholarship.result.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    MemberNotFoundException(500, "M001", "유저가 존재하지 않습니다"),
    IllegalRegistrationIdException(500, "O001", "Illegal Registration Id"),
    TokenException(500, "T001", "정상적인 토큰이 아닙니다"),
    TokenInvalidException(500, "T002", "검증된 토큰이 아닙니다"),
    TokenExpiredException(500, "T003", "토큰이 만료되었씁니다"),
    JwtSignatureInvalid(500, "T004", "서명이 검증되지 않음"),
    ScholarshipNotFoundException(500, "S001", "장학금을 찾을 수 없습니다")
    ;


    private final int status;
    private final String code;
    private final String message;
}

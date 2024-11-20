package mju.scholarship.result.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    MemberNotFoundException(500, "M501", "유저가 존재하지 않습니다(토큰오류)"),
    IllegalRegistrationIdException(500, "A501", "Oauth 로그인 오류"),
    TokenException(500, "T501", "정상적인 토큰이 아닙니다"),
    TokenInvalidException(500, "T502", "검증된 토큰이 아닙니다"),
    TokenExpiredException(500, "T503", "토큰이 만료되었씁니다"),
    JwtSignatureInvalid(500, "T504", "서명이 검증되지 않음"),
    RefreshTokenNotFoundException(500, "T505", "리프레시 토큰을 찾을 수 없음"),
    TokenNotFoundException(500, "T506", "토큰을 찾을 수 없습니다"),
    ScholarshipNotFoundException(500, "S501", "장학금을 찾을 수 없습니다"),
    ;


    private final int status;
    private final String code;
    private final String message;
}

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

    ScholarshipNotFoundException(500, "S507", "장학금을 찾을 수 없습니다"),
    AlreadyGotScholarshipException(500, "S508", "이미 받은 장학금에 등록되었습니다"),
    AlreadyInterestedScholarshipException(500, "S509", "이미 찜하였습니다"),
    GotScholarshipNotFoundException(500, "S510", "이미 받은 장학금을 찾을 수 없습니다"),
    InterestedScholarshipNotFoundException(500, "S511", "찜한 장학금을 찾을 수 없습니다"),


    FileConvertException(500, "F501", "파일 변환 실패"),
    FileUploadException(500, "F502", "파일 업로드 실패"),


    ReviewNotFoundException(500, "R001", "리뷰를 찾을 수 없습니다"),
    AlreadyLikeReview(500, "R002", "이미 좋아요를 누른 리뷰입니다"),
    ;


    private final int status;
    private final String code;
    private final String message;
}

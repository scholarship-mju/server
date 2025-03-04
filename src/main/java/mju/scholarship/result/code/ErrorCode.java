package mju.scholarship.result.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 유저
    MemberNotFoundException(404, "M401", "유저가 존재하지 않습니다(토큰오류)"),
    IllegalRegistrationIdException(401, "A501", "Oauth 로그인 오류"),

    // 토큰
    TokenException(401, "T401", "정상적인 토큰이 아닙니다"),
    TokenInvalidException(401, "T402", "검증된 토큰이 아닙니다"),
    TokenExpiredException(401, "T403", "토큰이 만료되었씁니다"),
    JwtSignatureInvalid(401, "T404", "서명이 검증되지 않음"),
    RefreshTokenNotFoundException(401, "T405", "리프레시 토큰을 찾을 수 없음"),
    TokenNotFoundException(401, "T406", "토큰을 찾을 수 없습니다"),

    // 장학금
    ScholarshipNotFoundException(404, "S401", "장학금을 찾을 수 없습니다"),
    AlreadyGotScholarshipException(400, "S402", "이미 받은 장학금에 등록되었습니다"),
    AlreadyInterestedScholarshipException(400, "S403", "이미 찜하였습니다"),
    GotScholarshipNotFoundException(404, "S404", "이미 받은 장학금을 찾을 수 없습니다"),
    InterestedScholarshipNotFoundException(404, "S405", "찜한 장학금을 찾을 수 없습니다"),
    CsvParsingException(500, "S501", "장학금 파싱 실패"),

    // 파일
    FileConvertException(500, "F501", "파일 변환 실패"),
    FileUploadException(500, "F502", "파일 업로드 실패"),

    // 리뷰
    ReviewNotFoundException(404, "R401", "리뷰를 찾을 수 없습니다"),
    AlreadyLikeReview(400, "R402", "이미 좋아요를 누른 리뷰입니다"),
    NotFoundLikeReview(400, "R403", "리뷰에 좋아요를 누르지 않았습니다"),

    ;


    private final int status;
    private final String code;
    private final String message;
}

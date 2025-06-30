package mju.scholarship.result.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 유저
    MemberNotFoundException(404, "MemberNotFoundException", "유저가 존재하지 않습니다(토큰오류)"),
    IllegalRegistrationIdException(401, "IllegalRegistrationIdException", "Oauth 로그인 오류"),
    NotAdminException(403, "NotAdminException", "관리자 계정이 아닙니다"),

    // 토큰
    TokenException(401, "TokenException", "정상적인 토큰이 아닙니다"),
    TokenInvalidException(401, "TokenInvalidException", "검증된 토큰이 아닙니다"),
    TokenExpiredException(401, "TokenExpiredException", "토큰이 만료되었씁니다"),
    JwtSignatureInvalid(401, "JwtSignatureInvalid", "서명이 검증되지 않음"),
    RefreshTokenNotFoundException(401, "RefreshTokenNotFoundException", "리프레시 토큰을 찾을 수 없음"),
    TokenNotFoundException(401, "TokenNotFoundException", "토큰을 찾을 수 없습니다"),

    // 장학금
    ScholarshipNotFoundException(404, "ScholarshipNotFoundException", "장학금을 찾을 수 없습니다"),
    AlreadyGotScholarshipException(400, "AlreadyGotScholarshipException", "이미 받은 장학금에 등록되었습니다"),
    AlreadyInterestedScholarshipException(400, "AlreadyInterestedScholarshipException", "이미 찜하였습니다"),
    GotScholarshipNotFoundException(404, "GotScholarshipNotFoundException", "이미 받은 장학금을 찾을 수 없습니다"),
    InterestedScholarshipNotFoundException(404, "InterestedScholarshipNotFoundException", "찜한 장학금을 찾을 수 없습니다"),
    CsvParsingException(500, "CsvParsingException", "장학금 파싱 실패"),
    AlreadyScholarshipException(400, "AlreadyScholarshipException", "이미 있는 장학금입니다"),
    ViewCountUpdateException(500, "ViewCountUpdateException", "장학금 조회수 레디스에서 DB로 적용도중 예외"),

    // 파일
    FileConvertException(500, "FileConvertException", "파일 변환 실패"),
    FileUploadException(500, "FileUploadException", "파일 업로드 실패"),

    // 리뷰
    ReviewNotFoundException(404, "ReviewNotFoundException", "리뷰를 찾을 수 없습니다"),
    AlreadyLikeReview(400, "AlreadyLikeReview", "이미 좋아요를 누른 리뷰입니다"),
    NotFoundLikeReview(400, "NotFoundLikeReview", "리뷰에 좋아요를 누르지 않았습니다"),

    ;


    private final int status;
    private final String code;
    private final String message;
}

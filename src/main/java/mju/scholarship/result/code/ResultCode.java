package mju.scholarship.result.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    // 유저
    CreateInfoSuccess(200, "M202", "회원 정보 등록 성공"),
    ProfileUpdateSuccess(200, "M203", "프로필 변경 성공"),
    DELETE_ACCOUNT_SUCCESS(200, "M204", "회원 탈퇴 성공"),

    //장학금
    UploadScholarshipSuccess(200, "S201", "장학금 등록 성공"),
    InterestScholarshipSuccess(200, "S205", "장학금 찜하기 성공"),
    DeleteInterestScholarshipSuccess(200, "S206", "찜한 장학금 제거 성공"),
    AddGotScholarshipSuccess(200, "S206", "받은 장학금 등록 성공"),
    ValidGotScholarshipSuccess(200, "S207", "장학금 검증 사지 등록 성공"),
    DeleteGotScholarshipSuccess(200, "S207", "받은 장학금 제거 성공"),
    ValidAddGotScholarshipSuccess(200, "S208", "인증된 받은 장학금 추가"),
    SetProgressStatusSuccess(200, "S209", "장학금 상태 세팅 성공"),

    //임베딩
    EmbeddingScholarshipSuccess(200, "E200", "장학금 임데이 성공"),

    //
    FirstLoginSuccess(200, "L201", "첫번째 로그인 성공"),
    AdminAccessSuccess(200, "L202", "어드민 계정 인증 성공"),

    //리뷰
    CreateReviewSuccess(200, "R001", "리뷰 생성 성공"),
    LikeReviewSuccess(200, "R002", "리뷰 좋아요 성공"),
    CancelLikeReview(200, "R003", "리뷰 좋아요 취소 성공"),
    DeleteReviewSuccess(200, "R003", "리뷰 삭제 성공"),
    ;

    private final int status;
    private final String code;
    private final String message;
}

package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class NotFoundLikeReview extends BusinessException {

    public NotFoundLikeReview() {
        super(ErrorCode.NotFoundLikeReview);
    }
}

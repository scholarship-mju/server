package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class AlreadyLikeReview extends BusinessException {

    public AlreadyLikeReview() {
        super(ErrorCode.AlreadyLikeReview);
    }
}

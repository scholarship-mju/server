package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class ReviewNotFoundException extends BusinessException {

    public ReviewNotFoundException() {
        super(ErrorCode.ReviewNotFoundException);
    }

}

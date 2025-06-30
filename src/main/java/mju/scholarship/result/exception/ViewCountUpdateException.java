package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class ViewCountUpdateException extends BusinessException{

    public ViewCountUpdateException() {
        super(ErrorCode.ViewCountUpdateException);
    }
}

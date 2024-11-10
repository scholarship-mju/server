package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class RefreshTokenNotFoundException extends BusinessException{

    public RefreshTokenNotFoundException() {
        super(ErrorCode.RefreshTokenNotFoundException);
    }
}

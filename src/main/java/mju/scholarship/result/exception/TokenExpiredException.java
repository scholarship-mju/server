package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class TokenExpiredException extends BusinessException{

    public TokenExpiredException() {
        super(ErrorCode.TokenExpiredException);
    }
}

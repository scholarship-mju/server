package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class TokenInvalidException extends BusinessException{

    public TokenInvalidException() {
        super(ErrorCode.TokenInvalidException);
    }
}

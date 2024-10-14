package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class JwtSignatureInvalidException extends BusinessException{
    public JwtSignatureInvalidException() {
        super(ErrorCode.JwtSignatureInvalid);
    }
}

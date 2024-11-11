package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class TokenNotFoundException extends BusinessException{

    public TokenNotFoundException() {
        super(ErrorCode.TokenNotFoundException);
    }

}

package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class IllegalRegistrationIdException extends BusinessException{

    public IllegalRegistrationIdException() {
        super(ErrorCode.IllegalRegistrationIdException);
    }
}

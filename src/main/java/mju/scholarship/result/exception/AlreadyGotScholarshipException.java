package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class AlreadyGotScholarshipException extends BusinessException{

    public AlreadyGotScholarshipException() {
        super(ErrorCode.AlreadyGotScholarshipException);
    }
}

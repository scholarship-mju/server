package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class AlreadyScholarshipException extends BusinessException{

    public AlreadyScholarshipException() {
        super(ErrorCode.AlreadyScholarshipException);
    }
}

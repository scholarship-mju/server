package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class AlreadyInterestedScholarshipException extends BusinessException{

    public AlreadyInterestedScholarshipException() {
        super(ErrorCode.AlreadyInterestedScholarshipException);
    }
}

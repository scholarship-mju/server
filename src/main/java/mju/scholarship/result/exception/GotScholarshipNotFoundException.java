package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class GotScholarshipNotFoundException extends BusinessException{
    public GotScholarshipNotFoundException() {
        super(ErrorCode.GotScholarshipNotFoundException);
    }
}

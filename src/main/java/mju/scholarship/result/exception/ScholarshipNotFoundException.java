package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class ScholarshipNotFoundException extends BusinessException{

    public ScholarshipNotFoundException() {
        super(ErrorCode.ScholarshipNotFoundException);
    }
}

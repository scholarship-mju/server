package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class InterestedScholarshipNotFoundException extends BusinessException{

    public InterestedScholarshipNotFoundException() {
        super(ErrorCode.InterestedScholarshipNotFoundException);
    }
}

package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class MemberNotFoundException extends BusinessException {

    public MemberNotFoundException() {
        super(ErrorCode.MemberNotFoundException);
    }
}

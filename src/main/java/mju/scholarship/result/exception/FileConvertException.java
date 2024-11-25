package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class FileConvertException extends BusinessException{

    public FileConvertException() {
        super(ErrorCode.FileConvertException);
    }
}

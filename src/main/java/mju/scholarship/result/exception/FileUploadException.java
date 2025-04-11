package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class FileUploadException extends BusinessException{

    public FileUploadException() {
        super(ErrorCode.FileUploadException);
    }

    public FileUploadException(String message) {
        super(message, ErrorCode.FileUploadException);

    }
}

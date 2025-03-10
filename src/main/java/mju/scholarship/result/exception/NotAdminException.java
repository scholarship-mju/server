package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class NotAdminException extends BusinessException {
  public NotAdminException() {
    super(ErrorCode.NotAdminException);
  }
}

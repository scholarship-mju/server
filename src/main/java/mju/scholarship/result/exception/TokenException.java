package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class TokenException extends BusinessException {
  public TokenException() {
    super(ErrorCode.TokenException);
  }
}

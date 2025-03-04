package mju.scholarship.result.exception;

import mju.scholarship.result.code.ErrorCode;

public class CsvParsingException extends BusinessException {

  public CsvParsingException() {
    super(ErrorCode.CsvParsingException);
  }
}

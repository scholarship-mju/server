package mju.scholarship.result;

import mju.scholarship.result.code.ErrorCode;
import mju.scholarship.result.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode(); // 예외에서 ErrorCode 추출
        ErrorResponse errorResponse = ErrorResponse.from(errorCode); // ErrorResponse 생성
        return ResponseEntity
                .status(errorCode.getStatus()) // HTTP 상태 코드 설정
                .body(errorResponse); // ErrorResponse 반환
    }
}

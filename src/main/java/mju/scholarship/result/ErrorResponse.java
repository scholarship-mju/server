package mju.scholarship.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import mju.scholarship.result.code.ErrorCode;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    @Schema(description = "HTTP 상태 코드", example = "500")
    private final int status;

    @Schema(description = "에러 코드", example = "M001")
    private final String code;

    @Schema(description = "에러 메시지", example = "유저가 존재하지 않습니다")

    private final String message;

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
    }
}

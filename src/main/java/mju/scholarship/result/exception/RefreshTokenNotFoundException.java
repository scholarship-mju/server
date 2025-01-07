package mju.scholarship.result.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.result.code.ErrorCode;

import java.io.IOException;

@Slf4j
public class RefreshTokenNotFoundException extends BusinessException {

    public RefreshTokenNotFoundException() {
        super(ErrorCode.RefreshTokenNotFoundException);
        log.info("컨트롤러어드바이스에서 관리하는 비즈니스예외처리 일어남");
    }
}

package kcs.funding.fundingboost.domain.dto.global;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kcs.funding.fundingboost.domain.exception.ErrorCode;
import lombok.Getter;

@Getter
@Schema(name = "ExceptionDto", description = "API 예외 발생 시 응답 DTO")
public class ExceptionDto {
    @Schema(name = "code", description = "에러 코드")
    @NotNull
    private final Integer code;

    @Schema(name = "message", description = "에러 메시지")
    @NotNull
    private final String message;

    public ExceptionDto(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public static ExceptionDto of(ErrorCode errorCode) {
        return new ExceptionDto(errorCode);
    }
}


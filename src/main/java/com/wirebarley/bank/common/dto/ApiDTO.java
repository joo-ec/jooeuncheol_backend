package com.wirebarley.bank.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Response DTO")
@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiDTO<T> {
    @Schema(description = "응답 코드")
    private final int rtCode;

    @Schema(description = "응답 메시지")
    @Setter
    private String rtMsg;

    @Schema(description = "응답 결과 데이터")
    private final T data;

//    private final PageDTO page;

    @Schema(description = "토큰 값")
    @Setter
    private String token;
}

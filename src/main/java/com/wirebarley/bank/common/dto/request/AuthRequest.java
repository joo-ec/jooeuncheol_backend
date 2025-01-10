package com.wirebarley.bank.common.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Schema(description = "사용자 로그인 요청 DTO")
@Data
@Builder
public class AuthRequest {

    @Schema(description = "사용자 ID", example = "user")
    private String userId;

    @Schema(description = "사용자 비밀번호", example = "user123!")
    private String password;

}

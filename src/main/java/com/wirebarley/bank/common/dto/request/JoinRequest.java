package com.wirebarley.bank.common.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Schema(description = "사용자 회원 가입 요청 DTO")
@Data
@Builder
public class JoinRequest {
    @Schema(description = "사용자 ID")
    private String userId;
    @Schema(description = "사용자 비밀번호")
    private String password;
    @Schema(description = "사용자 이름")
    private String name;
    @Schema(description = "사용자 주소")
    private String address;
    @Schema(description = "사용자 전화번호")
    private String telNo;
    @Schema(description = "사용자 생년월일")
    private String brithDate;
}

package com.wirebarley.bank.common.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Schema(description = "계좌 정보 조회 DTO")
@Data
@Builder
public class AccountVerifyRequest {

    @Schema(description = "은행 코드", required = true, example = "CMMBAK001")
    private String bankCode;

    @Schema(description = "계좌 번호", required = true, example = "002000111223")
    private String accountNumber;

    @Schema(description = "계좌 비밀 번호", required = true, example = "1111")
    private String accountPassword;

    @Schema(description = "사용자 ID", required = true, example = "user")
    private String userId;

}

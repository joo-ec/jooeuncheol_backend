package com.wirebarley.bank.common.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Schema(description = "출금 요청 DTO")
@Data
@Builder
public class WithdrawalRequest {
    @Schema(description = "계좌 번호")
    private String accountNumber;
    @Schema(description = "은행 코드")
    private String bankCode;
    @Schema(description = "계좌 비밀번호")
    private String accountPassword;
    @Schema(description = "거래 금액")
    private long amount;
    @Schema(description = "사용자 ID")
    private String userId;
}

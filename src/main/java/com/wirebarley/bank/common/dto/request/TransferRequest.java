package com.wirebarley.bank.common.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Schema(description = "거래 요청 DTO")
@Data
@Builder
public class TransferRequest {
    @Schema(description = "계좌 번호")
    private String accountNumber;
    @Schema(description = "은행 코드")
    private String bankCode;
    @Schema(description = "타 계좌 번호")
    private String targetAccountNumber;
    @Schema(description = "타 은행 코드")
    private String targetBankCode;
    @Schema(description = "계좌 비밀 번호")
    private String accountPassword;

    @Schema(description = "거래 금액")
    private long amount;

    @Schema(description = "사용자 ID")
    private String userId;
}

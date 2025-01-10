package com.wirebarley.bank.common.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "계좌 정보 DTO")
@Data
@Builder
public class AccountDTO {

    @Schema(description = "순번")
    private long NO;
    @Schema(description = "계좌 번호")
    private String accountNumber;
    @Schema(description = "사용자 ID")
    private String userId;
    @Schema(description = "은행 코드")
    private String bankCode;
    @Schema(description = "은행 코드명")
    private String bankCodeName;
    @Schema(description = "상품 코드")
    private String productCode;
    @Schema(description = "상품 코드명")
    private String productCodeName;
    @Schema(description = "승인 여부 코드")
    private String approvalStatus;
    @Schema(description = "승인 여부 코드명")
    private String approvalStatusName;

    @Schema(description = "반려 사유")
    private String rejectReason;
    @Schema(description = "사용 유무 코드")
    private String useStatus;
    @Schema(description = "사용 유무 코드명")
    private String useStatusName;

    @Schema(description = "잔고")
    private long balance;
    @Schema(description = "출금 한도")
    private long withdrawalBounds;
    @Schema(description = "이체 한도")
    private long transferBounds;
    @Schema(description = "금리")
    private Double interestRate;

    @Schema(description = "등록 일자")
    private LocalDateTime registrationDate;

    @Schema(description = "등록 자")
    private String registrationId;

}

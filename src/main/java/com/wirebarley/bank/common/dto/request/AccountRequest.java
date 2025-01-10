package com.wirebarley.bank.common.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Comment;


@Schema(description = "계좌 정보 DTO")
@Data
@Builder
public class AccountRequest {

    @Schema(description = "계좌 비밀 번호", required = true, example = "1111")
    private String accountPassword;
    
    @Schema(description = "은행 코드", required = true, example = "CMMBAK001")
    private String bankCode;
    
    @Schema(description = "상품 코드", required = true, example = "CMMPRD001")
    private String productCode;

    @Schema(description = "사용자 ID", required = true, example = "user")
    private String userId;
}

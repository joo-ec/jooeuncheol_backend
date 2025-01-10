package com.wirebarley.bank.common.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Comment;


@Schema(description = "사용자 정보 DTO")
@Data
@Builder
public class LoginDTO {
    @Schema(description = "순번")
    private long no;
    @Schema(description = "사용자 ID")
    private String userId;
    @Schema(description = "이름")
    private String name;
    @Schema(description = "주소")
    private String address;
    @Schema(description = "핸드폰 번호")
    private String telNo;
    @Schema(description = "생년월일")
    private String brithDate;
    @Schema(description = "사용 유무")
    private String useStatus;
    @Schema(description = "권한 코드")
    private String authorizationCode;
}

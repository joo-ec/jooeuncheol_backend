package com.wirebarley.bank.common.dto;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO {

    private String userId;

    private String name;

    private String address;

    private String telNo;

    private String brithDate;

    private String useStatus;

    private String authorizationCode;

}

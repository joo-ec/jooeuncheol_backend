package com.wirebarley.bank.common.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("순번")
    @Column(name = "NO")
    private long NO;

    @Comment("상품 코드")
    @Column(name = "PRODUCT_CODE", unique = true, length = 10)
    private String productCode;

    @Comment("상품 명")
    @Column(name = "NAME", length = 100)
    private String name;

    @Comment("상품 설명")
    @Column(name = "DESCRIPTION", length = 100)
    private String description;

    @Comment("상품 출금 한도")
    @Column(name = "WITHDRAWAL_BOUNDS")
    private long withdrawalBounds;

    @Comment("상품 이체 한도")
    @Column(name = "TRANSFER_BOUNDS")
    private long transferBounds;

    @Comment("상품 금리")
    @Column(name = "INTEREST_RATE")
    private Double interestRate;

    @Comment("가입 기간")
    @Column(name = "JOIN_PERIOD")
    private int joinPeriod;

    @Comment("가입 대상")
    @Column(name = "JOIN_TARGET", length = 10)
    private String joinTarget;

    @Comment("사용 여부")
    @Column(name = "USE_STATUS", length = 10)
    private String useStatus;

    @Comment("등록 일자")
    @CreationTimestamp
    @Column(name = "REGISTRATION_DATE")
    private LocalDateTime registrationDate;

    @Comment("등록 자")
    @Column(name = "REGISTRATION_ID", length = 50)
    private String registrationId;

    @Comment("수정 일자")
    @UpdateTimestamp
    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;

    @Comment("수정 자")
    @Column(name = "UPDATE_ID", length = 50)
    private String updateId;

}

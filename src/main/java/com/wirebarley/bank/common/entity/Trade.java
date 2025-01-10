package com.wirebarley.bank.common.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TRADE")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("순번")
    @Column(name = "NO")
    private long NO;

    @Comment("은행 코드")
    @Column(name = "BANK_CODE", length = 10)
    private String bankCode;

    @Comment("계좌 번호")
    @Column(name = "ACCOUNT_NUMBER", length = 12)
    private String accountNumber;

    @Comment("타 계좌 번호")
    @Column(name = "TARGET_ACCOUNT_NUMBER", length = 12)
    private String targetAccountNumber;

    @Comment("타 은행 코드")
    @Column(name = "TARGET_BANK_CODE", length = 10)
    private String targetBankCode;

    @Comment("거래 구분")
    @Column(name = "TRADE_TYPE", length = 10)
    private String tradeType;

    @Comment("거래 결과")
    @Column(name = "TRADE_RESULT", length = 10)
    private String tradeResult;

    @Comment("금액")
    @Column(name = "AMOUNT")
    private long amount;

    @Comment("수수료")
    @Column(name = "FEE")
    private long fee;

    @Comment("잔고")
    @Column(name = "BALANCE")
    private long balance;

    @Comment("등록 일자")
    @CreationTimestamp
    @Column(name = "REGISTRATION_DATE")
    private LocalDateTime registrationDate;

    @Comment("등록 자")
    @Column(name = "REGISTRATION_ID", length = 50)
    private String registrationId;

}

package com.wirebarley.bank.common.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "ACCOUNT")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("순번")
    @Column(name = "NO")
    private long NO;

    @Comment("계좌 번호")
    @Column(name = "ACCOUNT_NUMBER", length = 12)
    private String accountNumber;

    @Comment("계좌 비밀번호")
    @Column(name = "ACCOUNT_PASSWORD", length = 100)
    private String accountPassword;

    @Comment("사용자 ID")
    @Column(name = "USER_ID", length = 50)
    private String userId;

    @Comment("은행 코드")
    @Column(name = "BANK_CODE", length = 10)
    private String bankCode;

    @Comment("상품 코드")
    @Column(name = "PRODUCT_CODE", length = 10)
    private String productCode;

    @Comment("승인 여부")
    @Column(name = "APPROVAL_STATUS", length = 10)
    private String approvalStatus;

    @Comment("반려 사유")
    @Column(name = "REJECT_REASON", length = 500)
    private String rejectReason;

    @Comment("사용 유무")
    @Column(name = "USE_STATUS", length = 10)
    private String useStatus;

    @Comment("잔고")
    @Column(name = "BALANCE")
    private long balance;

    @Comment("출금 한도")
    @Column(name = "WITHDRAWAL_BOUNDS")
    private long withdrawalBounds;

    @Comment("이체 한도")
    @Column(name = "TRANSFER_BOUNDS")
    private long transferBounds;

    @Comment("금리")
    @Column(name = "INTEREST_RATE")
    private Double interestRate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BANK_CODE", referencedColumnName = "CODE", insertable = false, updatable = false)
    private CommonCode joinBankCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_CODE", referencedColumnName = "PRODUCT_CODE", insertable = false, updatable = false)
    private Product joinProductCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPROVAL_STATUS", referencedColumnName = "CODE", insertable = false, updatable = false)
    private CommonCode joinApprovalStatusCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USE_STATUS", referencedColumnName = "CODE", insertable = false, updatable = false)
    private CommonCode joinUseStatus;

}

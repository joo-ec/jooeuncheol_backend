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
@Table(name = "COMMON_CODE")
public class CommonCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("순번")
    @Column(name = "NO")
    private long NO;

    @Comment("코드")
    @Column(name = "CODE", unique = true, length = 10)
    private String code;

    @Comment("코드 명")
    @Column(name = "CODE_NAME", length = 100)
    private String codeName;

    @Comment("상위 코드")
    @Column(name = "PARENT_CODE", length = 10)
    private String parentCode;

    @Comment("레벨")
    @Column(name = "LEVEL")
    private int level;

    @Comment("사용 유무")
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

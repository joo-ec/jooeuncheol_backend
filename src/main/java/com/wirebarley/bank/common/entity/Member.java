package com.wirebarley.bank.common.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MEMBER")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("순번")
    @Column(name = "NO")
    private long no;

    @Comment("사용자 ID")
    @Column(name = "USER_ID", length = 50)
    private String userId;

    @Comment("비밀번호")
    @Column(name = "PASSWORD", length = 100)
    private String password;

    @Comment("이름")
    @Column(name = "NAME", length = 50)
    private String name;

    @Comment("주소")
    @Column(name = "ADDRESS", length = 100)
    private String address;

    @Comment("핸드폰 번호")
    @Column(name = "TEL_NO", length = 100)
    private String telNo;

    @Comment("생년월일")
    @Column(name = "BRITH_DATE", length = 8)
    private String brithDate;

    @Comment("사용 유무")
    @Column(name = "USE_STATUS", length = 10)
    private String useStatus;

    @Comment("권한 코드")
    @Column(name = "AUTHORIZATION_CODE", length = 10)
    private String authorizationCode;


}

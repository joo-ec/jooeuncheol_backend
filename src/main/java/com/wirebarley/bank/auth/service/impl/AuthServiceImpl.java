package com.wirebarley.bank.auth.service.impl;

import com.wirebarley.bank.auth.service.AuthService;
import com.wirebarley.bank.common.dto.request.AuthRequest;
import com.wirebarley.bank.common.dto.request.JoinRequest;
import com.wirebarley.bank.common.entity.Member;
import com.wirebarley.bank.common.repository.AuthRepository;
import com.wirebarley.bank.common.type.ResponseCode;
import com.wirebarley.bank.common.type.UseStatus;
import com.wirebarley.bank.common.utils.BankScrtyUtil;
import com.wirebarley.core.exception.BizException;
import com.wirebarley.core.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService  {

    private final AuthRepository authRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    @Override
    public String login(AuthRequest authRequest) {

        Member member = authRepository.findByUserId(authRequest.getUserId());

        if( member == null ) {
            throw new BizException(ResponseCode.NOT_FOUND_MEMBER);
        }

        if( !encoder.matches(authRequest.getPassword(), member.getPassword())) {
            throw new BizException(ResponseCode.INVALID_MEMBER);
        }

        return jwtUtil.generateToken(member);
    }

    @Override
    public void join(JoinRequest joinRequest) {
        Member member = Member.builder()
                .userId(joinRequest.getUserId())
                .password(encoder.encode(joinRequest.getPassword()))
                .name(joinRequest.getName())
                .address(joinRequest.getAddress())
                .telNo(BankScrtyUtil.encodingToString(joinRequest.getTelNo()))
                .brithDate(BankScrtyUtil.encodingToString(joinRequest.getBrithDate()))
                .useStatus(UseStatus.USE.getCode())
                .authorizationCode("USER")
                .build();

        authRepository.save(member);
    }
}

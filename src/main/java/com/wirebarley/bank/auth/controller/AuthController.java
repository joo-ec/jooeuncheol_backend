package com.wirebarley.bank.auth.controller;


import com.wirebarley.bank.auth.service.AuthService;
import com.wirebarley.bank.common.dto.ApiDTO;
import com.wirebarley.bank.common.dto.request.AuthRequest;
import com.wirebarley.bank.common.dto.request.JoinRequest;
import com.wirebarley.bank.common.dto.response.LoginDTO;
import com.wirebarley.bank.common.type.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "1. 사용자 가입 및 로그인 API", description = "계좌 서비스를 사용하기 위해 회원 가입 및 로그인 기능을 제공한다.")
@Slf4j
@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "1-1. 회원 가입", description = "사용자의 정보를 받아 회원 가입 한다.", security = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    @PostMapping("join")
    public ApiDTO<Void> join(@RequestBody JoinRequest joinRequest ) {

        authService.join(joinRequest);

        return ApiDTO.<Void>builder()
                .rtCode(ResponseCode.SUCCESS.getCode())
                .build();
    }

    @Operation(summary = "1-2. 로그인 요청", description = "사용자의 ID와 비밀번호를 통해 로그인 한다.", security = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "1001", description = "사용자 정보가 없음"),
            @ApiResponse(responseCode = "1002", description = "인가된 사용자가 아님")
    })
    @PostMapping("login-jwt")
    public ApiDTO<Void> login(@RequestBody AuthRequest authRequest ) {
        String token = authService.login(authRequest);
        return ApiDTO.<Void>builder()
                .rtCode(ResponseCode.SUCCESS.getCode())
                .token(token)
                .build();
    }



}

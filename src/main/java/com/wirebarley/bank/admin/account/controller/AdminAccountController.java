package com.wirebarley.bank.admin.account.controller;


import com.wirebarley.bank.admin.account.service.AdminAccountService;
import com.wirebarley.bank.common.dto.ApiDTO;
import com.wirebarley.bank.common.dto.LoginVO;
import com.wirebarley.bank.common.dto.request.AccountVerifyRequest;
import com.wirebarley.bank.common.dto.request.ApprovalAccountRequest;
import com.wirebarley.bank.common.type.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "4. 관리자 계좌 관리 API", description = "사용자 계정 관리 기능을 제공한다.")
@Slf4j
@RestController
@RequestMapping("/api/admin/")
@AllArgsConstructor
public class AdminAccountController {

    private final AdminAccountService accountService;

    @Operation(summary = "4-1. 계좌 목록 조회", description = "사용자 계좌 승인 기능을 제공 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @PutMapping("{accountNumber}")
    public ApiDTO<Boolean> approvalAccount(@Parameter(description = "계좌 번호", required = true) @PathVariable("accountNumber") String accountNumber,
                                           @RequestBody ApprovalAccountRequest accountRequest, @Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO) {

        accountRequest.setUserId(loginVO.getUserId());
        accountRequest.setAccountNumber(accountNumber);

        accountService.approvalAccount(accountRequest);

        return ApiDTO.<Boolean>builder()
                .rtCode(ResponseCode.SUCCESS.getCode())
                .data(true)
                .build();
    }


}

package com.wirebarley.bank.account.controller;


import com.wirebarley.bank.account.service.AccountService;
import com.wirebarley.bank.common.dto.ApiDTO;
import com.wirebarley.bank.common.dto.LoginVO;
import com.wirebarley.bank.common.dto.request.AccountRequest;
import com.wirebarley.bank.common.dto.request.AccountVerifyRequest;
import com.wirebarley.bank.common.dto.response.AccountDTO;
import com.wirebarley.bank.common.entity.Account;
import com.wirebarley.bank.common.type.ResponseCode;
import com.wirebarley.bank.common.utils.JsonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "2. 계좌 관리 API", description = "로그인한 사용자 계좌 목록, 계좌 상세, 계좌 등록, 계좌 삭제 기능 제공")
@Slf4j
@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "1-1. 계좌 목록 조회", description = "로그인한 사용자의 등록된 계좌 목록을 조회 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @GetMapping("")
    public ApiDTO<List<AccountDTO>> getAccounts(@Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO) {

        List<Account> account = accountService.getAccounts(loginVO.getUserId());

        List<AccountDTO> accountDTO = account.stream().map( vo -> {
            AccountDTO item = JsonUtils.convert(vo, AccountDTO.class);
            item.setBankCodeName(vo.getJoinBankCode().getCodeName());
            item.setProductCodeName(vo.getJoinProductCode().getProductCode());
            item.setApprovalStatusName(vo.getJoinApprovalStatusCode().getCodeName());
            item.setUseStatusName(vo.getJoinUseStatus().getCodeName());
            return item;
        }).collect(Collectors.toList());


        return ApiDTO.<List<AccountDTO>>builder()
                .rtCode(ResponseCode.SUCCESS.getCode())
                .data(accountDTO)
                .build();
    }

    @Operation(summary = "1-2. 계좌 등록", description = "계좌 정보를 입력 받아 계좌를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "3000", description = "은행 정보 없음", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3200", description = "상품 정보 없음", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
    })
    @PostMapping("")
    public ApiDTO<AccountDTO> saveAccount(@RequestBody AccountRequest accountRequest, @Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO) {

        accountRequest.setUserId(loginVO.getUserId());

        Account account = accountService.save(accountRequest);

        return ApiDTO.<AccountDTO>builder()
                .rtCode(ResponseCode.SUCCESS.getCode())
                .data(JsonUtils.convert(account, AccountDTO.class))
                .build();
    }

    @Operation(summary = "1-3. 계좌 정보 조회", description = "로그인한 사용자의 계좌 정보를 조회 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "3100", description = "계좌 정보 없음", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3101", description = "계좌 비밀번호 오류", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3102", description = "계좌 접근 권한 없음", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
    })
    @PostMapping("/{accountNumber}")
    public ApiDTO<AccountDTO> getAccount(@Parameter(description = "계좌 번호", required = true) @PathVariable("accountNumber") String accountNumber, @RequestBody AccountVerifyRequest accountRequest, @Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO) {

        accountRequest.setUserId(loginVO.getUserId());
        accountRequest.setAccountNumber(accountNumber);

        Account account = accountService.getAccount(accountRequest);
        AccountDTO accountDTO = JsonUtils.convert(account, AccountDTO.class);

        accountDTO.setBankCodeName(account.getJoinBankCode().getCodeName());
        accountDTO.setProductCodeName(account.getJoinProductCode().getProductCode());
        accountDTO.setApprovalStatusName(account.getJoinApprovalStatusCode().getCodeName());
        accountDTO.setUseStatusName(account.getJoinUseStatus().getCodeName());

        return ApiDTO.<AccountDTO>builder()
                .rtCode(ResponseCode.SUCCESS.getCode())
                .data(accountDTO)
                .build();
    }

    @Operation(summary = "1-4. 계좌 삭제", description = "등록된 계좌를 삭제 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "3100", description = "계좌 정보 없음", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3101", description = "계좌 비밀번호 오류", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3102", description = "계좌 접근 권한 없음", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3103", description = "해지된 계좌 정보", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3104", description = "미승인 계좌 정보", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
    })
    @DeleteMapping("/{accountNumber}")
    public ApiDTO<Boolean> deleteAccount(@Parameter(description = "계좌 번호", required = true) @PathVariable("accountNumber") String accountNumber, @RequestBody AccountVerifyRequest accountRequest, @Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO) {

        accountRequest.setUserId(loginVO.getUserId());
        accountRequest.setAccountNumber(accountNumber);

        accountService.deleteAccount(accountRequest);

        return ApiDTO.<Boolean>builder()
                .rtCode(ResponseCode.SUCCESS.getCode())
                .data(true)
                .build();
    }



}

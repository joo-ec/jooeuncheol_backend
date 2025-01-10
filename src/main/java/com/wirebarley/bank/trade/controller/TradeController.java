package com.wirebarley.bank.trade.controller;


import com.wirebarley.bank.common.dto.ApiDTO;
import com.wirebarley.bank.common.dto.LoginVO;
import com.wirebarley.bank.common.dto.request.DepositRequest;
import com.wirebarley.bank.common.dto.request.TransferDetailsRequest;
import com.wirebarley.bank.common.dto.request.TransferRequest;
import com.wirebarley.bank.common.dto.request.WithdrawalRequest;
import com.wirebarley.bank.common.dto.response.BalanceDTO;
import com.wirebarley.bank.common.dto.response.TradeDetailsDTO;
import com.wirebarley.bank.common.type.ResponseCode;
import com.wirebarley.bank.trade.service.TradeService;
import com.wirebarley.core.exception.BizException;
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

@Tag(name = "3. 거래 관리 API", description = "계좌의 거래 관리 API를 제공한다.")
@Slf4j
@RestController
@RequestMapping("/api/trade/")
@AllArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @Operation(summary = "3-1. 계좌 내역 조회", description = "사용자의 계좌의 거래 내역을 조회 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "3100", description = "계좌 정보 없음", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3101", description = "계좌 비밀번호 오류", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3102", description = "계좌 접근 권한 없음", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3103", description = "해지된 계좌 정보", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3104", description = "미승인 계좌 정보", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
    })
    @GetMapping("{accountNumber}")
    public ApiDTO<List<TradeDetailsDTO>> getTransferList(@Parameter(description = "계좌 번호", required = true) @PathVariable("accountNumber") String accountNumber, TransferDetailsRequest detailsRequest, @Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO) {
        detailsRequest.setAccountNumber(accountNumber);
        detailsRequest.setUserId(loginVO.getUserId());

        log.info("detailsRequest => {} ", detailsRequest);

        List<TradeDetailsDTO> data = tradeService.getTransferList(detailsRequest);


        return ApiDTO.<List<TradeDetailsDTO>>builder()
                .rtCode(ResponseCode.SUCCESS.getCode())
                .data(data)
                .build();
    }
    
    @Operation(summary = "3-2. 계좌 입금", description = "사용자 계좌에 입금 기능을 제공 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "3100", description = "계좌 정보 없음", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3101", description = "계좌 비밀번호 오류", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3102", description = "계좌 접근 권한 없음", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3103", description = "해지된 계좌 정보", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3104", description = "미승인 계좌 정보", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3300", description = "거래 금액 오류", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
    })
    @PutMapping("{accountNumber}")
    public ApiDTO<BalanceDTO> depositAccount(@Parameter(description = "계좌 번호", required = true) @PathVariable("accountNumber") String accountNumber, @RequestBody DepositRequest depositRequest, @Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO) {
        BalanceDTO balanceDTO = null;
        int rtCode = ResponseCode.SUCCESS.getCode();
        depositRequest.setAccountNumber(accountNumber);
        depositRequest.setUserId(loginVO.getUserId());

        try{
            balanceDTO = tradeService.depositAccount(depositRequest);
        } catch ( BizException be ) {
            balanceDTO = tradeService.processException(depositRequest, be.getErrCode());
            rtCode = be.getErrCode();
        }

        return ApiDTO.<BalanceDTO>builder()
                .rtCode(rtCode)
                .data(balanceDTO)
                .build();
    }
    
    @Operation(summary = "3-3. 계좌 이체", description = "사용자의 계좌에서 타 사용자 계좌에 거래 금액을 이체 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "3100", description = "계좌 정보 없음", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3101", description = "계좌 비밀번호 오류", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3102", description = "계좌 접근 권한 없음", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3103", description = "해지된 계좌 정보", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3104", description = "미승인 계좌 정보", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3300", description = "거래 금액 오류", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3301", description = "거래 금액 부족", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3302", description = "거래 한도 초과", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
    })
    @PutMapping("{accountNumber}/transfer")
    public ApiDTO<BalanceDTO> transferAccount(@Parameter(description = "계좌 번호", required = true) @PathVariable("accountNumber") String accountNumber, @RequestBody TransferRequest transferRequest, @Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO) {
        BalanceDTO balanceDTO = null;
        int rtCode = ResponseCode.SUCCESS.getCode();

        transferRequest.setAccountNumber(accountNumber);
        transferRequest.setUserId(loginVO.getUserId());

        try{
            balanceDTO = tradeService.transferAccount(transferRequest);
        } catch ( BizException be ) {
            rtCode = be.getErrCode();
        }
        return ApiDTO.<BalanceDTO>builder()
                .rtCode(rtCode)
                .data(balanceDTO)
                .build();
    }
    
    @Operation(summary = "3-4. 계좌 출금", description = "사용자 계좌에 출금 기능을 제공 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "3100", description = "계좌 정보 없음", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3101", description = "계좌 비밀번호 오류", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3102", description = "계좌 접근 권한 없음", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3103", description = "해지된 계좌 정보", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3104", description = "미승인 계좌 정보", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3300", description = "거래 금액 오류", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3301", description = "거래 금액 부족", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
            @ApiResponse(responseCode = "3302", description = "거래 한도 초과", content = @Content(schema=@Schema(implementation = ApiDTO.class))),
    })
    @PutMapping("{accountNumber}/withdrawal")
    public ApiDTO<BalanceDTO> withdrawalAccount(@Parameter(description = "계좌 번호", required = true) @PathVariable("accountNumber") String accountNumber, @RequestBody WithdrawalRequest withdrawalRequest, @Parameter(hidden = true) @AuthenticationPrincipal LoginVO loginVO) {
        BalanceDTO balanceDTO = null;
        int rtCode = ResponseCode.SUCCESS.getCode();

        withdrawalRequest.setAccountNumber(accountNumber);
        withdrawalRequest.setUserId(loginVO.getUserId());

        try{
            balanceDTO = tradeService.withdrawalAccount(withdrawalRequest);
        } catch ( BizException be ) {
            rtCode = be.getErrCode();
        }

        return ApiDTO.<BalanceDTO>builder()
                .rtCode(rtCode)
                .data(balanceDTO)
                .build();
    }

    

}


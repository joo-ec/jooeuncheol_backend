package com.wirebarley.bank.integrate.trade;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wirebarley.bank.account.service.AccountService;
import com.wirebarley.bank.account.service.impl.AccountServiceImpl;
import com.wirebarley.bank.common.code.service.impl.CommonCodeServiceImpl;
import com.wirebarley.bank.common.dto.ApiDTO;
import com.wirebarley.bank.common.dto.request.*;
import com.wirebarley.bank.common.dto.response.AccountDTO;
import com.wirebarley.bank.common.dto.response.BalanceDTO;
import com.wirebarley.bank.common.entity.Account;
import com.wirebarley.bank.common.entity.Trade;
import com.wirebarley.bank.common.repository.AccountRepository;
import com.wirebarley.bank.common.repository.CommonCodeRepository;
import com.wirebarley.bank.common.repository.ProductRepository;
import com.wirebarley.bank.common.repository.TradeRepository;
import com.wirebarley.bank.common.type.Approval;
import com.wirebarley.bank.common.type.ResponseCode;
import com.wirebarley.bank.common.type.TradeType;
import com.wirebarley.bank.common.type.UseStatus;
import com.wirebarley.bank.common.utils.JsonUtils;
import com.wirebarley.bank.product.service.impl.ProductServiceImpl;
import com.wirebarley.bank.trade.service.TradeService;
import com.wirebarley.bank.trade.service.impl.TradeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TradeIntegrateTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TradeService tradeService;

    private final String URI = "/api/trade";

    private final String JWT_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJhZGRyZXNzIjoi7ISc7Jq4IOq4iOyynOq1rCDrlJTsp4DthLjroZw56ri4IDY4IiwiYXV0aG9yaXphdGlvbkNvZGUiOiJVU0VSIiwibmFtZSI6IuyCrOyaqeyekCIsImJyaXRoRGF0ZSI6IjIwMjUwMTAxIiwidHlwZSI6IkF1dGhvcml6YXRpb24iLCJ1c2VySWQiOiJ1c2VyIiwidGVsTm8iOiIwMDAwMDAwMDAwMCIsInVzZVN0YXR1cyI6IkNNTVVTRTAwMSIsInN1YiI6IkF1dGhvcml6YXRpb24iLCJpYXQiOjE3MzYzODY3NTUsImV4cCI6MTczNzcwMDc1NX0.pRKVrRmapP8BYbE_9Z0SK4jYvSaFQQ2UCUZPzRVdLlovV5yoEhnMTlZMRhXFTjq8ewyHXy2Fsq19mU8YyVtQlw";
    private final String JWT_TOKEN2 = "eyJhbGciOiJIUzUxMiJ9.eyJhZGRyZXNzIjoi7ISc7Jq4IOq4iOyynOq1rCDrlJTsp4DthLjroZw56ri4IDY4IiwiYXV0aG9yaXphdGlvbkNvZGUiOiJBRE1JTiIsIm5hbWUiOiLqtIDrpqzsnpAiLCJicml0aERhdGUiOiIyMDI1MDEwMiIsInR5cGUiOiJBdXRob3JpemF0aW9uIiwidXNlcklkIjoiYWRtaW4iLCJ0ZWxObyI6IjAxMDEyMzQxMjM0IiwidXNlU3RhdHVzIjoiQ01NVVNFMDAxIiwic3ViIjoiQXV0aG9yaXphdGlvbiIsImlhdCI6MTczNjQyNzE3OSwiZXhwIjoxNzM3NzQxMTc5fQ.Gi3TF0Te2jFcxTBf5QAVNFcKfxDQaUXnoXKt5xH9w7gaBiwRaVP3LGGePkYBHQMIVoncPaMVxyo9bMJq2brkDA";

    private String test_AccountNumber() throws Exception {

        AccountRequest accountRequest = AccountRequest.builder()
                .bankCode("CMMBAK001")
                .productCode("CMMPRD003")
                .accountPassword("1234")
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/account")
                .header("Authorization", JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest))
        );

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        ApiDTO<AccountDTO> response = objectMapper.readValue(responseBody, new TypeReference<>() {});

        return response.getData().getAccountNumber();
    }
    private void test_AccountApproval(String accountNumber) throws Exception {
        ApprovalAccountRequest accountRequest = ApprovalAccountRequest.builder().bankCode("CMMBAK001").build();

        ResultActions resultActions = mockMvc.perform(put("/api/admin/"+accountNumber)
                .header("Authorization", JWT_TOKEN2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest))
        ).andDo(print());
    }

    private String test_getAccountNumber() throws Exception {
        String accountNumber = test_AccountNumber();
        test_AccountApproval(accountNumber);
        return accountNumber;
    }

    private void test_setDepositAccount(String accountNumber) {
        DepositRequest depositRequest = DepositRequest.builder()
                .accountNumber(accountNumber)
                .bankCode("CMMBAK001")
                .amount(50000)
                .userId("user")
                .build();
        tradeService.depositAccount(depositRequest);
    }

    @DisplayName("계좌 입금")
    @Test
    void depositAccount() throws Exception {
        String accountNumber = test_getAccountNumber();

        DepositRequest depositRequest = DepositRequest.builder()
                .accountNumber(accountNumber)
                .bankCode("CMMBAK001")
                .amount(30000)
                .userId("user")
                .build();

        ResultActions resultActions = mockMvc.perform(put(URI+"/"+accountNumber )
                .header("Authorization", JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositRequest))
        ).andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").isNotEmpty());

    }

    @DisplayName("계좌 입금 - 거래 금액 오류")
    @Test
    void depositAccount_invalid_amount() throws Exception {
        String accountNumber = test_getAccountNumber();

        DepositRequest depositRequest = DepositRequest.builder()
                .accountNumber(accountNumber)
                .bankCode("CMMBAK001")
                .amount(-9999)
                .userId("user")
                .build();

        ResultActions resultActions = mockMvc.perform(put(URI+"/"+accountNumber )
                .header("Authorization", JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositRequest))
        ).andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.INVALID_AMOUNT_TRADE.getCode()));

    }

    @DisplayName("계좌 이체")
    @Test
    void transferAccount() throws Exception {
        String accountNumber = "001820009681";
        TransferRequest transferRequest = TransferRequest.builder()
                .accountNumber(accountNumber)
                .targetAccountNumber("001052307616")
                .bankCode("CMMBAK001")
                .targetBankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(10000)
                .build();


        // 실행
        mockMvc.perform(put("/api/trade/{accountNumber}/transfer", accountNumber)
                        .header("Authorization", JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.accountNumber").value(accountNumber))
                .andDo(print());
    }

    @DisplayName("계좌 이체 - 거래 금액 오류")
    @Test
    void transferAccount_invalid_amount() throws Exception {

        String accountNumber = "001820009681";
        TransferRequest transferRequest = TransferRequest.builder()
                .accountNumber(accountNumber)
                .targetAccountNumber("001052307616")
                .bankCode("CMMBAK001")
                .targetBankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(0)
                .build();


        // 실행
        mockMvc.perform(put("/api/trade/{accountNumber}/transfer", accountNumber)
                        .header("Authorization", JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.INVALID_AMOUNT_TRADE.getCode()))
                .andDo(print());
    }

    @DisplayName("계좌 이체 - 거래 금액 부족")
    @Test
    void transferAccount_invalid_balance() throws Exception {

        String accountNumber = "001820009681";
        TransferRequest transferRequest = TransferRequest.builder()
                .accountNumber(accountNumber)
                .targetAccountNumber("001052307616")
                .bankCode("CMMBAK001")
                .targetBankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(994890)
                .build();


        // 실행
        mockMvc.perform(put("/api/trade/{accountNumber}/transfer", accountNumber)
                        .header("Authorization", JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.INVALID_WITHDRAWAL_AMOUNT_TRADE.getCode()))
                .andDo(print());
    }

    @DisplayName("계좌 출금 - 거래 한도 초과")
    @Test
    void transferAccount_invalid_bounds() throws Exception {
        String accountNumber = "002517613820";

        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber(accountNumber)
                .bankCode("CMMBAK002")
                .amount(10000000)
                .accountPassword("1111")
                .build();

        ResultActions resultActions = mockMvc.perform(put("/api/trade/{accountNumber}/transfer",accountNumber )
                .header("Authorization", JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withdrawalRequest))
        ).andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.OVER_BOUNDS_TRADE.getCode()));
    }
    
    @DisplayName("계좌 출금")
    @Test
    void withdrawalAccount() throws Exception {
        String accountNumber = "001820009681";

        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber(accountNumber)
                .bankCode("CMMBAK001")
                .amount(10)
                .accountPassword("1111")
                .build();

        ResultActions resultActions = mockMvc.perform(put("/api/trade/{accountNumber}/withdrawal",accountNumber )
                .header("Authorization", JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withdrawalRequest))
        ).andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.SUCCESS.getCode()));

    }

    @DisplayName("계좌 출금 - 거래 금액 오류")
    @Test
    void withdrawalAccount_invalid_amount() throws Exception {
        String accountNumber = test_getAccountNumber();

        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber(accountNumber)
                .bankCode("CMMBAK001")
                .amount(0)
                .userId("user")
                .build();

        ResultActions resultActions = mockMvc.perform(put("/api/trade/{accountNumber}/withdrawal",accountNumber )
                .header("Authorization", JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withdrawalRequest))
        ).andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.INVALID_AMOUNT_TRADE.getCode()));
    }

    @DisplayName("계좌 출금 - 거래 금액 부족")
    @Test
    void withdrawalAccount_invalid_balance() throws Exception {
        String accountNumber = "001820009681";

        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber(accountNumber)
                .bankCode("CMMBAK001")
                .amount(10000000)
                .accountPassword("1111")
                .build();

        ResultActions resultActions = mockMvc.perform(put("/api/trade/{accountNumber}/withdrawal",accountNumber )
                .header("Authorization", JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withdrawalRequest))
        ).andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.INVALID_WITHDRAWAL_AMOUNT_TRADE.getCode()));
    }

    @DisplayName("계좌 출금 - 거래 한도 초과")
    @Test
    void withdrawalAccount_invalid_bounds() throws Exception {
        String accountNumber = "002517613820";

        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber(accountNumber)
                .bankCode("CMMBAK002")
                .amount(10000000)
                .accountPassword("1111")
                .build();

        ResultActions resultActions = mockMvc.perform(put("/api/trade/{accountNumber}/withdrawal",accountNumber )
                .header("Authorization", JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withdrawalRequest))
        ).andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.OVER_BOUNDS_TRADE.getCode()));
    }


    
}

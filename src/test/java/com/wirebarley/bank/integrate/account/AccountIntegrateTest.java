package com.wirebarley.bank.integrate.account;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wirebarley.bank.account.service.AccountService;
import com.wirebarley.bank.common.dto.ApiDTO;
import com.wirebarley.bank.common.dto.request.AccountRequest;
import com.wirebarley.bank.common.dto.request.AccountVerifyRequest;
import com.wirebarley.bank.common.dto.request.ApprovalAccountRequest;
import com.wirebarley.bank.common.dto.response.AccountDTO;
import com.wirebarley.bank.common.type.ResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AccountIntegrateTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AccountService accountService;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();


    private final String URI = "/api/account";

    private final String JWT_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJhZGRyZXNzIjoi7ISc7Jq4IOq4iOyynOq1rCDrlJTsp4DthLjroZw56ri4IDY4IiwiYXV0aG9yaXphdGlvbkNvZGUiOiJVU0VSIiwibmFtZSI6IuyCrOyaqeyekCIsImJyaXRoRGF0ZSI6IjIwMjUwMTAxIiwidHlwZSI6IkF1dGhvcml6YXRpb24iLCJ1c2VySWQiOiJ1c2VyIiwidGVsTm8iOiIwMDAwMDAwMDAwMCIsInVzZVN0YXR1cyI6IkNNTVVTRTAwMSIsInN1YiI6IkF1dGhvcml6YXRpb24iLCJpYXQiOjE3MzYzODY3NTUsImV4cCI6MTczNzcwMDc1NX0.pRKVrRmapP8BYbE_9Z0SK4jYvSaFQQ2UCUZPzRVdLlovV5yoEhnMTlZMRhXFTjq8ewyHXy2Fsq19mU8YyVtQlw";
    private final String JWT_TOKEN2 = "eyJhbGciOiJIUzUxMiJ9.eyJhZGRyZXNzIjoi7ISc7Jq4IOq4iOyynOq1rCDrlJTsp4DthLjroZw56ri4IDY4IiwiYXV0aG9yaXphdGlvbkNvZGUiOiJBRE1JTiIsIm5hbWUiOiLqtIDrpqzsnpAiLCJicml0aERhdGUiOiIyMDI1MDEwMiIsInR5cGUiOiJBdXRob3JpemF0aW9uIiwidXNlcklkIjoiYWRtaW4iLCJ0ZWxObyI6IjAxMDEyMzQxMjM0IiwidXNlU3RhdHVzIjoiQ01NVVNFMDAxIiwic3ViIjoiQXV0aG9yaXphdGlvbiIsImlhdCI6MTczNjQyNzE3OSwiZXhwIjoxNzM3NzQxMTc5fQ.Gi3TF0Te2jFcxTBf5QAVNFcKfxDQaUXnoXKt5xH9w7gaBiwRaVP3LGGePkYBHQMIVoncPaMVxyo9bMJq2brkDA";

    List<AccountDTO> getMockAccount() {
        AccountDTO account1 = AccountDTO.builder()
                .accountNumber("001123456789")
                .userId("user")
                .bankCode("CMMBAK001")
                .bankCodeName("한국 은행")
                .productCode("CMMPRD003")
                .productCodeName("CMMPRD003")
                .approvalStatus("CMMAPR001")
                .approvalStatusName("승인")
                .rejectReason(null)
                .useStatus("CMMUSE001")
                .useStatusName("사용")
                .balance(5)
                .withdrawalBounds(1000000)
                .transferBounds(3000000)
                .interestRate(0.3)
                .registrationId("user")
                .build();

        AccountDTO account2 = AccountDTO.builder()
                .accountNumber("001987654321")
                .userId("user")
                .bankCode("CMMBAK001")
                .bankCodeName("한국 은행")
                .productCode("CMMPRD003")
                .productCodeName("CMMPRD003")
                .approvalStatus("CMMAPR001")
                .approvalStatusName("승인")
                .rejectReason(null)
                .useStatus("CMMUSE001")
                .useStatusName("사용")
                .balance(5)
                .withdrawalBounds(1000000)
                .transferBounds(3000000)
                .interestRate(0.3)
                .registrationId("user")
                .build();

        return Arrays.asList(account1, account2);

    }

    @DisplayName("계좌 목록 조회")
    @Test
    void getAccounts() throws Exception {

        ResultActions resultActions = mockMvc.perform(get(URI)
                        .header("Authorization", JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").isNotEmpty());

    }

    @DisplayName("계좌 목록 조회 - 조회 결과 없음")
    @Test
    void getAccounts_no_data() throws Exception {

        ResultActions resultActions = mockMvc.perform(get(URI)
                        .header("Authorization", JWT_TOKEN2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").isNotEmpty());

    }

    @DisplayName("계좌 등록")
    @Test
    void saveAccount() throws Exception {

        AccountRequest accountRequest = AccountRequest.builder()
                .bankCode("CMMBAK001")
                .productCode("CMMPRD003")
                .accountPassword("1122")
                .userId("user")
                .build();

        ResultActions resultActions = mockMvc.perform(post(URI)
                        .header("Authorization", JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest))
                ).andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").isNotEmpty());

    }

    @DisplayName("계좌 등록 - 은행 정보 없음")
    @Test
    void saveAccount_invalid_bank() throws Exception {

        AccountRequest accountRequest = AccountRequest.builder()
                .bankCode("CMMBAK00")
                .productCode("CMMPRD003")
                .accountPassword("1122")
                .userId("user")
                .build();

        ResultActions resultActions = mockMvc.perform(post(URI)
                        .header("Authorization", JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest))
                )
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.INVALID_BANK.getCode()));

    }

    @DisplayName("계좌 등록 - 상품 코드 없음")
    @Test
    void saveAccount_invalid_product() throws Exception {

        AccountRequest accountRequest = AccountRequest.builder()
                .bankCode("CMMBAK001")
                .productCode("CMMPRD00")
                .accountPassword("1122")
                .userId("user")
                .build();

        ResultActions resultActions = mockMvc.perform(post(URI)
                        .header("Authorization", JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest))
                )
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.INVALID_PRODUCT.getCode()));

    }

    @DisplayName("계좌 정보 조회")
    @Test
    void getAccount() throws Exception {

        AccountVerifyRequest accountRequest =
                AccountVerifyRequest.builder()
                        .bankCode("CMMBAK001")
                        .accountPassword("1111")
                        .userId("user")
                        .build();

        ResultActions resultActions = mockMvc.perform(post(URI+"/001052307616")
                        .header("Authorization", JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest))
                )
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @DisplayName("계좌 정보 조회 - 계좌 정보 없음")
    @Test
    void getAccount_not_fount() throws Exception {

        AccountVerifyRequest accountRequest =
                AccountVerifyRequest.builder()
                        .bankCode("CMMBAK001")
                        .accountPassword("1111")
                        .userId("user")
                        .build();

        ResultActions resultActions = mockMvc.perform(post(URI+"/0010523076161")
                        .header("Authorization", JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest))
                )
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.NOT_FOUND_ACCOUNT.getCode()));
    }

    @DisplayName("계좌 정보 조회 - 계좌 비밀번호 오류")
    @Test
    void getAccount_invalid_password() throws Exception {

        AccountVerifyRequest accountRequest =
                AccountVerifyRequest.builder()
                        .bankCode("CMMBAK001")
                        .accountPassword("11112")
                        .userId("user")
                        .build();

        ResultActions resultActions = mockMvc.perform(post(URI+"/001052307616")
                        .header("Authorization", JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest))
                )
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.INVALID_ACCOUNT.getCode()));
    }

    @DisplayName("계좌 정보 조회 - 계좌 접근 권한 없음")
    @Test
    void getAccount_no_access() throws Exception {

        AccountVerifyRequest accountRequest =
                AccountVerifyRequest.builder()
                        .bankCode("CMMBAK001")
                        .accountPassword("1111")
                        .build();

        ResultActions resultActions = mockMvc.perform(post(URI+"/001052307616")
                        .header("Authorization", JWT_TOKEN2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest))
                )
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.NO_ACCESS_ACCOUNT.getCode()));
    }

    private void test_AccountApproval(String accountNumber) throws Exception {
        ApprovalAccountRequest accountRequest = ApprovalAccountRequest.builder()
                .bankCode("CMMBAK001")
                .build();

        ResultActions resultActions = mockMvc.perform(put("/api/admin/"+accountNumber)
                .header("Authorization", JWT_TOKEN2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest))
        );
    }

    private String test_AccountNumber() throws Exception {

        AccountRequest accountRequest = AccountRequest.builder()
                .bankCode("CMMBAK001")
                .productCode("CMMPRD003")
                .accountPassword("1234")
                .userId("user")
                .build();

        ResultActions resultActions = mockMvc.perform(post(URI)
                .header("Authorization", JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest))
        );

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        ApiDTO<AccountDTO> response = objectMapper.readValue(responseBody, new TypeReference<>() {});

        return response.getData().getAccountNumber();
    }

    @DisplayName("계좌 삭제")
    @Test
    void deleteAccount() throws Exception {
        String accountNumber = test_AccountNumber();
        test_AccountApproval(accountNumber);

        AccountVerifyRequest accountRequest = AccountVerifyRequest.builder()
                .bankCode("CMMBAK001")
                .accountPassword("1234")
                .userId("user")
                .build();

        ResultActions resultActions = mockMvc.perform(delete(URI+"/"+accountNumber)
                        .header("Authorization", JWT_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest))
                )
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.rtCode").value(ResponseCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

    }

}

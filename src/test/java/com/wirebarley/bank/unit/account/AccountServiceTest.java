package com.wirebarley.bank.unit.account;

import com.wirebarley.bank.account.service.impl.AccountServiceImpl;
import com.wirebarley.bank.common.code.service.impl.CommonCodeServiceImpl;
import com.wirebarley.bank.common.dto.request.AccountRequest;
import com.wirebarley.bank.common.dto.request.AccountVerifyRequest;
import com.wirebarley.bank.common.entity.Account;
import com.wirebarley.bank.common.entity.CommonCode;
import com.wirebarley.bank.common.entity.Product;
import com.wirebarley.bank.common.repository.AccountRepository;
import com.wirebarley.bank.common.repository.CommonCodeRepository;
import com.wirebarley.bank.common.repository.ProductRepository;
import com.wirebarley.bank.common.type.*;
import com.wirebarley.bank.product.service.impl.ProductServiceImpl;
import com.wirebarley.core.exception.BizException;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {


    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final CommonCodeRepository commonCodeRepository = mock(CommonCodeRepository.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final AccountRepository accountRepository = mock(AccountRepository.class);

    private final CommonCodeServiceImpl commonCodeService = new CommonCodeServiceImpl(commonCodeRepository);
    private final ProductServiceImpl productService = new ProductServiceImpl(productRepository);
    private final AccountServiceImpl accountService = new AccountServiceImpl(
            productService,
            commonCodeService,
            accountRepository,
            encoder
    );

    List<Account> getMockAccount() {
        Account account1 = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .userId("user")
                .build();

        Account account2 = Account.builder()
                .accountNumber("001987654321")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1234"))
                .useStatus(UseStatus.NOT_USED.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .userId("user")
                .build();

        Account account3 = Account.builder()
                .accountNumber("001122334455")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("4321"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.NOT_APPROVED.getCode())
                .userId("user")
                .build();

        return Arrays.asList(account1, account2, account3);
    }

    @Test
    void test_getAccounts_success() {
        String userId = "user";

        List<Account> mockAccounts = getMockAccount();

        when(accountRepository.findByUserId(userId)).thenReturn(mockAccounts);

        List<Account> accounts = accountService.getAccounts("user");

        assertEquals(2, accounts.size());
        assertEquals("001123456789", accounts.get(0).getAccountNumber());
        assertEquals("001987654321", accounts.get(1).getAccountNumber());

        verify(accountRepository, times(1)).findByUserId(userId);

    }


    @Test
    void test_getAccounts_noData() {
        String userId = "user123";

        List<Account> mockAccounts = getMockAccount();
        when(accountRepository.findByUserId(userId)).thenReturn(mockAccounts);

        // When
        List<Account> accounts = accountService.getAccounts(userId);

        // Then
        assertEquals(0, accounts.size());
        verify(accountRepository, times(1)).findByUserId(userId);
    }


    @Test
    void test_getAccount_success() {
        AccountVerifyRequest accountVerifyRequest =
                AccountVerifyRequest.builder()
                        .accountNumber("001123456789")
                        .bankCode("CMMBAK001")
                        .accountPassword("1111")
                        .userId("user")
                        .build();

        List<Account> mockAccounts = getMockAccount();

        when( accountRepository.findByBankCodeAndAccountNumber( accountVerifyRequest.getBankCode()
                , accountVerifyRequest.getAccountNumber() ) ).thenReturn( mockAccounts.get(0) );

        Account account = accountService.getAccount(accountVerifyRequest);

        assertNotNull(account);
        assertEquals("001123456789",account.getAccountNumber());
        assertEquals("CMMBAK001",account.getBankCode());
        assertEquals(true,encoder.matches("1111", account.getAccountPassword()));
        assertEquals(UseStatus.USE.getCode(),account.getUseStatus());
        assertEquals(Approval.APPROVAL.getCode(),account.getApprovalStatus());
        assertEquals("user",account.getUserId());
        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
    }

    @Test
    void test_getAccount_not_found() {
        AccountVerifyRequest accountVerifyRequest = AccountVerifyRequest.builder()
                        .build();

        when( accountRepository.findByBankCodeAndAccountNumber( accountVerifyRequest.getBankCode()
                , accountVerifyRequest.getAccountNumber() ) ).thenReturn( null );

        BizException exception = assertThrows(BizException.class, () -> accountService.getAccount(accountVerifyRequest));
        assertEquals(ResponseCode.NOT_FOUND_ACCOUNT.getCode(), exception.getErrCode());
        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
    }

    @Test
    void test_getAccount_no_access() {
        AccountVerifyRequest accountVerifyRequest = AccountVerifyRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword("1111")
                .userId("user1")
                .build();

        List<Account> mockAccounts = getMockAccount();

        when( accountRepository.findByBankCodeAndAccountNumber( accountVerifyRequest.getBankCode()
                , accountVerifyRequest.getAccountNumber() ) ).thenReturn( mockAccounts.get(0) );

        BizException exception = assertThrows(BizException.class, () -> accountService.getAccount(accountVerifyRequest));
        assertEquals(ResponseCode.NO_ACCESS_ACCOUNT.getCode(), exception.getErrCode());
        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
    }

    @Test
    void test_getAccount_invalid_password() {
        AccountVerifyRequest accountVerifyRequest = AccountVerifyRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword("1112")
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        when( accountRepository.findByBankCodeAndAccountNumber( accountVerifyRequest.getBankCode()
                , accountVerifyRequest.getAccountNumber() ) ).thenReturn( mockAccounts.get(0) );

        BizException exception = assertThrows(BizException.class, () -> accountService.getAccount(accountVerifyRequest));
        assertEquals(ResponseCode.INVALID_ACCOUNT.getCode(), exception.getErrCode());
        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
    }


    @Test
    void test_getTradeAccount_success() {
        AccountVerifyRequest accountVerifyRequest =
                AccountVerifyRequest.builder()
                        .accountNumber("001123456789")
                        .bankCode("CMMBAK001")
                        .accountPassword("1111")
                        .userId("user")
                        .build();

        List<Account> mockAccounts = getMockAccount();

        when( accountRepository.findByBankCodeAndAccountNumber( accountVerifyRequest.getBankCode()
                , accountVerifyRequest.getAccountNumber() ) ).thenReturn( mockAccounts.get(0) );

        Account account = accountService.getTradeAccount(accountVerifyRequest, TradeType.DEPOSIT);

        assertNotNull(account);
        assertEquals("001123456789",account.getAccountNumber());
        assertEquals("CMMBAK001",account.getBankCode());
        assertEquals(true,encoder.matches("1111", account.getAccountPassword()));
        assertEquals(UseStatus.USE.getCode(),account.getUseStatus());
        assertEquals(Approval.APPROVAL.getCode(),account.getApprovalStatus());
        assertEquals("user",account.getUserId());
        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
    }

    @Test
    void test_getTradeAccount_not_found() {
        AccountVerifyRequest accountVerifyRequest = AccountVerifyRequest.builder()
                .build();

        when( accountRepository.findByBankCodeAndAccountNumber( accountVerifyRequest.getBankCode()
                , accountVerifyRequest.getAccountNumber() ) ).thenReturn( null );

        BizException exception = assertThrows(BizException.class, () -> accountService.getTradeAccount(accountVerifyRequest, TradeType.DEPOSIT));
        assertEquals(ResponseCode.NOT_FOUND_ACCOUNT.getCode(), exception.getErrCode());
        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
    }

    @Test
    void test_getTradeAccount_no_access() {
        AccountVerifyRequest accountVerifyRequest = AccountVerifyRequest.builder()
                .accountNumber("")
                .bankCode("")
                .accountPassword("1111")
                .userId("user1")
                .build();

        List<Account> mockAccounts = getMockAccount();

        when( accountRepository.findByBankCodeAndAccountNumber( accountVerifyRequest.getBankCode()
                , accountVerifyRequest.getAccountNumber() ) ).thenReturn( mockAccounts.get(1) );

        BizException exception = assertThrows(BizException.class, () -> accountService.getTradeAccount(accountVerifyRequest, TradeType.DEPOSIT));
        assertEquals(ResponseCode.NO_ACCESS_ACCOUNT.getCode(), exception.getErrCode());
        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
    }

    @Test
    void test_getTradeAccount_invalid_password() {
        AccountVerifyRequest accountVerifyRequest = AccountVerifyRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword("1112")
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        when( accountRepository.findByBankCodeAndAccountNumber( accountVerifyRequest.getBankCode()
                , accountVerifyRequest.getAccountNumber() ) ).thenReturn( mockAccounts.get(0) );

        BizException exception = assertThrows(BizException.class, () -> accountService.getTradeAccount(accountVerifyRequest, TradeType.DEPOSIT));
        assertEquals(ResponseCode.INVALID_ACCOUNT.getCode(), exception.getErrCode());
        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
    }

    @Test
    void test_getTradeAccount_not_used() {
        AccountVerifyRequest accountVerifyRequest = AccountVerifyRequest.builder()
                .accountNumber("001987654321")
                .bankCode("CMMBAK001")
                .accountPassword("1234")
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        when( accountRepository.findByBankCodeAndAccountNumber( accountVerifyRequest.getBankCode()
                , accountVerifyRequest.getAccountNumber() ) ).thenReturn( mockAccounts.get(1) );

        BizException exception = assertThrows(BizException.class, () -> accountService.getTradeAccount(accountVerifyRequest, TradeType.DEPOSIT));
        assertEquals(ResponseCode.NOT_USED_ACCOUNT.getCode(), exception.getErrCode());
        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );

    }

    @Test
    void test_getTradeAccount_not_approved() {
        AccountVerifyRequest accountVerifyRequest = AccountVerifyRequest.builder()
                .accountNumber("001122334455")
                .bankCode("CMMBAK001")
                .accountPassword("4321")
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        when( accountRepository.findByBankCodeAndAccountNumber( accountVerifyRequest.getBankCode()
                , accountVerifyRequest.getAccountNumber() ) ).thenReturn( mockAccounts.get(2) );

        BizException exception = assertThrows(BizException.class, () -> accountService.getTradeAccount(accountVerifyRequest, TradeType.DEPOSIT));
        assertEquals(ResponseCode.NOT_APPROVED_ACCOUNT.getCode(), exception.getErrCode());
        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
    }


    @Test
    void test_save_success(){
        AccountRequest accountRequest = AccountRequest.builder()
                .bankCode("CMMBAK001")
                .productCode("CMMPRD003")
                .accountPassword("1122")
                .userId("user")
                .build();

        CommonCode bankCode = CommonCode.builder()
                .NO(1)
                .code("CMMBAK001")
                .codeName("한국 은행")
                .parentCode("CMMBAK")
                .build();

        Product product = Product.builder()
                .NO(1)
                .productCode("CMMPRD003")
                .name("보통예금")
                .build();

        when(commonCodeService.getCodeDetail(accountRequest.getBankCode(), ParentType.BANK.getCode())).thenReturn(bankCode);

        when(productService.findByProductCode("CMMPRD003")).thenReturn(product);

        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account account = accountService.save(accountRequest);

        assertNotNull(account);
        assertEquals(true,encoder.matches("1122", account.getAccountPassword()));
        assertEquals("CMMBAK001", account.getBankCode());
        assertEquals("CMMPRD003", account.getProductCode());
        assertEquals("user", account.getUserId());
        assertEquals(Approval.NOT_APPROVED.getCode(), account.getApprovalStatus());
        assertEquals(UseStatus.USE.getCode(), account.getUseStatus());

        verify(commonCodeRepository, times(1)).findByCodeAndParentCode(accountRequest.getBankCode(), ParentType.BANK.getCode());
        verify(productRepository, times(1)).findByProductCode("CMMPRD003");
        verify(accountRepository, times(1)).save(any(Account.class));

    }

    @Test
    void test_save_invalid_bank(){
        AccountRequest accountRequest = AccountRequest.builder()
                .bankCode("CMMBAK001")
                .productCode("CMMPRD003")
                .accountPassword("1122")
                .userId("user")
                .build();

        CommonCode bankCode = CommonCode.builder()
                .NO(1)
                .code("CMMBAK001")
                .codeName("한국 은행")
                .parentCode("CMMBAK")
                .build();

        Product product = Product.builder()
                .NO(1)
                .productCode("CMMPRD003")
                .name("보통예금")
                .build();

        when(commonCodeService.getCodeDetail(accountRequest.getBankCode(), ParentType.BANK.getCode())).thenReturn(null);
        when(productService.findByProductCode("CMMPRD003")).thenReturn(product);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BizException exception = assertThrows(BizException.class, () -> accountService.save(accountRequest));
        assertEquals(ResponseCode.INVALID_BANK.getCode(), exception.getErrCode());

        verify(commonCodeRepository, times(1)).findByCodeAndParentCode(accountRequest.getBankCode(), ParentType.BANK.getCode());
        verify(productRepository, times(0)).findByProductCode("CMMPRD003");
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    void test_save_invalid_product(){
        AccountRequest accountRequest = AccountRequest.builder()
                .bankCode("CMMBAK001")
                .productCode("CMMPRD003")
                .accountPassword("1122")
                .userId("user")
                .build();

        CommonCode bankCode = CommonCode.builder()
                .NO(1)
                .code("CMMBAK001")
                .codeName("한국 은행")
                .parentCode("CMMBAK")
                .build();

        Product product = Product.builder()
                .NO(1)
                .productCode("CMMPRD003")
                .name("보통예금")
                .build();

        when(commonCodeService.getCodeDetail(accountRequest.getBankCode(), ParentType.BANK.getCode())).thenReturn(bankCode);
        when(productService.findByProductCode("CMMPRD003")).thenReturn(null);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BizException exception = assertThrows(BizException.class, () -> accountService.save(accountRequest));
        assertEquals(ResponseCode.INVALID_PRODUCT.getCode(), exception.getErrCode());

        verify(commonCodeRepository, times(1)).findByCodeAndParentCode(accountRequest.getBankCode(), ParentType.BANK.getCode());
        verify(productRepository, times(1)).findByProductCode("CMMPRD003");
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    void test_delete_success(){
        AccountVerifyRequest accountVerifyRequest = AccountVerifyRequest.builder()
                .accountNumber("001987654321")
                .bankCode("CMMBAK001")
                .accountPassword("1234")
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        when( accountRepository.findByBankCodeAndAccountNumber( accountVerifyRequest.getBankCode()
                , accountVerifyRequest.getAccountNumber() ) ).thenReturn( mockAccounts.get(1) );
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        accountService.deleteAccount(accountVerifyRequest);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void test_delete_not_found() {
        AccountVerifyRequest accountVerifyRequest = AccountVerifyRequest.builder()
                .accountNumber("0019876543211")
                .bankCode("CMMBAK001")
                .accountPassword("1234")
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();
        when( accountRepository.findByBankCodeAndAccountNumber( accountVerifyRequest.getBankCode()
                , accountVerifyRequest.getAccountNumber() ) ).thenReturn( null );
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BizException exception = assertThrows(BizException.class, () -> accountService.deleteAccount(accountVerifyRequest));
        assertEquals(ResponseCode.NOT_FOUND_ACCOUNT.getCode(), exception.getErrCode());
        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    void test_delete_no_access() {
        AccountVerifyRequest accountVerifyRequest = AccountVerifyRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword("1234")
                .userId("user1")
                .build();

        List<Account> mockAccounts = getMockAccount();
        when( accountRepository.findByBankCodeAndAccountNumber( accountVerifyRequest.getBankCode()
                , accountVerifyRequest.getAccountNumber() ) ).thenReturn( mockAccounts.get(1) );
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BizException exception = assertThrows(BizException.class, () -> accountService.deleteAccount(accountVerifyRequest));
        assertEquals(ResponseCode.NO_ACCESS_ACCOUNT.getCode(), exception.getErrCode());
        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    void test_delete_invalid_password() {
        AccountVerifyRequest accountVerifyRequest = AccountVerifyRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword("1112")
                .userId("user")
                .build();
        List<Account> mockAccounts = getMockAccount();
        when( accountRepository.findByBankCodeAndAccountNumber( accountVerifyRequest.getBankCode()
                , accountVerifyRequest.getAccountNumber() ) ).thenReturn( mockAccounts.get(1) );
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BizException exception = assertThrows(BizException.class, () -> accountService.deleteAccount(accountVerifyRequest));
        assertEquals(ResponseCode.INVALID_ACCOUNT.getCode(), exception.getErrCode());
        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
    }


}

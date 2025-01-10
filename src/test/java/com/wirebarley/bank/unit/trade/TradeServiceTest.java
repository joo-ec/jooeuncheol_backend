package com.wirebarley.bank.unit.trade;

import com.wirebarley.bank.account.service.impl.AccountServiceImpl;
import com.wirebarley.bank.common.code.service.impl.CommonCodeServiceImpl;
import com.wirebarley.bank.common.dto.request.AccountVerifyRequest;
import com.wirebarley.bank.common.dto.request.DepositRequest;
import com.wirebarley.bank.common.dto.request.TransferRequest;
import com.wirebarley.bank.common.dto.request.WithdrawalRequest;
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
import com.wirebarley.bank.trade.service.impl.TradeServiceImpl;
import com.wirebarley.core.exception.BizException;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class TradeServiceTest {
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final CommonCodeRepository commonCodeRepository = mock(CommonCodeRepository.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final TradeRepository tradeRepository = mock(TradeRepository.class);

    private final CommonCodeServiceImpl commonCodeService = new CommonCodeServiceImpl(commonCodeRepository);
    private final ProductServiceImpl productService = new ProductServiceImpl(productRepository);
    private final AccountServiceImpl accountService = new AccountServiceImpl(
            productService,
            commonCodeService,
            accountRepository,
            encoder
    );
    private final TradeServiceImpl tradeService = new TradeServiceImpl(accountService, tradeRepository);

    List<Account> getMockAccount() {
        Account account1 = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(100000)
                .withdrawalBounds(1000000)
                .transferBounds(3000000)
                .userId("user")
                .build();

        Account account2 = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.NOT_USED.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .userId("user")
                .build();

        Account account3 = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.NOT_APPROVED.getCode())
                .userId("user")
                .build();

        Account account4 = Account.builder()
                .accountNumber("001987654321")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1234"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(0)
                .withdrawalBounds(1000000)
                .transferBounds(3000000)
                .userId("user")
                .build();

        return Arrays.asList(account1, account2, account3, account4);
    }

    List<Trade> getMockTrade() {
        Trade trade1 = Trade.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .targetAccountNumber(null)
                .targetBankCode(null)
                .tradeType(TradeType.DEPOSIT.getCode())
                .tradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()))
                .amount(100000)
                .fee(0)
                .balance(100000)
                .build();

        Trade trade2= Trade.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .targetAccountNumber(null)
                .targetBankCode(null)
                .tradeType(TradeType.WITHDRAWAL.getCode())
                .tradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()))
                .amount(10000)
                .fee(0)
                .balance(90000)
                .build();

        Trade trade3 = Trade.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .targetAccountNumber("001123456789")
                .targetBankCode("CMMBAK001")
                .tradeType(TradeType.TRANSFER.getCode())
                .tradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()))
                .amount(10000)
                .fee(0)
                .balance(90000)
                .build();



        return Arrays.asList(trade1, trade2, trade3);
    }

    List<Trade> getMockTradeExceed() {
        Trade trade1 = Trade.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .targetAccountNumber(null)
                .targetBankCode(null)
                .tradeType(TradeType.DEPOSIT.getCode())
                .tradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()))
                .amount(100000)
                .fee(0)
                .balance(100000)
                .build();

        Trade trade2= Trade.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .targetAccountNumber(null)
                .targetBankCode(null)
                .tradeType(TradeType.WITHDRAWAL.getCode())
                .tradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()))
                .amount(5000000)
                .fee(0)
                .balance(0)
                .build();

        Trade trade3 = Trade.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .targetAccountNumber("001123456789")
                .targetBankCode("CMMBAK001")
                .tradeType(TradeType.TRANSFER.getCode())
                .tradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()))
                .amount(10000)
                .fee(0)
                .balance(90000)
                .build();



        return Arrays.asList(trade1, trade2, trade3);
    }

    @Test
    void test_depositAccount_success() {
        DepositRequest depositRequest = DepositRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .amount(30000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(1300000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(depositRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        BalanceDTO balanceDTO = JsonUtils.convert(trade, BalanceDTO.class);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(0));
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BalanceDTO result = tradeService.depositAccount(depositRequest);

        assertNotNull(result);
        assertEquals(1300000, result.getBalance());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(tradeRepository, times(1)).save(any(Trade.class));

    }

    @Test
    void test_depositAccount_invalid_amount() {
        DepositRequest depositRequest = DepositRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .amount(0)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(1300000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(depositRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        BalanceDTO balanceDTO = JsonUtils.convert(trade, BalanceDTO.class);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(0));
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.depositAccount(depositRequest));
        assertEquals(ResponseCode.INVALID_AMOUNT_TRADE.getCode(), exception.getErrCode());

        verify(accountRepository, times(0)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }

    @Test
    void test_depositAccount_not_found() {
        DepositRequest depositRequest = DepositRequest.builder()
                .accountNumber("0011234567891")
                .bankCode("CMMBAK001")
                .amount(30000)
                .userId("user")
                .build();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(1300000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(depositRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(null);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.depositAccount(depositRequest));
        assertEquals(ResponseCode.NOT_FOUND_ACCOUNT.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }

    @Test
    void test_depositAccount_no_access() {
        DepositRequest depositRequest = DepositRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .amount(30000)
                .userId("user1")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(1300000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(depositRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(null);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.depositAccount(depositRequest));
        assertEquals(ResponseCode.NOT_FOUND_ACCOUNT.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }

    @Test
    void test_depositAccount_invalid_password() {
        DepositRequest depositRequest = DepositRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .amount(30000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(1300000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(depositRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(0));
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.depositAccount(depositRequest));
        assertEquals(ResponseCode.INVALID_ACCOUNT.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }

    @Test
    void test_depositAccount_not_used() {
        DepositRequest depositRequest = DepositRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .amount(30000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(1300000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(depositRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(1));
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.depositAccount(depositRequest));
        assertEquals(ResponseCode.NOT_USED_ACCOUNT.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }

    @Test
    void test_depositAccount_not_approved() {
        DepositRequest depositRequest = DepositRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .amount(30000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(1300000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(depositRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(2));
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.depositAccount(depositRequest));
        assertEquals(ResponseCode.NOT_APPROVED_ACCOUNT.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }


    @Test
    void test_withdrawalAccount_success() {
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(10000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(withdrawalRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.WITHDRAWAL.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        BalanceDTO balanceDTO = JsonUtils.convert(trade, BalanceDTO.class);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(0));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        BalanceDTO result = tradeService.withdrawalAccount(withdrawalRequest);

        assertNotNull(result);
        assertEquals(90000, result.getBalance());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(tradeRepository, times(1)).save(any(Trade.class));

    }

    @Test
    void test_withdrawalAccount_invalid_amount() {
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(0)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(withdrawalRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.WITHDRAWAL.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        BalanceDTO balanceDTO = JsonUtils.convert(trade, BalanceDTO.class);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(0));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.withdrawalAccount(withdrawalRequest));
        assertEquals(ResponseCode.INVALID_AMOUNT_TRADE.getCode(), exception.getErrCode());

        verify(accountRepository, times(0)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }

    @Test
    void test_withdrawalAccount_not_found() {
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber("0011234567891")
                .bankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(10000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(withdrawalRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.WITHDRAWAL.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        BalanceDTO balanceDTO = JsonUtils.convert(trade, BalanceDTO.class);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(null);
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.withdrawalAccount(withdrawalRequest));
        assertEquals(ResponseCode.NOT_FOUND_ACCOUNT.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }

    @Test
    void test_withdrawalAccount_no_access() {
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(10000)
                .userId("user1")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(withdrawalRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.WITHDRAWAL.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        BalanceDTO balanceDTO = JsonUtils.convert(trade, BalanceDTO.class);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(0));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.withdrawalAccount(withdrawalRequest));
        assertEquals(ResponseCode.NO_ACCESS_ACCOUNT.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));
    }

    @Test
    void test_withdrawalAccount_invalid_password() {
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword("11112")
                .amount(10000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(withdrawalRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.WITHDRAWAL.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        BalanceDTO balanceDTO = JsonUtils.convert(trade, BalanceDTO.class);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(0));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.withdrawalAccount(withdrawalRequest));
        assertEquals(ResponseCode.INVALID_ACCOUNT.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));
    }

    @Test
    void test_withdrawalAccount_not_used() {
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(10000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(withdrawalRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.WITHDRAWAL.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        BalanceDTO balanceDTO = JsonUtils.convert(trade, BalanceDTO.class);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(1));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.withdrawalAccount(withdrawalRequest));
        assertEquals(ResponseCode.NOT_USED_ACCOUNT.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));
    }

    @Test
    void test_withdrawalAccount_not_approved() {
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(10000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(withdrawalRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.WITHDRAWAL.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        BalanceDTO balanceDTO = JsonUtils.convert(trade, BalanceDTO.class);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(2));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.withdrawalAccount(withdrawalRequest));
        assertEquals(ResponseCode.NOT_APPROVED_ACCOUNT.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));
    }

    @Test
    void test_withdrawalAccount_invalid_balance() {
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(1000000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(withdrawalRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.WITHDRAWAL.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        BalanceDTO balanceDTO = JsonUtils.convert(trade, BalanceDTO.class);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(0));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.withdrawalAccount(withdrawalRequest));
        assertEquals(ResponseCode.INVALID_WITHDRAWAL_AMOUNT_TRADE.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }

    @Test
    void test_withdrawalAccount_invalid_bounds() {
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(10000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(withdrawalRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.WITHDRAWAL.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTradeExceed();

        BalanceDTO balanceDTO = JsonUtils.convert(trade, BalanceDTO.class);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(0));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.withdrawalAccount(withdrawalRequest));
        assertEquals(ResponseCode.OVER_BOUNDS_TRADE.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(tradeRepository, times(1)).findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }


    @Test
    void test_transferAccount_success() {
        TransferRequest transferRequest = TransferRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .targetAccountNumber("001987654321")
                .targetBankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(10000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(transferRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.TRANSFER.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee((long) (transferRequest.getAmount() * 0.01));
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        DepositRequest depositRequest = DepositRequest.builder()
                .bankCode(transferRequest.getTargetBankCode())
                .accountNumber(transferRequest.getTargetAccountNumber())
                .amount(transferRequest.getAmount())
                .build();

        Account updateTargetAccount = Account.builder()
                .accountNumber("001987654321")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1234"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(10000)
                .userId("user")
                .build();

        Trade targetTrade = JsonUtils.convert(updateTargetAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(0));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        when(accountRepository.findByBankCodeAndAccountNumber(depositRequest.getBankCode(),depositRequest.getAccountNumber())).thenReturn(mockAccounts.get(3));
        when(accountService.update(any(Account.class))).thenReturn(updateTargetAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BalanceDTO result = tradeService.transferAccount(transferRequest);

        assertNotNull(result);
        assertEquals(90000, result.getBalance());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(tradeRepository, times(1)).findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber() );
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(tradeRepository, times(2)).save(any(Trade.class));
        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );

    }

    @Test
    void test_transferAccount_invalid_amount() {
        TransferRequest transferRequest = TransferRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .targetAccountNumber("001987654321")
                .targetBankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(0)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(transferRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.TRANSFER.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee((long) (transferRequest.getAmount() * 0.01));
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        DepositRequest depositRequest = DepositRequest.builder()
                .bankCode(transferRequest.getTargetBankCode())
                .accountNumber(transferRequest.getTargetAccountNumber())
                .amount(transferRequest.getAmount())
                .build();

        Account updateTargetAccount = Account.builder()
                .accountNumber("001987654321")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1234"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(10000)
                .userId("user")
                .build();

        Trade targetTrade = JsonUtils.convert(updateTargetAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(0));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        when(accountRepository.findByBankCodeAndAccountNumber(depositRequest.getBankCode(),depositRequest.getAccountNumber())).thenReturn(mockAccounts.get(3));
        when(accountService.update(any(Account.class))).thenReturn(updateTargetAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.transferAccount(transferRequest));
        assertEquals(ResponseCode.INVALID_AMOUNT_TRADE.getCode(), exception.getErrCode());

        verify(accountRepository, times(0)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(tradeRepository, times(0)).findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));
        verify(accountRepository, times(0)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );

    }

    @Test
    void test_transferAccount_not_found() {
        TransferRequest transferRequest = TransferRequest.builder()
                .accountNumber("0011234567891")
                .bankCode("CMMBAK001")
                .targetAccountNumber("001987654321")
                .targetBankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(1000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(transferRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.TRANSFER.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee((long) (transferRequest.getAmount() * 0.01));
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        DepositRequest depositRequest = DepositRequest.builder()
                .bankCode(transferRequest.getTargetBankCode())
                .accountNumber(transferRequest.getTargetAccountNumber())
                .amount(transferRequest.getAmount())
                .build();

        Account updateTargetAccount = Account.builder()
                .accountNumber("001987654321")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1234"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(10000)
                .userId("user")
                .build();

        Trade targetTrade = JsonUtils.convert(updateTargetAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(null);
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        when(accountRepository.findByBankCodeAndAccountNumber(depositRequest.getBankCode(),depositRequest.getAccountNumber())).thenReturn(mockAccounts.get(3));
        when(accountService.update(any(Account.class))).thenReturn(updateTargetAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.transferAccount(transferRequest));
        assertEquals(ResponseCode.NOT_FOUND_ACCOUNT.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(tradeRepository, times(0)).findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }

    @Test
    void test_transferAccount_no_access() {
        TransferRequest transferRequest = TransferRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .targetAccountNumber("001987654321")
                .targetBankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(1000)
                .userId("user1")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(transferRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.TRANSFER.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee((long) (transferRequest.getAmount() * 0.01));
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        DepositRequest depositRequest = DepositRequest.builder()
                .bankCode(transferRequest.getTargetBankCode())
                .accountNumber(transferRequest.getTargetAccountNumber())
                .amount(transferRequest.getAmount())
                .build();

        Account updateTargetAccount = Account.builder()
                .accountNumber("00198765432")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1234"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(10000)
                .userId("user")
                .build();

        Trade targetTrade = JsonUtils.convert(updateTargetAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(0));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        when(accountRepository.findByBankCodeAndAccountNumber(depositRequest.getBankCode(),depositRequest.getAccountNumber())).thenReturn(mockAccounts.get(3));
        when(accountService.update(any(Account.class))).thenReturn(updateTargetAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.transferAccount(transferRequest));
        assertEquals(ResponseCode.NO_ACCESS_ACCOUNT.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(tradeRepository, times(0)).findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }

    @Test
    void test_transferAccount_invalid_password() {
        TransferRequest transferRequest = TransferRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .targetAccountNumber("001987654321")
                .targetBankCode("CMMBAK001")
                .accountPassword("11112")
                .amount(1000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(transferRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.TRANSFER.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee((long) (transferRequest.getAmount() * 0.01));
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        DepositRequest depositRequest = DepositRequest.builder()
                .bankCode(transferRequest.getTargetBankCode())
                .accountNumber(transferRequest.getTargetAccountNumber())
                .amount(transferRequest.getAmount())
                .build();

        Account updateTargetAccount = Account.builder()
                .accountNumber("00198765432")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1234"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(10000)
                .userId("user")
                .build();

        Trade targetTrade = JsonUtils.convert(updateTargetAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(0));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        when(accountRepository.findByBankCodeAndAccountNumber(depositRequest.getBankCode(),depositRequest.getAccountNumber())).thenReturn(mockAccounts.get(3));
        when(accountService.update(any(Account.class))).thenReturn(updateTargetAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.transferAccount(transferRequest));
        assertEquals(ResponseCode.INVALID_ACCOUNT.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(tradeRepository, times(0)).findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }

    @Test
    void test_transferAccount_not_uesd() {
        TransferRequest transferRequest = TransferRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .targetAccountNumber("001987654321")
                .targetBankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(1000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(transferRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.TRANSFER.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee((long) (transferRequest.getAmount() * 0.01));
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        DepositRequest depositRequest = DepositRequest.builder()
                .bankCode(transferRequest.getTargetBankCode())
                .accountNumber(transferRequest.getTargetAccountNumber())
                .amount(transferRequest.getAmount())
                .build();

        Account updateTargetAccount = Account.builder()
                .accountNumber("00198765432")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1234"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(10000)
                .userId("user")
                .build();

        Trade targetTrade = JsonUtils.convert(updateTargetAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(1));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        when(accountRepository.findByBankCodeAndAccountNumber(depositRequest.getBankCode(),depositRequest.getAccountNumber())).thenReturn(mockAccounts.get(3));
        when(accountService.update(any(Account.class))).thenReturn(updateTargetAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.transferAccount(transferRequest));
        assertEquals(ResponseCode.NOT_USED_ACCOUNT.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(tradeRepository, times(0)).findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }

    @Test
    void test_transferAccount_not_approved() {
        TransferRequest transferRequest = TransferRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .targetAccountNumber("001987654321")
                .targetBankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(1000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(transferRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.TRANSFER.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee((long) (transferRequest.getAmount() * 0.01));
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        DepositRequest depositRequest = DepositRequest.builder()
                .bankCode(transferRequest.getTargetBankCode())
                .accountNumber(transferRequest.getTargetAccountNumber())
                .amount(transferRequest.getAmount())
                .build();

        Account updateTargetAccount = Account.builder()
                .accountNumber("00198765432")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1234"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(10000)
                .userId("user")
                .build();

        Trade targetTrade = JsonUtils.convert(updateTargetAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(2));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        when(accountRepository.findByBankCodeAndAccountNumber(depositRequest.getBankCode(),depositRequest.getAccountNumber())).thenReturn(mockAccounts.get(3));
        when(accountService.update(any(Account.class))).thenReturn(updateTargetAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.transferAccount(transferRequest));
        assertEquals(ResponseCode.NOT_APPROVED_ACCOUNT.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(tradeRepository, times(0)).findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }

    @Test
    void test_transferAccount_invalid_balance() {
        TransferRequest transferRequest = TransferRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .targetAccountNumber("001987654321")
                .targetBankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(10000000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(transferRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.TRANSFER.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee((long) (transferRequest.getAmount() * 0.01));
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTrade();

        DepositRequest depositRequest = DepositRequest.builder()
                .bankCode(transferRequest.getTargetBankCode())
                .accountNumber(transferRequest.getTargetAccountNumber())
                .amount(transferRequest.getAmount())
                .build();

        Account updateTargetAccount = Account.builder()
                .accountNumber("00198765432")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1234"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(10000)
                .userId("user")
                .build();

        Trade targetTrade = JsonUtils.convert(updateTargetAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(0));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        when(accountRepository.findByBankCodeAndAccountNumber(depositRequest.getBankCode(),depositRequest.getAccountNumber())).thenReturn(mockAccounts.get(3));
        when(accountService.update(any(Account.class))).thenReturn(updateTargetAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.transferAccount(transferRequest));
        assertEquals(ResponseCode.INVALID_WITHDRAWAL_AMOUNT_TRADE.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(tradeRepository, times(0)).findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }

    @Test
    void test_transferAccount_invalid_bounds() {
        TransferRequest transferRequest = TransferRequest.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .targetAccountNumber("001987654321")
                .targetBankCode("CMMBAK001")
                .accountPassword("1111")
                .amount(1000)
                .userId("user")
                .build();

        List<Account> mockAccounts = getMockAccount();

        Account updateAccount = Account.builder()
                .accountNumber("001123456789")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1111"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(90000)
                .userId("user")
                .build();

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(transferRequest, AccountVerifyRequest.class);

        Trade trade = JsonUtils.convert(updateAccount, Trade.class);
        trade.setTradeType(TradeType.TRANSFER.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee((long) (transferRequest.getAmount() * 0.01));
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        List<Trade> mockTrades = getMockTradeExceed();

        DepositRequest depositRequest = DepositRequest.builder()
                .bankCode(transferRequest.getTargetBankCode())
                .accountNumber(transferRequest.getTargetAccountNumber())
                .amount(transferRequest.getAmount())
                .build();

        Account updateTargetAccount = Account.builder()
                .accountNumber("00198765432")
                .bankCode("CMMBAK001")
                .accountPassword(encoder.encode("1234"))
                .useStatus(UseStatus.USE.getCode())
                .approvalStatus(Approval.APPROVAL.getCode())
                .balance(10000)
                .userId("user")
                .build();

        Trade targetTrade = JsonUtils.convert(updateTargetAccount, Trade.class);
        trade.setTradeType(TradeType.DEPOSIT.getCode());
        trade.setAmount(updateAccount.getBalance());
        trade.setFee(0);
        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));
        trade.setNO(0);

        when(accountRepository.findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber())).thenReturn(mockAccounts.get(0));
        when(tradeRepository.findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber())).thenReturn(mockTrades);
        when(accountService.update(any(Account.class))).thenReturn(updateAccount);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        when(accountRepository.findByBankCodeAndAccountNumber(depositRequest.getBankCode(),depositRequest.getAccountNumber())).thenReturn(mockAccounts.get(3));
        when(accountService.update(any(Account.class))).thenReturn(updateTargetAccount);
        when(tradeService.saveAndUpdate(any(Trade.class))).thenReturn(trade);

        BizException exception = assertThrows(BizException.class, () -> tradeService.transferAccount(transferRequest));
        assertEquals(ResponseCode.OVER_BOUNDS_TRADE.getCode(), exception.getErrCode());

        verify(accountRepository, times(1)).findByBankCodeAndAccountNumber(accountVerifyRequest.getBankCode(),accountVerifyRequest.getAccountNumber() );
        verify(tradeRepository, times(1)).findByBankCodeAndAccountNumber(mockAccounts.get(0).getBankCode(),mockAccounts.get(0).getAccountNumber() );
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(tradeRepository, times(0)).save(any(Trade.class));

    }


}

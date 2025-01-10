package com.wirebarley.bank.account.service.impl;

import com.wirebarley.bank.account.service.AccountService;
import com.wirebarley.bank.common.code.service.CommonCodeService;
import com.wirebarley.bank.common.dto.request.AccountRequest;
import com.wirebarley.bank.common.dto.request.AccountVerifyRequest;
import com.wirebarley.bank.common.entity.Account;
import com.wirebarley.bank.common.entity.CommonCode;
import com.wirebarley.bank.common.entity.Product;
import com.wirebarley.bank.common.repository.AccountRepository;
import com.wirebarley.bank.common.type.*;
import com.wirebarley.bank.common.utils.BankUtils;
import com.wirebarley.bank.product.service.ProductService;
import com.wirebarley.core.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final ProductService productService;
    private final CommonCodeService commonCodeService;

    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;

    @Override
    public List<Account> getAccounts(String userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Account getAccount(AccountVerifyRequest accountRequest) {
        return findByAccount(accountRequest);
    }

    @Override
    @Transactional
    public Account getTradeAccount(AccountVerifyRequest accountRequest, TradeType tradeType) {
        return findByAccount(accountRequest, tradeType);
    }

    @Override
    public Account save(AccountRequest accountRequest) {

        CommonCode bankCode = commonCodeService.getCodeDetail(accountRequest.getBankCode(),ParentType.BANK.getCode());
        if( bankCode == null ) { throw new BizException(ResponseCode.INVALID_BANK); }


        Product product = productService.findByProductCode(accountRequest.getProductCode());

        if( product == null ) { throw new BizException(ResponseCode.INVALID_PRODUCT); }

        Account account = Account.builder()
                .accountNumber(generateAccountNumber(accountRequest.getBankCode()))
                .accountPassword(encoder.encode(accountRequest.getAccountPassword()))
                .bankCode(bankCode.getCode())
                .productCode(product.getProductCode())
                .approvalStatus(Approval.NOT_APPROVED.getCode())
                .useStatus(UseStatus.USE.getCode())
                .balance(0)
                .withdrawalBounds(product.getWithdrawalBounds())
                .transferBounds(product.getTransferBounds())
                .interestRate(product.getInterestRate())
                .userId(accountRequest.getUserId())
                .registrationId(accountRequest.getUserId())
                .build();

        return update(account);
    }

    @Override
    @Transactional
    public Account update(Account account) {
        return accountRepository.save(account);
    }

    public Account findByAccount(AccountVerifyRequest accountRequest) {
        return findByAccount(accountRequest, null);
    }

    public Account findByAccount(AccountVerifyRequest accountRequest, TradeType tradeType) {
        Account account = accountRepository.findByBankCodeAndAccountNumber(accountRequest.getBankCode(),accountRequest.getAccountNumber());

        if( account == null ) { throw new BizException(ResponseCode.NOT_FOUND_ACCOUNT); }

        if( tradeType != null ) {
            if (Approval.NOT_APPROVED.getCode().equals(account.getApprovalStatus())) { throw new BizException(ResponseCode.NOT_APPROVED_ACCOUNT); }
            if (UseStatus.NOT_USED.getCode().equals(account.getUseStatus())) { throw new BizException(ResponseCode.NOT_USED_ACCOUNT); }
        }

        if( TradeType.DEPOSIT != tradeType ) {

            if( !account.getUserId().equals( accountRequest.getUserId() ) ) { throw new BizException(ResponseCode.NO_ACCESS_ACCOUNT); }

            if (!encoder.matches(accountRequest.getAccountPassword(), account.getAccountPassword())) {
                throw new BizException(ResponseCode.INVALID_ACCOUNT);
            }
        }

        return account;
    }




    @Override
    public void deleteAccount(AccountVerifyRequest accountRequest) {
        Account account = findByAccount(accountRequest);
        account.setUseStatus(UseStatus.NOT_USED.getCode());
        accountRepository.save(account);
    }




    private String generateAccountNumber(String bankCode) {
        StringBuilder accountNumber = new StringBuilder();

        do {
            accountNumber.setLength(0);
            accountNumber.append(bankCode.replace("CMMBAK", ""))
                    .append(BankUtils.generateRandomString(9));
        } while( accountRepository.countByAccountNumber(accountNumber.toString()) > 0 );

        return accountNumber.toString();
    }



}

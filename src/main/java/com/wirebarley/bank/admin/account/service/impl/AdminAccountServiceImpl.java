package com.wirebarley.bank.admin.account.service.impl;


import com.wirebarley.bank.admin.account.service.AdminAccountService;
import com.wirebarley.bank.common.dto.request.ApprovalAccountRequest;
import com.wirebarley.bank.common.entity.Account;
import com.wirebarley.bank.common.repository.AccountRepository;
import com.wirebarley.bank.common.type.Approval;
import com.wirebarley.bank.common.type.ResponseCode;
import com.wirebarley.bank.common.type.UseStatus;
import com.wirebarley.core.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class AdminAccountServiceImpl implements AdminAccountService {

    private final AccountRepository accountRepository;


    @Override
    @Transactional
    public void approvalAccount(ApprovalAccountRequest accountRequest) {

        Account account = accountRepository.findByBankCodeAndAccountNumber(accountRequest.getBankCode(), accountRequest.getAccountNumber());

        if( account == null ) { throw new BizException(ResponseCode.NOT_FOUND_ACCOUNT); }
        if( Approval.APPROVAL.getCode().equals( account.getApprovalStatus() ) ) { throw new BizException(ResponseCode.NOT_APPROVED_ACCOUNT); }
        if( UseStatus.NOT_USED.getCode().equals( account.getUseStatus() ) ) { throw new BizException(ResponseCode.NOT_USED_ACCOUNT); }

        account.setApprovalStatus(Approval.APPROVAL.getCode());

        accountRepository.save(account);
    }

}

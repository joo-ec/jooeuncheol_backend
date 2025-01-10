package com.wirebarley.bank.account.service;

import com.wirebarley.bank.common.dto.request.AccountRequest;
import com.wirebarley.bank.common.dto.request.AccountVerifyRequest;
import com.wirebarley.bank.common.dto.request.DepositRequest;
import com.wirebarley.bank.common.entity.Account;
import com.wirebarley.bank.common.type.TradeType;

import java.util.List;

public interface AccountService {

    List<Account> getAccounts(String userId);
    Account getAccount(AccountVerifyRequest accountRequest);
    Account getTradeAccount(AccountVerifyRequest accountRequest, TradeType tradeType);

    Account save(AccountRequest accountRequest);

    void deleteAccount(AccountVerifyRequest accountRequest);

    Account update(Account account);

}

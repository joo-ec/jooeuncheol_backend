package com.wirebarley.bank.trade.service.impl;

import com.wirebarley.bank.account.service.AccountService;
import com.wirebarley.bank.common.dto.request.*;
import com.wirebarley.bank.common.dto.response.BalanceDTO;
import com.wirebarley.bank.common.dto.response.TradeDetailsDTO;
import com.wirebarley.bank.common.entity.Account;
import com.wirebarley.bank.common.entity.Trade;
import com.wirebarley.bank.common.repository.TradeRepository;
import com.wirebarley.bank.common.type.ResponseCode;
import com.wirebarley.bank.common.type.TradeType;
import com.wirebarley.bank.common.utils.JsonUtils;
import com.wirebarley.bank.trade.service.TradeService;
import com.wirebarley.core.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.wirebarley.bank.common.repository.specifications.TransferDetailsSpecifications.*;


@Slf4j
@Service
@AllArgsConstructor
public class TradeServiceImpl implements TradeService {

    private final AccountService accountService;

    private final TradeRepository tradeRepository;

    @Override
    @Transactional
    public BalanceDTO depositAccount(DepositRequest depositRequest) {

        if( depositRequest.getAmount() < 1 ) throw new BizException(ResponseCode.INVALID_AMOUNT_TRADE);

        AccountVerifyRequest accountVerifyRequest = JsonUtils.convert(depositRequest, AccountVerifyRequest.class);

        Account account = accountService.getTradeAccount(accountVerifyRequest, TradeType.DEPOSIT);
        account.setBalance( account.getBalance() + depositRequest.getAmount() );

        Account latestAccount = accountService.update(account);

        Trade trade = tradeBuilder(TradeType.DEPOSIT, latestAccount, depositRequest.getAmount());
        trade = saveAndUpdate(trade);

        return JsonUtils.convert(trade, BalanceDTO.class);
    }


   /*
     * 계좌 출금 기능 ( 1일 한도 1,000,000 )
     * @param withdrawalRequest
     * @return BalanceDTO
    */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BalanceDTO withdrawalAccount(WithdrawalRequest withdrawalRequest) {

        Account account = calcBalance(JsonUtils.convert(withdrawalRequest, AccountVerifyRequest.class), withdrawalRequest.getAmount(), TradeType.WITHDRAWAL );

        Trade trade = tradeBuilder(TradeType.WITHDRAWAL, account, withdrawalRequest.getAmount());

        return JsonUtils.convert(tradeRepository.save(trade), BalanceDTO.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BalanceDTO transferAccount(TransferRequest transferRequest) {
        TradeType tradeType = null;

        if( transferRequest.getBankCode().equals( transferRequest.getTargetBankCode() ) ) {
            tradeType = TradeType.TRANSFER;
        } else {
            tradeType = TradeType.ANOTHER_TRANSFER;
        }

        long fee = Math.round( transferRequest.getAmount() * 0.01 ); // 수수료 계산 출금 금액의 1%
        Account account = calcBalance(JsonUtils.convert(transferRequest, AccountVerifyRequest.class), transferRequest.getAmount(), fee, tradeType);

        Trade trade = tradeBuilder(tradeType, account, transferRequest.getAmount(), fee);

        BalanceDTO balanceDTO = JsonUtils.convert(tradeRepository.save(trade), BalanceDTO.class);

        DepositRequest depositRequest = DepositRequest.builder()
                .bankCode(transferRequest.getTargetBankCode())
                .accountNumber(transferRequest.getTargetAccountNumber())
                .amount(transferRequest.getAmount())
                .build();

        depositAccount( depositRequest );

        return balanceDTO;
    }

    @Override
    public Trade saveAndUpdate(Trade trade) {
        return tradeRepository.save(trade);
    }

    @Override
    public <T> BalanceDTO processException(T requestClazz, int tradeResult) {
        Trade trade = JsonUtils.convert(requestClazz, Trade.class);

        trade.setTradeResult(String.valueOf(tradeResult));

        return JsonUtils.convert(trade, BalanceDTO.class);
    }

    @Override
    public List<TradeDetailsDTO> getTransferList(TransferDetailsRequest detailsRequest) {

        accountService.getAccount(JsonUtils.convert(detailsRequest, AccountVerifyRequest.class));

        Specification<Trade> spec = Specification.where(accountNumberEquals(detailsRequest.getAccountNumber()))
                .and(bankCodeEquals(detailsRequest.getBankCode()))
                .and(tradeTypeEquals(detailsRequest.getTradeType()))
                .and(tradeResultEquals(detailsRequest.getTradeResult()))
                .and(userIdEquals(detailsRequest.getUserId()))
                .and(tradeDateBetween(detailsRequest.getStartTradeDT().atStartOfDay(), detailsRequest.getEndTradeDT().atTime(LocalTime.MAX)));

        Sort sort = Sort.by(Sort.Direction.DESC, "registrationDate");

        List<Trade> tradeList = tradeRepository.findAll(spec, sort);

        List<TradeDetailsDTO> data = tradeList.stream()
                .map(vo-> JsonUtils.convert(vo, TradeDetailsDTO.class))
                .collect(Collectors.toList());

        return data;
    }

    public Account calcBalance( AccountVerifyRequest accountVerifyRequest, long amount, TradeType tradeType) {
        return calcBalance(accountVerifyRequest, amount, 0, tradeType);
    }

    public Account calcBalance( AccountVerifyRequest accountVerifyRequest, long amount, long fee, TradeType tradeType ) {

        if( amount < 1 ) { throw new BizException(ResponseCode.INVALID_AMOUNT_TRADE); }

        Account account = accountService.getTradeAccount(accountVerifyRequest, tradeType);

        if( account.getBalance() < ( amount + fee )) { throw new BizException(ResponseCode.INVALID_WITHDRAWAL_AMOUNT_TRADE); }

        List<Trade> trades = tradeRepository.findByBankCodeAndAccountNumber(account.getBankCode(), account.getAccountNumber());

        long totalBounds = 0;

        if( TradeType.WITHDRAWAL == tradeType ) {
            totalBounds = trades.stream().filter(item->TradeType.WITHDRAWAL.getCode().equals(item.getTradeType())).mapToLong(Trade::getAmount).sum();
            if (account.getWithdrawalBounds() < (totalBounds + amount)) { throw new BizException(ResponseCode.OVER_BOUNDS_TRADE); }
        } else if( TradeType.TRANSFER == tradeType || TradeType.ANOTHER_TRANSFER == tradeType ) {
            totalBounds = trades.stream().filter(item-> TradeType.TRANSFER.getCode().equals(item.getTradeType()) || TradeType.ANOTHER_TRANSFER.getCode().equals(item.getTradeType())).mapToLong(Trade::getAmount).sum();
            if (account.getTransferBounds() < (totalBounds + amount )) { throw new BizException(ResponseCode.OVER_BOUNDS_TRADE); }
        }

        account.setBalance( account.getBalance() - ( amount + fee ) );

        account = accountService.update(account);

        return account;
    }


    private Trade tradeBuilder(TradeType tradeType, Account account, long amount) {
        return tradeBuilder(tradeType, account, amount, 0);
    }

    private Trade tradeBuilder(TradeType tradeType, Account account, long amount, long fee) {
        Trade trade = JsonUtils.convert(account, Trade.class);
        trade.setTradeType(tradeType.getCode());
        trade.setAmount(amount);
        trade.setFee(fee);

        trade.setTradeResult(String.valueOf(ResponseCode.SUCCESS.getCode()));

        trade.setNO(0);

        return trade;
    }

}
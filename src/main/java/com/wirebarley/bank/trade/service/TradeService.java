package com.wirebarley.bank.trade.service;

import com.wirebarley.bank.common.dto.request.DepositRequest;
import com.wirebarley.bank.common.dto.request.TransferDetailsRequest;
import com.wirebarley.bank.common.dto.request.TransferRequest;
import com.wirebarley.bank.common.dto.request.WithdrawalRequest;
import com.wirebarley.bank.common.dto.response.BalanceDTO;
import com.wirebarley.bank.common.dto.response.TradeDetailsDTO;
import com.wirebarley.bank.common.entity.Trade;
import com.wirebarley.bank.common.type.TradeType;

import java.util.List;

public interface TradeService {

    BalanceDTO depositAccount(DepositRequest depositRequest);

    BalanceDTO withdrawalAccount(WithdrawalRequest withdrawalRequest);

    BalanceDTO transferAccount(TransferRequest transferRequest);

    Trade saveAndUpdate(Trade trade);

    <T> BalanceDTO processException(T requestClazz, int tradeResult);

    List<TradeDetailsDTO> getTransferList(TransferDetailsRequest detailsRequest);
}

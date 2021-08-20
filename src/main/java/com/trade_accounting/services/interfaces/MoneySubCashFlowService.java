package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.MoneySubCashFlowDto;
import com.trade_accounting.models.dto.PaymentDto;

import java.util.List;
import java.util.Map;

public interface MoneySubCashFlowService {

    List<MoneySubCashFlowDto> getAll();

    void update(MoneySubCashFlowDto moneySubCashFlowDto);

    List<MoneySubCashFlowDto> filter(Map<String, String> query);
}

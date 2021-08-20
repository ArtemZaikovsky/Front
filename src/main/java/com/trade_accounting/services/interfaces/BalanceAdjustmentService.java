package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.BalanceAdjustmentDto;
import retrofit2.Response;

import java.util.List;
import java.util.Map;

public interface BalanceAdjustmentService {

    List<BalanceAdjustmentDto> getAll();

    BalanceAdjustmentDto getById(Long id);

    List<BalanceAdjustmentDto> searchByString(String nameFilter);

    Response<BalanceAdjustmentDto> create(BalanceAdjustmentDto balanceAdjustmentDto);

    void update(BalanceAdjustmentDto balanceAdjustmentDto);

    void deleteById(Long id);

    List<BalanceAdjustmentDto> searchByFilter(Map<String, String> queryBalanceAdjustment);
}

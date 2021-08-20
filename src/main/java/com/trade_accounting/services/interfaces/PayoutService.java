package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.PayoutDto;

import java.util.List;

public interface PayoutService {

    List<PayoutDto> getAll();

    List<PayoutDto> getAllByParameters(String searchTerm);

    PayoutDto getById(Long id);

    void create(PayoutDto payoutDto);

    void update(PayoutDto payoutDto);

    void deleteById(Long id);
}

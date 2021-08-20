package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.PaymentDto;

import java.util.List;
import java.util.Map;

public interface PaymentService {

    List<PaymentDto> getAll();

    PaymentDto getById(Long id);

    void create(PaymentDto paymentDto);

    void update(PaymentDto paymentDto);

    void deleteById(Long id);

    List<PaymentDto> filter(Map<String, String> query);

    List<PaymentDto> search(String search);
}

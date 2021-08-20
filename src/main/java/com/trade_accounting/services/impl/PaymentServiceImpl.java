package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.PaymentDto;
import com.trade_accounting.services.interfaces.PaymentService;
import com.trade_accounting.services.interfaces.api.PaymentApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentApi paymentApi;

    private final String paymentUrl;

    private final CallExecuteService<PaymentDto> dtoCallExecuteService;
    public PaymentServiceImpl(@Value("${payment_url}") String paymentUrl, Retrofit retrofit, CallExecuteService<PaymentDto> dtoCallExecuteService) {
        paymentApi = retrofit.create(PaymentApi.class);
        this.paymentUrl = paymentUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<PaymentDto> getAll() {
        Call<List<PaymentDto>> paymentDtoListCall = paymentApi.getAll(paymentUrl);
        return dtoCallExecuteService.callExecuteBodyList(paymentDtoListCall, PaymentDto.class);
    }

    @Override
    public List<PaymentDto> filter(Map<String, String> filterData) {
        if (filterData.get("typeOfPayment") != null) {
            if (filterData.get("typeOfPayment").equals("Входящий")) {
                filterData.put("typeOfPayment", "INCOMING");
            } else {
                filterData.put("typeOfPayment", "OUTGOING");
            }
        }
        List<PaymentDto> paymentDtoList = new ArrayList<>();
        Call<List<PaymentDto>> paymentDtoListCall = paymentApi.filter(paymentUrl, filterData);

        try {
            paymentDtoList = paymentDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка PaymentDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка PaymentDto - ", e);
        }
        return paymentDtoList;
    }

    @Override
    public PaymentDto getById(Long id) {
        PaymentDto paymentDto = new PaymentDto();
        Call<PaymentDto> paymentDtoCall = paymentApi.getById(paymentUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(paymentDtoCall, paymentDto,PaymentDto.class, id);
    }

    @Override
    public void create(PaymentDto paymentDto) {
        Call<Void> paymentDtoCall = paymentApi.create(paymentUrl, paymentDto);
        dtoCallExecuteService.callExecuteBodyCreate(paymentDtoCall, PaymentDto.class);
    }

    @Override
    public void update(PaymentDto paymentDto) {
        Call<Void> paymentDtoCall = paymentApi.update(paymentUrl, paymentDto);
        dtoCallExecuteService.callExecuteBodyUpdate(paymentDtoCall, PaymentDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> paymentDtoCall = paymentApi.deleteById(paymentUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(paymentDtoCall, PaymentDto.class, id);
    }

    @Override
    public List<PaymentDto> search(String search) {
        List<PaymentDto> paymentDtoList = new ArrayList<>();
        Call<List<PaymentDto>> paymentDtoListCall = paymentApi.search(paymentUrl, search);

        try {
            paymentDtoList = paymentDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск PaymentDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск PaymentDto - {}", e);
        }
        return paymentDtoList;
    }
}

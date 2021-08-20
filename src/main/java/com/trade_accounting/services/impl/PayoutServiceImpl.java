package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.PayoutDto;
import com.trade_accounting.services.interfaces.PayoutService;
import com.trade_accounting.services.interfaces.api.PayoutApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PayoutServiceImpl implements PayoutService {

    private final PayoutApi payoutApi;

    private final String payoutURL;

    private PayoutDto payoutDto;

    private final CallExecuteService<PayoutDto> dtoCallExecuteService;

    public PayoutServiceImpl(@Value("${payout_url}") String payoutURL, Retrofit retrofit,
                             CallExecuteService<PayoutDto> dtoCallExecuteService) {
        payoutApi = retrofit.create(PayoutApi.class);
        this.payoutURL = payoutURL;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<PayoutDto> getAll() {
        Call<List<PayoutDto>> payoutDtoListCall = payoutApi.getAll(payoutURL);
        return dtoCallExecuteService.callExecuteBodyList(payoutDtoListCall, PayoutDto.class);
    }

    @Override
    public List<PayoutDto> getAllByParameters(String searchTerm) {
        List<PayoutDto> payoutDtoList = new ArrayList<>();
        Call<List<PayoutDto>> payoutDtoListCall = payoutApi.getAllByParameters(payoutURL, searchTerm);
        try {
            payoutDtoList = payoutDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка по запросу {}", searchTerm);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка по запросу {}", searchTerm);
        }
        return payoutDtoList;
    }

    @Override
    public PayoutDto getById(Long id) {
        Call<PayoutDto> payoutDtoCall = payoutApi.getById(payoutURL, id);
        return dtoCallExecuteService.callExecuteBodyById(payoutDtoCall, payoutDto, PayoutDto.class, id);
    }

    @Override
    public void create(PayoutDto payoutDto) {

        Call<Void> payoutDtoCall = payoutApi.create(payoutURL, payoutDto);

        try {
            payoutDtoCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра PayoutDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра PayoutDto - {}", e);
        }
    }

    @Override
    public void update(PayoutDto payoutDto) {

        Call<Void> payoutDtoCall = payoutApi.update(payoutURL, payoutDto);

        try {
            payoutDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра PayoutDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра PayoutDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {

        Call<Void> payoutDtoCall = payoutApi.deleteById(payoutURL, id);

        try {
            payoutDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра PayoutDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра PayoutDto по id= {} - {}", id, e);
        }
    }
}

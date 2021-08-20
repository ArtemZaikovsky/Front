package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.BalanceAdjustmentDto;
import com.trade_accounting.services.interfaces.BalanceAdjustmentService;
import com.trade_accounting.services.interfaces.api.BalanceAdjustmentApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BalanceAdjustmentServiceImpl implements BalanceAdjustmentService {

    private final BalanceAdjustmentApi balanceAdjustmentApi;
    private final String balanceAdjustmentUrl;

    public BalanceAdjustmentServiceImpl(@Value("${balance_adjustment_url}") String balanceAdjustmentUrl, Retrofit retrofit) {
        this.balanceAdjustmentApi = retrofit.create(BalanceAdjustmentApi.class);
        this.balanceAdjustmentUrl = balanceAdjustmentUrl;
    }

    @Override
    public List<BalanceAdjustmentDto> getAll() {
        List<BalanceAdjustmentDto> balanceAdjustmentDtoList = new ArrayList<>();
        Call<List<BalanceAdjustmentDto>> callListBalanceAdjustments = balanceAdjustmentApi.getAll(balanceAdjustmentUrl);
        try {
            balanceAdjustmentDtoList = callListBalanceAdjustments.execute().body();
            log.info("Успешно выполнен запрос на получение списка BalanceAdjustmentDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка BalanceAdjustmentDto - {IOException}", e);
        }
        return balanceAdjustmentDtoList;
    }

    @Override
    public BalanceAdjustmentDto getById(Long id) {
        BalanceAdjustmentDto balanceAdjustmentDto = new BalanceAdjustmentDto();
        Call<BalanceAdjustmentDto> balanceAdjustmentDtoCall = balanceAdjustmentApi.getById(balanceAdjustmentUrl, id);
        try {
            balanceAdjustmentDto = balanceAdjustmentDtoCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение корректировки баланса balanceAdjustment по его id {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение корректировки баланса balanceAdjustment" +
                    " по его id {} - {IOException}", id, e);
        }
        return balanceAdjustmentDto;
    }

    @Override
    public List<BalanceAdjustmentDto> searchByString(String nameFilter) {
        List<BalanceAdjustmentDto> balanceAdjustmentDtoList = new ArrayList<>();
        Call<List<BalanceAdjustmentDto>> callListBalanceAdjustments = balanceAdjustmentApi.searchByString(balanceAdjustmentUrl, nameFilter);
        try {
            balanceAdjustmentDtoList = callListBalanceAdjustments.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение корректировки баланса по фильтру {}", nameFilter);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение корректировки баланса {IOException}", e);
        }
        return balanceAdjustmentDtoList;
    }

    @Override
    public Response<BalanceAdjustmentDto> create(BalanceAdjustmentDto modelDto) {
        Response<BalanceAdjustmentDto> response = Response.success(new BalanceAdjustmentDto());
        Call<BalanceAdjustmentDto> balanceAdjustmentCall = balanceAdjustmentApi.create(balanceAdjustmentUrl, modelDto);
        try {
            response = balanceAdjustmentCall.execute();
            log.info("Успешно выполнен запрос на создание balanceAdjustment");
        } catch (IOException e) {
            log.error("Произошла ошибка при создании корректировки баланса {}", e);
        }
        return response;
    }

    @Override
    public void update(BalanceAdjustmentDto modelDto) {
        Call<Void> call = balanceAdjustmentApi.update(balanceAdjustmentUrl, modelDto);
        try {
            call.execute();
            log.info("Успешно выполнен запрос на обновление корректировки баланса {}", modelDto);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление корректировки баланса {}", modelDto);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> call = balanceAdjustmentApi.deleteById(balanceAdjustmentUrl, id);
        try {
            call.execute();
            log.info("Успешно выполнен запрос на удаление balanceAdjustment {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на удаление balanceAdjustment {} - {}", id, e);
        }
    }

    @Override
    public List<BalanceAdjustmentDto> searchByFilter(Map<String, String> queryBalanceAdjustment) {
        List<BalanceAdjustmentDto> balanceAdjustmentDtoList = new ArrayList<>();
        Call<List<BalanceAdjustmentDto>> callListBalanceAdjustments = balanceAdjustmentApi.searchByFilter(balanceAdjustmentUrl, queryBalanceAdjustment);
        try {
            balanceAdjustmentDtoList = callListBalanceAdjustments.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение корректировки баланса по фильтру {}", queryBalanceAdjustment);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение корректировки баланса {IOException}", e);
        }
        return balanceAdjustmentDtoList;
    }
}

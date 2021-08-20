package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RemainDto;
import com.trade_accounting.services.interfaces.RemainService;
import com.trade_accounting.services.interfaces.api.RemainApi;
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
public class RemainServiceImpl implements RemainService {

    private final RemainApi remainApi;
    private final String remainUrl;

    public RemainServiceImpl(@Value("@{remain_url}") String remainUrl, Retrofit retrofit) {
        remainApi = retrofit.create(RemainApi.class);
        this.remainUrl = remainUrl;
    }

    @Override
    public List<RemainDto> getAll() {
        List<RemainDto> remainDtoList = new ArrayList<>();
        Call<List<RemainDto>> remainDtoListCall = remainApi.getAll(remainUrl);

        try {
            remainDtoList.addAll(remainDtoListCall.execute().body());
            log.info("Успешно выполнен запрос на получение списка RemainDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка RemainDto - {}", e);
        }
        return remainDtoList;
    }

    @Override
    public RemainDto getById(Long id) {
        RemainDto remainDto = null;
        Call<RemainDto> remainDtoCall = remainApi.getBuId(remainUrl, id);

        try {
            remainDto = remainDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра RemainDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра RemainDto по id= {} - {}", id, e);
        }
        return remainDto;
    }

    @Override
    public void create(RemainDto remainDto) {
        Call<Void> remainDtoCall = remainApi.create(remainUrl, remainDto);

        try {
            remainDtoCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра RemainDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра ReaminDto - {}", e);
        }
    }

    @Override
    public void update(RemainDto remainDto) {
        Call<Void> remainDtoCall = remainApi.update(remainUrl, remainDto);

        try {
            remainDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра RemainDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра RemainDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> remainDtoCall = remainApi.deleteById(remainUrl, id);

        try {
            remainDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра RemainDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра RemainDto по id= {} - {}", id, e);
        }
    }
}

package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.PriceListDto;
import com.trade_accounting.services.interfaces.PriceListService;
import com.trade_accounting.services.interfaces.api.PriceListApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class PriceListServiceImpl implements PriceListService {

    private final PriceListApi priceListApi;
    private final String priceListUrl;
    private PriceListDto priceListDto;

    public PriceListServiceImpl(@Value("${price_list_url}") String priceListUrl, Retrofit retrofit) {
        this.priceListUrl = priceListUrl;
        priceListApi = retrofit.create(PriceListApi.class);
    }

    @Override
    public List<PriceListDto> getAll() {
        Call<List<PriceListDto>> priceListDtoListCall = priceListApi.getAll(priceListUrl);
        List<PriceListDto> priceListDtoList = null;
        try {
            priceListDtoList = priceListDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка экземпляров priceListDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка экземпляров PriceListDto");
        }
        return priceListDtoList;
    }

    @Override
    public PriceListDto getById(Long id) {
        Call<PriceListDto> priceListDtoCall = priceListApi.getById(priceListUrl, id);
        try {
            priceListDto = priceListDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра priceListDto по id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра PriceListDto по id= {} - {}",
                    id, e);
        }
        return priceListDto;
    }

    @Override
    public void create(PriceListDto priceListDto) {
        Call<Void> priceListDtoCall = priceListApi.create(priceListUrl, priceListDto);
        try {
            priceListDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание экземпляра PriceListDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра PriceListDto - {}", e);
        }
    }

    @Override
    public void update(PriceListDto priceListDto) {
        Call<Void> priceListDtoCall = priceListApi.update(priceListUrl, priceListDto);
        try {
            priceListDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра PriceListDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра PriceListDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> priceListDtoCall = priceListApi.deleteById(priceListUrl, id);
        try {
            priceListDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра PriceListDto с id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра PriceListDto с id= {} - {}", e);
        }
    }
}

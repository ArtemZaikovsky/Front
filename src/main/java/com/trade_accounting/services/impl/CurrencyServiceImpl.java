package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CurrencyDto;
import com.trade_accounting.services.interfaces.CurrencyService;
import com.trade_accounting.services.interfaces.api.CurrencyApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyApi currencyApi;

    private final String currencyUrl;

    private final CallExecuteService<CurrencyDto> dtoCallExecuteService;

    public CurrencyServiceImpl(@Value("${currency_url}") String currencyUrl, Retrofit retrofit, CallExecuteService<CurrencyDto> dtoCallExecuteService) {
        this.currencyUrl = currencyUrl;
        currencyApi = retrofit.create(CurrencyApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<CurrencyDto> getAll() {
        Call<List<CurrencyDto>> currencyDtoListCall = currencyApi.getAll(currencyUrl);
        return dtoCallExecuteService.callExecuteBodyList(currencyDtoListCall, CurrencyDto.class);
    }
    @Override
    public List<CurrencyDto> search(Map<String, String> query) {
        List<CurrencyDto> currencyDtoList = new ArrayList<>();
        Call<List<CurrencyDto>> currencyDtoListCall = currencyApi.search(currencyUrl, query);

        try {
            currencyDtoList = currencyDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка CurrencyDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка CurrencyDto - ", e);
        }
        return currencyDtoList;
    }

    @Override
    public CurrencyDto getById(Long id) {
        CurrencyDto currencyDto = new CurrencyDto();
        Call<CurrencyDto> currencyDtoCall = currencyApi.getById(currencyUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(currencyDtoCall, currencyDto, CurrencyDto.class, id);
    }

    @Override
    public void create(CurrencyDto currencyDto) {
        Call<Void> currencyDtoCall = currencyApi.create(currencyUrl, currencyDto);
        dtoCallExecuteService.callExecuteBodyCreate(currencyDtoCall, CurrencyDto.class);
    }

    @Override
    public void update(CurrencyDto currencyDto) {
        Call<Void> currencyDtoList = currencyApi.update(currencyUrl, currencyDto);
        dtoCallExecuteService.callExecuteBodyUpdate(currencyDtoList, CurrencyDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> currencyDtoCall = currencyApi.deleteById(currencyUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(currencyDtoCall, CurrencyDto.class, id);


    }
}

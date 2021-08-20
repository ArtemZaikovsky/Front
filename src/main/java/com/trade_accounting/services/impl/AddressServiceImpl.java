package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.AddressDto;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.AddressService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.api.AddressApi;
import com.trade_accounting.services.interfaces.api.CompanyApi;
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

@Slf4j
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressApi addressApi;
    private final String addressUrl;
    private AddressDto addressDto;

    public AddressServiceImpl(@Value("${address_url}") String addressUrl, Retrofit retrofit) {
        this.addressUrl = addressUrl;
        addressApi = retrofit.create(AddressApi.class);
    }



    @Override
    public AddressDto getById(Long id) {
        Call<AddressDto> addressDtoCall = addressApi.getById(addressUrl, id);
        try {
            addressDto = addressDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра AddressDto по id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра AddressDto по id= {} - {}",
                    id, e);
        }
        return addressDto;
    }

    @Override
    public AddressDto create(AddressDto addressDto) {
        Call<AddressDto> addressDtoCall = addressApi.create(addressUrl, addressDto);
        try {
            this.addressDto=addressDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание экземпляра AddressDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра AddressDto - {}", e);
        }
        return this.addressDto;
    }

    @Override
    public void update(AddressDto addressDto) {
        Call<Void> addressDtoCall = addressApi.update(addressUrl, addressDto);
        try {
            addressDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра AddressDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра AddressDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> addressDtoCall = addressApi.deleteById(addressUrl, id);

        try {
            addressDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра AddressDto с id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра AddressDto с id= {} - {}", e);
        }
    }
}

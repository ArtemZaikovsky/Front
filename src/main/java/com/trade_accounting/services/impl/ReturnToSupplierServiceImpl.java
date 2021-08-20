package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ReturnToSupplierDto;
import com.trade_accounting.services.interfaces.ReturnToSupplierService;
import com.trade_accounting.services.interfaces.api.ReturnToSupplierApi;
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
public class ReturnToSupplierServiceImpl implements ReturnToSupplierService {

    private final ReturnToSupplierApi returnToSupplierApi;
    private final String returnToSupplierUrl;

    public ReturnToSupplierServiceImpl(@Value("${return_supplier_url}") String returnToSupplierUrl, Retrofit retrofit) {
        this.returnToSupplierApi = retrofit.create(ReturnToSupplierApi.class);
        this.returnToSupplierUrl = returnToSupplierUrl;
    }

    @Override
    public List<ReturnToSupplierDto> getAll() {
        List<ReturnToSupplierDto> returnToSupplierDtoList = new ArrayList<>();
        Call<List<ReturnToSupplierDto>> callListReturnSuppliers = returnToSupplierApi.getAll(returnToSupplierUrl);
        try {
            returnToSupplierDtoList = callListReturnSuppliers.execute().body();
            log.info("Успешно выполнен запрос на получение списка ReturnToSupplierDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка ReturnToSupplierDto - {IOException}", e);
        }
        return returnToSupplierDtoList;
    }

    @Override
    public ReturnToSupplierDto getById(Long id) {
        ReturnToSupplierDto returnToSupplierDto = new ReturnToSupplierDto();
        Call<ReturnToSupplierDto> returnToSupplierDtoCall = returnToSupplierApi.getById(returnToSupplierUrl, id);
        try {
            returnToSupplierDto = returnToSupplierDtoCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение возврата поставщику returnToSupplier по его id {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение возврата поставщику returnToSupplier" +
                    " по его id {} - {IOException}", id, e);
        }
        return returnToSupplierDto;
    }

    @Override
    public List<ReturnToSupplierDto> searchByString(String nameFilter) {
        List<ReturnToSupplierDto> returnToSupplierDtoList = new ArrayList<>();
        Call<List<ReturnToSupplierDto>> callListReturnSuppliers = returnToSupplierApi.searchByString(returnToSupplierUrl, nameFilter);
        try {
            returnToSupplierDtoList = callListReturnSuppliers.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение возврата поставщику по фильтру {}", nameFilter);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение возврата поставщику {IOException}", e);
        }
        return returnToSupplierDtoList;
    }

    @Override
    public Response<ReturnToSupplierDto> create(ReturnToSupplierDto modelDto) {
        Response<ReturnToSupplierDto> response = Response.success(new ReturnToSupplierDto());
        Call<ReturnToSupplierDto> returnToSupplierCall = returnToSupplierApi.create(returnToSupplierUrl, modelDto);
        try {
            response = returnToSupplierCall.execute();
            log.info("Успешно выполнен запрос на создание returnToSupplier");
        } catch (IOException e) {
            log.error("Произошла ошибка при создании вовзрата поставщику {}", e);
        }
        return response;
    }

    @Override
    public void update(ReturnToSupplierDto modelDto) {
        Call<Void> call = returnToSupplierApi.update(returnToSupplierUrl, modelDto);
        try {
            call.execute();
            log.info("Успешно выполнен запрос на обновление возврата поставщику {}", modelDto);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление возврата поставщику {}", modelDto);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> call = returnToSupplierApi.deleteById(returnToSupplierUrl, id);
        try {
            call.execute();
            log.info("Успешно выполнен запрос на удаление returnToSupplier {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на удаление returnToSupplier {} - {}", id, e);
        }
    }

    @Override
    public List<ReturnToSupplierDto> searchByFilter(Map<String, String> queryReturnToSupplier) {
        List<ReturnToSupplierDto> returnToSupplierDtoList = new ArrayList<>();
        Call<List<ReturnToSupplierDto>> callListReturnSuppliers = returnToSupplierApi.searchByFilter(returnToSupplierUrl, queryReturnToSupplier);
        try {
            returnToSupplierDtoList = callListReturnSuppliers.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение возврата поставщикам по фильтру {}", queryReturnToSupplier);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск иполучение возврата поставщикам {IOException}", e);
        }
        return returnToSupplierDtoList;
    }
}

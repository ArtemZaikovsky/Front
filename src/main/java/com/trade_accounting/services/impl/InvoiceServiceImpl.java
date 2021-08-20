package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.trade_accounting.services.interfaces.api.InvoiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceApi invoiceApi;

    private final String invoiceUrl;

    private InvoiceDto invoiceDto;

    private final CallExecuteService<InvoiceDto> dtoCallExecuteService;

    public InvoiceServiceImpl(@Value("${invoice_url}") String invoiceUrl, Retrofit retrofit, CallExecuteService<InvoiceDto> dtoCallExecuteService) {
        invoiceApi = retrofit.create(InvoiceApi.class);
        this.invoiceUrl = invoiceUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<InvoiceDto> getAll() {
        Call<List<InvoiceDto>> invoiceDtoListCall = invoiceApi.getAll(invoiceUrl);
        return dtoCallExecuteService.callExecuteBodyList(invoiceDtoListCall, InvoiceDto.class);
    }

    @Override
    public List<InvoiceDto> getAll(String typeOfInvoice) {
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();
        Call<List<InvoiceDto>> invoiceDtoListCall = invoiceApi.getAll(invoiceUrl, typeOfInvoice);

        try {
            invoiceDtoList.addAll(Objects.requireNonNull(invoiceDtoListCall.execute().body()));
            log.info("Успешно выполнен запрос на получение списка InvoiceDto");
        } catch (IOException | NullPointerException e) {
            log.error("Попытка перехода на страницу /purchases  не авторизованного пользователя  - {NullPointerException}", e);
            log.error("Произошла ошибка при выполнении запроса на получение списка InvoiceDto - {IOException}", e);
        }
        return invoiceDtoList;
    }

    @Override
    public InvoiceDto getById(Long id) {
        Call<InvoiceDto> invoiceDtoCall = invoiceApi.getById(invoiceUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(invoiceDtoCall, invoiceDto, InvoiceDto.class, id);
    }

    @Override
    public List<InvoiceDto> search(Map<String, String> query) {
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();
        Call<List<InvoiceDto>> invoiceDtoListCall = invoiceApi.search(invoiceUrl, query);
        try {
            invoiceDtoList = invoiceDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка счетов invoice {}", query);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка InvoiceDto - ", e);
        }
        return invoiceDtoList;
    }

    @Override
    public List<InvoiceDto> findBySearchAndTypeOfInvoice(String search, String typeOfInvoice) {
        List<InvoiceDto> invoiceDtoList = new ArrayList<>();
        Call<List<InvoiceDto>> invoiceDtoListCall = invoiceApi
                .search(invoiceUrl, search.toLowerCase(), typeOfInvoice);

        try {
            invoiceDtoList = invoiceDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка счетов invoice");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка InvoiceDto - ", e);
        }
        return invoiceDtoList;
    }

    @Override
    public Response<InvoiceDto> create(InvoiceDto invoiceDto) {

        Call<InvoiceDto> invoiceDtoCall = invoiceApi.create(invoiceUrl, invoiceDto);
        Response<InvoiceDto> resp = Response.success(new InvoiceDto());

        try {
            resp = invoiceDtoCall.execute();
            log.info("Успешно выполнен запрос на создание Invoice");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение ProductDto - {}", e);
        }

        return resp;
    }

    @Override
    public void update(InvoiceDto invoiceDto) {
        Call<Void> invoiceDtoCall = invoiceApi.update(invoiceUrl, invoiceDto);
        dtoCallExecuteService.callExecuteBodyUpdate(invoiceDtoCall, InvoiceDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> invoiceDtoCall = invoiceApi.deleteById(invoiceUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(invoiceDtoCall, InvoiceDto.class, id);
    }
}

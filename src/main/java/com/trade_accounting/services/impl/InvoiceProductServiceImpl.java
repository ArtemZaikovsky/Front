package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.api.InvoiceProductApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductApi invoiceProductApi;

    private final String invoiceProductUrl;

    private final CallExecuteService<InvoiceProductDto> dtoCallExecuteService;

    public InvoiceProductServiceImpl(@Value("${invoice_product_url}") String invoiceProductUrl, Retrofit retrofit, CallExecuteService<InvoiceProductDto> dtoCallExecuteService){
        invoiceProductApi = retrofit.create(InvoiceProductApi.class);
        this.invoiceProductUrl = invoiceProductUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<InvoiceProductDto> getAll() {
        Call<List<InvoiceProductDto>> invoiceProductDtoListCall = invoiceProductApi.getAll(invoiceProductUrl);
        return dtoCallExecuteService.callExecuteBodyList(invoiceProductDtoListCall, InvoiceProductDto.class);
    }

    @Override
    public InvoiceProductDto getById(Long id) {
        InvoiceProductDto invoiceProductDto = new InvoiceProductDto();
        Call<InvoiceProductDto> invoiceProductDtoCall = invoiceProductApi.getById(invoiceProductUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(invoiceProductDtoCall, invoiceProductDto, InvoiceProductDto.class, id);
    }

    @Override
    public List<InvoiceProductDto> getByInvoiceId(Long id) {
        List<InvoiceProductDto> invoiceProductDtoList = null;
        Call<List<InvoiceProductDto>> invoiceProductDtoCall = invoiceProductApi.getByInvoiceId(invoiceProductUrl + "/invoice_product", id);
        try{
            invoiceProductDtoList = invoiceProductDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка InvoiceProductDto с Invoice.id = {}", id);
        } catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса на получение списка InvoiceProductDto по id= {} - {}", id, e);
        }
        return invoiceProductDtoList;
    }

    @Override
    public void create(InvoiceProductDto invoiceProductDto) {
        Call<Void> invoiceProductDtoCall = invoiceProductApi.create(invoiceProductUrl, invoiceProductDto);
        dtoCallExecuteService.callExecuteBodyCreate(invoiceProductDtoCall, InvoiceProductDto.class);
    }

    @Override
    public void update(InvoiceProductDto invoiceProductDto) {
        Call<Void> invoiceProductDtoCall = invoiceProductApi.create(invoiceProductUrl, invoiceProductDto);
        dtoCallExecuteService.callExecuteBodyUpdate(invoiceProductDtoCall, InvoiceProductDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> invoiceProductDtoCall = invoiceProductApi.deleteById(invoiceProductUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(invoiceProductDtoCall,InvoiceProductDto.class, id);
    }
}

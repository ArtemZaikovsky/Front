package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CorrectionDto;
import com.trade_accounting.models.dto.InternalOrderDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.services.interfaces.InternalOrderService;
import com.trade_accounting.services.interfaces.api.CorrectionApi;
import com.trade_accounting.services.interfaces.api.InternalOrderApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class InternalOrderServiceImpl implements InternalOrderService {
    private final InternalOrderApi internalOrderApi;
    private final String internalOrderUrl;
    private final CallExecuteService<InternalOrderDto> callExecuteService;
    private InternalOrderDto internalOrderDto;

    public InternalOrderServiceImpl(Retrofit retrofit,
                                    @Value("${internal_order_url}") String internalOrderUrl,
                                    CallExecuteService<InternalOrderDto> callExecuteService) {
        this.internalOrderApi = retrofit.create(InternalOrderApi.class);
        this.internalOrderUrl = internalOrderUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<InternalOrderDto> getAll() {
        Call<List<InternalOrderDto>> internalDtoListCall = internalOrderApi.getAll(internalOrderUrl);
        return callExecuteService.callExecuteBodyList(internalDtoListCall, InternalOrderDto.class);
    }

    @Override
    public InternalOrderDto getById(Long id) {
        Call<InternalOrderDto> internalDtoListCall = internalOrderApi.getById(internalOrderUrl, id);
        return callExecuteService.callExecuteBodyById(internalDtoListCall, new InternalOrderDto(), InternalOrderDto.class, id);
    }

    @Override
    public InternalOrderDto create(InternalOrderDto internalOrderDto) {
        Call<InternalOrderDto> internalDtoCall = internalOrderApi.create(internalOrderUrl, internalOrderDto);

        try {
            internalOrderDto = internalDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание InternalOrder");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение InternalOrderDto - {}", e);
        }

        return internalOrderDto;
    }

    @Override
    public void update(InternalOrderDto internalOrderDto) {
        Call<Void> internalOrderDtoCall = internalOrderApi.update(internalOrderUrl, internalOrderDto);
        try {
            internalOrderDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра InternalOrder");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра InternalOrderDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> internalOrderDtoCall = internalOrderApi.deleteById(internalOrderUrl, id);

        try {
            internalOrderDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра InternalOrderDto с id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра InternalOrderDto с id= {} - {}", e);
        }
    }
}

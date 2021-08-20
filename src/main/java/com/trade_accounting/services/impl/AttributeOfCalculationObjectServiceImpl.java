package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.AttributeOfCalculationObjectDto;
import com.trade_accounting.services.interfaces.AttributeOfCalculationObjectService;
import com.trade_accounting.services.interfaces.api.AttributeOfCalculationObjectApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;


@Slf4j
@Service
public class AttributeOfCalculationObjectServiceImpl implements AttributeOfCalculationObjectService {

    private final AttributeOfCalculationObjectApi attributeOfCalculationObjectApi;

    private final String attributeOfCalculationObjectUrl;

    private final CallExecuteService<AttributeOfCalculationObjectDto> dtoCallExecuteService;

    @Autowired
    public AttributeOfCalculationObjectServiceImpl(@Value("${attribute_calculation_object_url}") String attributeOfCalculationObjectUrl, Retrofit retrofit, CallExecuteService<AttributeOfCalculationObjectDto> dtoCallExecuteService) {
        attributeOfCalculationObjectApi = retrofit.create(AttributeOfCalculationObjectApi.class);
        this.attributeOfCalculationObjectUrl = attributeOfCalculationObjectUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<AttributeOfCalculationObjectDto> getAll() {

        Call<List<AttributeOfCalculationObjectDto>> attributeOfCalculationObjectDtoListCall = attributeOfCalculationObjectApi.getAll(attributeOfCalculationObjectUrl);
        return dtoCallExecuteService.callExecuteBodyList(attributeOfCalculationObjectDtoListCall, AttributeOfCalculationObjectDto.class);

    }

    @Override
    public AttributeOfCalculationObjectDto getById(Long id) {
        AttributeOfCalculationObjectDto attributeOfCalculationObjectDto = new AttributeOfCalculationObjectDto();
        Call<AttributeOfCalculationObjectDto> attributeOfCalculationObjectDtoCall = attributeOfCalculationObjectApi.getById(attributeOfCalculationObjectUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(attributeOfCalculationObjectDtoCall,attributeOfCalculationObjectDto, AttributeOfCalculationObjectDto.class, id);
    }

    @Override
    public void create(AttributeOfCalculationObjectDto attributeOfCalculationObjectDto) {
        Call<Void> attributeOfCalculationObjectDtoCall = attributeOfCalculationObjectApi.create(attributeOfCalculationObjectUrl, attributeOfCalculationObjectDto);
        dtoCallExecuteService.callExecuteBodyCreate(attributeOfCalculationObjectDtoCall, AttributeOfCalculationObjectDto.class);

    }

    @Override
    public void update(AttributeOfCalculationObjectDto attributeOfCalculationObjectDto) {
        Call<Void> attributeOfCalculationObjectDtoCall = attributeOfCalculationObjectApi.update(attributeOfCalculationObjectUrl, attributeOfCalculationObjectDto);
        dtoCallExecuteService.callExecuteBodyUpdate(attributeOfCalculationObjectDtoCall, AttributeOfCalculationObjectDto.class);

    }

    @Override
    public void deleteById(Long id) {
        Call<Void> attributeOfCalculationObjectDtoCall = attributeOfCalculationObjectApi.deleteById(attributeOfCalculationObjectUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(attributeOfCalculationObjectDtoCall, AttributeOfCalculationObjectDto.class, id);

    }

}

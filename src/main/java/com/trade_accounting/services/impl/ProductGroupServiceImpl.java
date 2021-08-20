package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ProductGroupDto;
import com.trade_accounting.services.interfaces.ProductGroupService;
import com.trade_accounting.services.interfaces.api.ProductGroupApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;


@Slf4j
@Service
public class ProductGroupServiceImpl implements ProductGroupService {

    private final String productGroupUrl;

    private final ProductGroupApi productGroupApi;

    private ProductGroupDto productGroupDto;

    private final CallExecuteService<ProductGroupDto> dtoCallExecuteService;

    public ProductGroupServiceImpl(@Value("${product_group_url}") String productGroupUrl, Retrofit retrofit, CallExecuteService<ProductGroupDto> dtoCallExecuteService) {
        this.productGroupUrl = productGroupUrl;
        this.productGroupApi = retrofit.create(ProductGroupApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<ProductGroupDto> getAll() {
        Call<List<ProductGroupDto>> productGroupDtoListCall = productGroupApi.getAll(productGroupUrl);
        return dtoCallExecuteService.callExecuteBodyList(productGroupDtoListCall, ProductGroupDto.class);
    }

    @Override
    public ProductGroupDto getById(Long id) {
        Call<ProductGroupDto> productGroupDtoCall = productGroupApi.getById(productGroupUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(productGroupDtoCall, productGroupDto, ProductGroupDto.class, id);
    }

    @Override
    public void create(ProductGroupDto dto) {
        Call<Void> productGroupDtoCall = productGroupApi.create(productGroupUrl, productGroupDto);
        dtoCallExecuteService.callExecuteBodyCreate(productGroupDtoCall, ProductGroupDto.class);
    }

    @Override
    public void update(ProductGroupDto dto) {
        Call<Void> productGroupDtoCall = productGroupApi.update(productGroupUrl, productGroupDto);
        dtoCallExecuteService.callExecuteBodyUpdate(productGroupDtoCall, ProductGroupDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> productGroupDtoCall = productGroupApi.deleteById(productGroupUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(productGroupDtoCall, ProductGroupDto.class, id);
    }
}

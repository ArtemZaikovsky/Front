package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.PageDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.ProductGroupDto;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.api.ProductApi;
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

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final String productUrl;

    private final ProductApi productApi;

    private final CallExecuteService<ProductDto> dtoCallExecuteService;


    ProductServiceImpl(@Value("${product_url}") String productUrl, Retrofit retrofit, CallExecuteService<ProductDto> dtoCallExecuteService) {
        this.productUrl = productUrl;
        productApi = retrofit.create(ProductApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<ProductDto> getAll() {
        Call<List<ProductDto>> productGetAllLiteCall = productApi.getAll(productUrl);
        return dtoCallExecuteService.callExecuteBodyList(productGetAllLiteCall, ProductDto.class);
    }

    @Override
    public ProductDto getById(Long id) {
        Call<ProductDto> productGetCall = productApi.getById(productUrl, id);
        ProductDto productDto = new ProductDto();
        return dtoCallExecuteService.callExecuteBodyById(productGetCall, productDto, ProductDto.class, id);
    }

    @Override
    public void create(ProductDto productDto) {
        Call<Void> productCall = productApi.create(productUrl, productDto);
        dtoCallExecuteService.callExecuteBodyCreate(productCall, ProductDto.class);
    }

    @Override
    public void update(ProductDto productDto) {
        Call<Void> productUpdateCall = productApi.update(productUrl, productDto);
        dtoCallExecuteService.callExecuteBodyUpdate(productUpdateCall, ProductDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> productDeleteCall = productApi.deleteById(productUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(productDeleteCall, ProductDto.class, id);
    }

    @Override
    public List<ProductDto> getAllByProductGroup(ProductGroupDto productGroupDto) {
        Call<List<ProductDto>> productGetAllCall = productApi.getAllByProductGroupId(productUrl, productGroupDto.getId());
        List<ProductDto> productDtos = new ArrayList<>();
        try {
            productDtos = productGetAllCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка ProductDto принадлежащих группе {}", productGroupDto.getName());
        } catch (IOException e) {
            log.error("Произошла ошибка при получении списка ProductDto принадлежащих группе {} - {}", productGroupDto.getName(), e.getMessage());
        }
        return productDtos;
    }

    @Override
    public List<ProductDto> search(String query) {
        List<ProductDto> productDtoList = new ArrayList<>();
        Call<List<ProductDto>> productDtoListCall = productApi.search(productUrl, query);
        try {
            productDtoList = productDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка ProductDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка ProductDto - ", e);
        }
        return productDtoList;
    }

    @Override
    public List<ProductDto> searchByFilter(Map<String, String> query) {
        List<ProductDto> productDtoList = new ArrayList<>();
        Call<List<ProductDto>> productDtoListCall = productApi.searchByFilter(productUrl, query);
        try {
            productDtoList = productDtoListCall.execute().body();
            log.info("Успешно выполнен запрос ФИЛЬТРА на поиск и получение списка ProductDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса ФИЛЬТРА на поиск и получение списка ProductDto - ", e);
        }
        return productDtoList;
    }

    @Override
    public PageDto<ProductDto> getPage(Map<String, String> filterParams, Map<String, String> sortParams, int page, int count) {
        PageDto<ProductDto> productDtoList = new PageDto<>();

        Call<PageDto<ProductDto>> productDtoListCall = productApi.getPage(productUrl, sortParams, filterParams, page, count);

        try {
            productDtoList = productDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение страницы ProductDto по фильтру");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение страницы ProductDto - ", e);
        }
        return productDtoList;
    }
}

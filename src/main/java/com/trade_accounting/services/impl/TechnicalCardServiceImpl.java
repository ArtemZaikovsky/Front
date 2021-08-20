package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.TechnicalCardDto;
import com.trade_accounting.services.interfaces.TechnicalCardService;
import com.trade_accounting.services.interfaces.api.TechnicalCardApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TechnicalCardServiceImpl implements TechnicalCardService {

    private final String technicalCardUrl;

    private final TechnicalCardApi technicalCardApi;

    private List<TechnicalCardDto> technicalCardDtoList = new ArrayList<>();

    private TechnicalCardDto technicalCardDto = new TechnicalCardDto();

    private final CallExecuteService<TechnicalCardDto> dtoCallExecuteService;

    public TechnicalCardServiceImpl(@Value("${technical_card_url}") String technicalCardUrl, Retrofit retrofit, CallExecuteService<TechnicalCardDto> dtoCallExecuteService) {
        this.technicalCardUrl = technicalCardUrl;
        technicalCardApi = retrofit.create(TechnicalCardApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<TechnicalCardDto> searchTechnicalCard(Map<String, String> queryTechnicalCard) {
        List<TechnicalCardDto> technicalCardDtoList = new ArrayList<>();
        Call<List<TechnicalCardDto>> technicalCardDtoListCall = technicalCardApi.searchContractor(technicalCardUrl, queryTechnicalCard);
        try {
            technicalCardDtoList = technicalCardDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка технических карт");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка технических карт: {IOException}", e);
        }
        return technicalCardDtoList;
    }

    @Override
    public List<TechnicalCardDto> getAll() {
        Call<List<TechnicalCardDto>> technicalCardGetAll = technicalCardApi.getAll(technicalCardUrl);
        return dtoCallExecuteService.callExecuteBodyList(technicalCardGetAll, TechnicalCardDto.class);
    }

    @Override
    public TechnicalCardDto getById(Long id) {
        Call<TechnicalCardDto> technicalCardDtoGetCall = technicalCardApi.getById(technicalCardUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(technicalCardDtoGetCall, technicalCardDto, TechnicalCardDto.class, id);
    }

    @Override
    public void create(TechnicalCardDto technicalCardDto) {
        Call<Void> technicalCardCreateCall = technicalCardApi.create(technicalCardUrl, technicalCardDto);
        dtoCallExecuteService.callExecuteBodyCreate(technicalCardCreateCall,TechnicalCardDto.class);
    }

    @Override
    public void update(TechnicalCardDto technicalCardDto) {
        Call<Void> technicalCardUpdateCall = technicalCardApi.update(technicalCardUrl, technicalCardDto);
        dtoCallExecuteService.callExecuteBodyUpdate(technicalCardUpdateCall, TechnicalCardDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> technicalCardDeleteCall = technicalCardApi.deleteById(technicalCardUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(technicalCardDeleteCall, TechnicalCardDto.class, id);
    }

    @Override
    public List<TechnicalCardDto> getAllByTechnicalCardGroupId(Long id) {
        Call<List<TechnicalCardDto>> technicalCardGetAllCall = technicalCardApi.getAllByTechnicalCardGroupId(technicalCardUrl, id);
        try {
            technicalCardDtoList = technicalCardGetAllCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка TechnicalCardDto принадлежащих группе с id {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при получении списка TechnicalCardDto принадлежащих группе с id {} - {}", id, e.getMessage());
        }
        return technicalCardDtoList;
    }

    @Override
    public List<TechnicalCardDto> search(String query) {
        Call<List<TechnicalCardDto>> technicalCardDtoListCall = technicalCardApi.search(technicalCardUrl, query);
        try {
            technicalCardDtoList = technicalCardDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка TechnicalCardDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка TechnicalCardDto - ", e);
        }
        return technicalCardDtoList;
    }

}

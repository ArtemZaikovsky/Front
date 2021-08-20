package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.PositionDto;
import com.trade_accounting.services.interfaces.PositionService;
import com.trade_accounting.services.interfaces.api.PositionApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class PositionServiceImpl implements PositionService {

    private final PositionApi positionApi;

    private final String positionUrl;

    private List<PositionDto> positionDtoList;

    private PositionDto positionDto;

    public PositionServiceImpl(@Value("${position_url}") String positionUrl, Retrofit retrofit) {
        this.positionUrl = positionUrl;
        positionApi = retrofit.create(PositionApi.class);
    }

    @Override
    public List<PositionDto> getAll() {

        Call<List<PositionDto>> positionDtoListCall = positionApi.getAll(positionUrl);
        Response<List<PositionDto>> response = null;

        try {
            response = positionDtoListCall.execute();
            if (response.isSuccessful()) {
                positionDtoList = response.body();
                log.info("Успешно выполнен запрос на получение списка PositionDto");
            } else {
                log.error("Произошла ошибка при выполнении запроса на получение списка PositionDto - {}", response.errorBody());
            }


        } catch (Exception e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка PositionDto");
        }
        return positionDtoList;
    }

    @Override
    public PositionDto getById(Long id) {
        Call<PositionDto> positionDtoCall = positionApi.getById(positionUrl, id);
        Response<PositionDto> response = null;

        try {
            response = positionDtoCall.execute();
            if (response.isSuccessful()) {
                positionDto = response.body();
                log.info("Успешно выполнен запрос на получение экземпляра PositionDto по id= {}", id);
            } else {
                log.error("Произошла ошибка при выполнении запроса на получение экземпляра PositionDto по id= {} - {}",
                        id, response.errorBody());
            }

        } catch (Exception e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра PositionDto по id");
        }
        return positionDto;
    }

    @Override
    public void create(PositionDto positionDto) {

        Call<Void> positionDtoCall = positionApi.create(positionUrl, positionDto);
        Response<Void> response = null;
        try {
            response = positionDtoCall.execute();
            if (response.isSuccessful()) {
                log.info("Успешно выполнен запрос на создание экземпляра PositionDto");
            } else {
                log.error("Произошла ошибка при выполнении запроса на создание экземпляра PositionDto - {}",
                        response.errorBody());
            }
            log.info("Успешно выполнен запрос на создание экземпляра PositionDto");

        } catch (Exception e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра PositionDto");
        }
    }

    @Override
    public void update(PositionDto positionDto) {

        Call<Void> positionDtoCall = positionApi.update(positionUrl, positionDto);
        Response<Void> response = null;

        try {
            response = positionDtoCall.execute();
            if (response.isSuccessful()) {
                log.info("Успешно выполнен запрос на обновление экземпляра PositionDto");
            } else {
                log.error("Произошла ошибка при выполнении запроса на обновление экземпляра PositionDto - {}",
                        response.errorBody());
            }
        } catch (Exception e) {
            log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра PositionDto");
        }
    }

    @Override
    public void deleteById(Long id) {

        Call<Void> positionDtoCall = positionApi.deleteById(positionUrl, id);
        Response<Void> response = null;

        try {
            response = positionDtoCall.execute();
            if (response.isSuccessful()) {
                log.info("Успешно выполнен запрос на удаление экземпляра PositionDto с id= {}", id);
            } else {
                log.error("Произошла ошибка при выполнении запроса на удаление экземпляра PositionDto с id= {} - {}",
                        id, response.errorBody());
            }
        } catch (Exception e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра PositionDto");
        }
    }
}

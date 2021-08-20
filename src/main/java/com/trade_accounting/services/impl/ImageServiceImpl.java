package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ImageDto;
import com.trade_accounting.services.interfaces.ImageService;
import com.trade_accounting.services.interfaces.api.ImageApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageApi imageApi;

    private final String imageUrl;

    private ImageDto imageDto;

    private final CallExecuteService<ImageDto> dtoCallExecuteService;


    public ImageServiceImpl(@Value("${image_url}") String imageUrl, Retrofit retrofit, CallExecuteService<ImageDto> dtoCallExecuteService) {
        imageApi = retrofit.create(ImageApi.class);
        this.imageUrl = imageUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<ImageDto> getAll() {
        Call<List<ImageDto>> imageDtoListCall = imageApi.getAll(imageUrl);
        return dtoCallExecuteService.callExecuteBodyList(imageDtoListCall, ImageDto.class);
    }

    @Override
    public ImageDto getById(Long id) {
        Call<ImageDto> imageDtoCall = imageApi.getById(imageUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(imageDtoCall, imageDto, ImageDto.class, id);
    }

    @Override
    public void create(ImageDto imageDto) {
        Call<Void> imageDtoCall = imageApi.create(imageUrl, imageDto);
        dtoCallExecuteService.callExecuteBodyCreate(imageDtoCall, ImageDto.class);
    }

    @Override
    public void update(ImageDto imageDto) {
        Call<Void> imageDtoCall = imageApi.update(imageUrl, imageDto);
        dtoCallExecuteService.callExecuteBodyUpdate(imageDtoCall, ImageDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> imageDtoCall = imageApi.deleteById(imageUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(imageDtoCall, ImageDto.class, id);
    }
}
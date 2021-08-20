package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.ImageDto;
import com.vaadin.flow.component.html.Image;

import java.util.List;

public interface ImageService {

    List<ImageDto> getAll();

    ImageDto getById(Long id);

    void create(ImageDto imageDto);

    void update(ImageDto imageDto);

    void deleteById(Long id);
}
package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.PageDto;

import java.util.Map;

public interface PageableService<T> {
    PageDto<T> getPage(Map<String,String> filterParams, Map<String, String> sortParams, int page, int count);
}

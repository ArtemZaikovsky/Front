package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.ReturnToSupplierDto;
import retrofit2.Response;

import java.util.List;
import java.util.Map;

public interface ReturnToSupplierService {

    List<ReturnToSupplierDto> getAll();

    ReturnToSupplierDto getById(Long id);

    List<ReturnToSupplierDto> searchByString(String nameFilter);

    Response<ReturnToSupplierDto> create(ReturnToSupplierDto returnToSupplierDto);

    void update(ReturnToSupplierDto returnToSupplierDto);

    void deleteById(Long id);

    List<ReturnToSupplierDto> searchByFilter(Map<String, String> queryReturnToSupplier);

}

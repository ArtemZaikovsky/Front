package com.trade_accounting.services.interfaces;


import com.trade_accounting.models.dto.InvoiceProductDto;

import java.util.List;

public interface InvoiceProductService {
    List<InvoiceProductDto> getAll();

    InvoiceProductDto getById(Long id);

    List<InvoiceProductDto> getByInvoiceId(Long id);

    void create(InvoiceProductDto invoiceProductDto);

    void update(InvoiceProductDto invoiceProductDto);

    void deleteById(Long id);
}

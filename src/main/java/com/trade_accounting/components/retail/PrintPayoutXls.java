package com.trade_accounting.components.retail;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.PayoutDto;
import com.trade_accounting.services.interfaces.EmployeeService;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDateTime;
import java.util.List;

public class PrintPayoutXls extends PrintExcelDocument<PayoutDto> {

    protected List<String> sum;
    private int lengthOfSumList = 0;
    private final EmployeeService employeeService;

    protected PrintPayoutXls(String pathToXlsTemplate, List<PayoutDto> list,
                             List<String> sum, EmployeeService employeeService) {
        super(pathToXlsTemplate, list);
        this.sum = sum;
        this.employeeService = employeeService;
    }

    @Override
    protected void selectValue(Cell editCell) {
        String formula = editCell.getStringCellValue();
        switch (formula) {
            case ("<date>"):
                editCell.setCellValue(LocalDateTime.now());
                break;
            case ("<authorName>"):
                editCell.setCellValue(employeeService.getPrincipal().getEmail());
                break;
        }
    }


    @Override
    protected void tableSelectValue(String value, PayoutDto model, Cell editCell) {
        switch (value) {
            case ("<id>"):
                editCell.setCellValue(model.getId());
                break;
            case ("<date>"):
                editCell.setCellValue(model.getDate());
                break;
            case ("<retail store>"):
                editCell.setCellValue(model.getRetailStoreDto().getName());
                break;
            case ("<who was paid>"):
                editCell.setCellValue(model.getWhoWasPaid());
                break;
            case ("<company>"):
                editCell.setCellValue(model.getCompanyDto().getName());
                break;
            case ("<sum>"):
                editCell.setCellValue(sum.get(lengthOfSumList++));
                if (lengthOfSumList >= sum.size()) {
                    lengthOfSumList = 0;
                }
                break;
        }
    }
}

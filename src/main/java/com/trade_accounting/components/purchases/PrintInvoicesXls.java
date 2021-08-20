package com.trade_accounting.components.purchases;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.services.interfaces.EmployeeService;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;
import java.util.List;

public class PrintInvoicesXls extends PrintExcelDocument<InvoiceDto> {
    private final EmployeeService employeeService;
    private final List<String> sumList;
    private int lengthOfsumList = 0;


    protected PrintInvoicesXls(String pathToXlsTemplate, List<InvoiceDto> list,
                               List<String> sumList, EmployeeService employeeService) {
        super(pathToXlsTemplate, list);
        this.employeeService = employeeService;
        this.sumList = sumList;
    }

    @Override
    protected void selectValue(Cell editCell) {
        String formula = editCell.getStringCellValue();
        switch (formula) {
            case ("<date>"):
                editCell.setCellValue(LocalDate.now());
                break;
            case ("<authorName>"):
                editCell.setCellValue(employeeService.getPrincipal().getEmail());
                break;
        }
    }

    @Override
    protected void tableSelectValue(String value, InvoiceDto model, Cell editCell) {
        switch (value) {
            case ("<id>"):
                editCell.setCellValue(String.valueOf(model.getId()));
                break;
            case ("<date>"):
                editCell.setCellValue(model.getDate());
                break;
            case ("<contractor>"):
                editCell.setCellValue(model.getContractorDto().getName());
                break;
            case ("<company>"):
                editCell.setCellValue(model.getCompanyDto().getName());
                break;
            case ("<isSpend>"):
                editCell.setCellValue(String.valueOf(model.isSpend()));
                break;
            case ("<sum>"):
                editCell.setCellValue(sumList.get(lengthOfsumList++));
                if (lengthOfsumList >= sumList.size()) {
                    lengthOfsumList = 0;
                }
                break;
        }
    }

}

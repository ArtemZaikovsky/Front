package com.trade_accounting.components.money;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.PaymentDto;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDateTime;
import java.util.List;

public class PrintPaymentsXls extends PrintExcelDocument<PaymentDto> {

    protected PrintPaymentsXls(String pathToXlsTemplate, List<PaymentDto> list) {
        super(pathToXlsTemplate, list);
    }

    @Override
    protected void selectValue(Cell editCell) {
        String formula = editCell.getStringCellValue();
        switch (formula) {
            case ("<date>"):
                editCell.setCellValue(LocalDateTime.now());
                break;
            case ("<authorName>"):
                editCell.setCellValue("Author");
                break;
        }
    }

    @Override
    protected void tableSelectValue(String value, PaymentDto model, Cell editCell) {
        switch (value) {
            case ("<id>"):
                editCell.setCellValue(model.getId());
                break;
            case ("<time>"):
                editCell.setCellValue(model.getTime());
                break;
            case ("<company>"):
                editCell.setCellValue(model.getCompanyDto().getName());
                break;
            case ("<sum>"):
                editCell.setCellValue(String.valueOf(model.getSum()));
                break;
            case ("<number>"):
                editCell.setCellValue(model.getNumber());
                break;
            case ("<typeOfPayment>"):
                editCell.setCellValue(model.getTypeOfPayment());
                break;
            case ("<contractor>"):
                editCell.setCellValue(model.getContractorDto().getName());
                break;
            case ("<contract>"):
                editCell.setCellValue(model.getContractDto().getNumber());
                break;
            case ("<project>"):
                editCell.setCellValue(model.getProjectDto().getName());
                break;
        }
    }
}


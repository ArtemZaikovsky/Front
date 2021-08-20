package com.trade_accounting.components.contractors;

import com.trade_accounting.components.util.PrintExcelDocument;
import com.trade_accounting.models.dto.ContractorDto;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

public class PrintContractorsXls extends PrintExcelDocument<ContractorDto> {

    protected PrintContractorsXls(String pathToXlsTemplate, List<ContractorDto> list) {
        super(pathToXlsTemplate, list);
    }

    @Override
    protected void selectValue(Cell editCell) {
        String formula = editCell.getStringCellValue();
        switch (formula) {
            case ("<date>"):
                editCell.setCellValue("08.02.2021");
                break;
            case ("<authorName>"):
                editCell.setCellValue("Senya Sheykin");
                break;
        }
    }

    @Override
    protected void tableSelectValue(String value, ContractorDto model, Cell editCell) {
        switch (value) {
            case ("<name>"):
                editCell.setCellValue(model.getName());
                break;
            case ("<inn>"):
                editCell.setCellValue(model.getLegalDetailDto().getInn());
                break;
            case ("<sortNumber>"):
                editCell.setCellValue(model.getSortNumber());
                break;
            case ("<phone>"):
                editCell.setCellValue(model.getPhone());
                break;
            case ("<fax>"):
                editCell.setCellValue(model.getFax());
                break;
            case ("<email>"):
                editCell.setCellValue(model.getEmail());
                break;
            case ("<address>"):
                editCell.setCellValue(model.getAddressDto().toString());
                break;
            case ("<commentToAddress>"):
                editCell.setCellValue(model.getCommentToAddress());
                break;
            case ("<comment>"):
                editCell.setCellValue(model.getComment());
                break;
        }
    }
}

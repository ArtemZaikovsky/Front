package com.trade_accounting.components.profile;

import com.trade_accounting.models.dto.CurrencyDto;
import com.trade_accounting.services.interfaces.CurrencyService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class CurrencyModalWindow extends Dialog {

    private TextField shortNameField = new TextField();

    private TextArea fullNameField = new TextArea();

    private TextArea digitalCodeField = new TextArea();

    private TextArea letterCodeField = new TextArea();

    private TextArea sortNumberField = new TextArea();

    private Long id;

    private final String labelWidth = "100px";

    private final String fieldWidth = "400px";

    private final CurrencyService currencyService;

    public CurrencyModalWindow(CurrencyDto currencyDto,
                               CurrencyService currencyService) {
        this.currencyService = currencyService;

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        id = currencyDto.getId();
        shortNameField.setValue(getFieldValueNotNull(currencyDto.getShortName()));
        fullNameField.setValue(getFieldValueNotNull(currencyDto.getFullName()));
        digitalCodeField.setValue(getFieldValueNotNull(currencyDto.getDigitalCode()));
        letterCodeField.setValue(getFieldValueNotNull(currencyDto.getLetterCode()));
        sortNumberField.setValue(getFieldValueNotNull(currencyDto.getSortNumber()));
        add(new Text("Наименование"), header(),
                new VerticalLayout(configureShortNameField(), configureDigitalCodeField(),
                        configureLetterCodeField(), configureSortNumberField()
                ));
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        fullNameField.setWidth("345px");
        header.add(fullNameField, getSaveButton(), getCancelButton(), getDeleteButton());
        return header;
    }

    private HorizontalLayout configureShortNameField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Краткое наименование");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, shortNameField);
        return horizontalLayout;
    }

    private HorizontalLayout configureDigitalCodeField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Цифровой код");
        label.setWidth(labelWidth);
        digitalCodeField.setWidth(fieldWidth);
        digitalCodeField.getStyle().set("minHeight", "120px");
        horizontalLayout.add(label, digitalCodeField);
        return horizontalLayout;
    }

    private HorizontalLayout configureLetterCodeField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Буквенный код");
        label.setWidth(labelWidth);
        letterCodeField.setWidth(fieldWidth);
        letterCodeField.getStyle().set("minHeight", "120px");
        horizontalLayout.add(label, letterCodeField);
        return horizontalLayout;
    }

    private HorizontalLayout configureSortNumberField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Сортировочный номер");
        label.setWidth(labelWidth);
        sortNumberField.setWidth(fieldWidth);
        sortNumberField.getStyle().set("minHeight", "120px");
        horizontalLayout.add(label, sortNumberField);
        return horizontalLayout;
    }

    private Button getSaveButton() {
        return new Button("Сохранить", event -> {
            CurrencyDto currencyDto = new CurrencyDto();
            currencyDto.setId(id);
            currencyDto.setShortName(shortNameField.getValue());
            currencyDto.setFullName(fullNameField.getValue());
            currencyDto.setDigitalCode(digitalCodeField.getValue());
            currencyDto.setLetterCode(letterCodeField.getValue());
            currencyDto.setSortNumber(sortNumberField.getValue());
            if (currencyDto.getId() == null) {
                currencyService.create(currencyDto);
            } else {
                currencyService.update(currencyDto);
            }

            close();
        });
    }

    private Button getCancelButton() {
        Button cancelButton = new Button("Закрыть", event -> {
            close();
        });
        return cancelButton;
    }

    private Button getDeleteButton() {
        Button deleteButton = new Button("Удалить", event -> {
            currencyService.deleteById(id);
            close();
        });
        return deleteButton;
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }
}

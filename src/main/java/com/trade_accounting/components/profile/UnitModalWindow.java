package com.trade_accounting.components.profile;

import com.trade_accounting.components.util.ValidTextField;
import com.trade_accounting.models.dto.UnitDto;
import com.trade_accounting.services.interfaces.UnitService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.RegexpValidator;

public class UnitModalWindow extends Dialog {

    private TextField shortNameField = new TextField();

    private TextArea fullNameField = new TextArea();

    private ValidTextField sortNumberField = new ValidTextField();

    private Long id;

    private final String labelWidth = "100px";

    private final String fieldWidth = "400px";

    private final UnitService unitService;

    public UnitModalWindow(UnitDto unitDto,
                           UnitService unitService) {
        this.unitService = unitService;

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        id = unitDto.getId();
        shortNameField.setValue(getFieldValueNotNull(unitDto.getShortName()));
        fullNameField.setValue(getFieldValueNotNull(unitDto.getFullName()));
        sortNumberField.setValue(getFieldValueNotNull(unitDto.getSortNumber()));
        add(new Text("Наименование"), header(),
                new VerticalLayout(configureShortNameField(), configureSortNumberField()));
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

    private HorizontalLayout configureSortNumberField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Сортировочный номер");
        label.setWidth(labelWidth);
        sortNumberField.setWidth(fieldWidth);
        horizontalLayout.add(label, sortNumberField);
        sortNumberField.addInputListener(inputEvent ->
                sortNumberField.addValidator(new RegexpValidator("Максимум 5 цифр",
                        "^([0-9]{0,5})$")));
        return horizontalLayout;
    }

    private Button getSaveButton() {
        return new Button("Сохранить", event -> {
            UnitDto newUnitDto = new UnitDto();
            newUnitDto.setId(id);
            newUnitDto.setFullName(fullNameField.getValue());
            newUnitDto.setShortName(shortNameField.getValue());
            newUnitDto.setSortNumber(sortNumberField.getValue());
            if (!sortNumberField.isEmpty() && sortNumberField.getValue()
                    .matches("^([0-9]{0,5})$")) {
                unitService.update(newUnitDto);
                close();
            }
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
            unitService.deleteById(id);
            close();
        });
        return deleteButton;
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }
}

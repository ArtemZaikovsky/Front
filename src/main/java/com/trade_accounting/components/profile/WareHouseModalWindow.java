package com.trade_accounting.components.profile;

import com.trade_accounting.components.util.ValidTextField;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.RegexpValidator;


public class WareHouseModalWindow extends Dialog {

    private TextField nameField = new TextField();

    private TextArea addressField = new TextArea();

    private TextArea commentToAddressField = new TextArea();

    private TextArea commentField = new TextArea();

    private ValidTextField sortNumberField = new ValidTextField();

    private Long id;

    private final String labelWidth = "100px";

    private final String fieldWidth = "400px";

    private final WarehouseService warehouseService;

    public WareHouseModalWindow(WarehouseDto warehouseDto,
                                WarehouseService warehouseService) {
        this.warehouseService = warehouseService;

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        id = warehouseDto.getId();
        nameField.setValue(getFieldValueNotNull(warehouseDto.getName()));
        addressField.setValue(getFieldValueNotNull(warehouseDto.getAddress()));
        commentToAddressField.setValue(getFieldValueNotNull(warehouseDto.getCommentToAddress()));
        commentField.setValue(getFieldValueNotNull(warehouseDto.getComment()));
        sortNumberField.setValue(getFieldValueNotNull(warehouseDto.getSortNumber()));
        add(new Text("Наименование"), header(),
                new VerticalLayout(configureAddressField(), configureCommentToAddressField(),
                        configureCommentField(),
                        configureSortNumberField()));
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        nameField.setWidth("345px");
        header.add(nameField, getSaveButton(), getCancelButton(), getDeleteButton());
        return header;
    }

    private HorizontalLayout configureAddressField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Фактический адрес");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, addressField);
        return horizontalLayout;
    }

    private HorizontalLayout configureCommentToAddressField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий к адресу");
        label.setWidth(labelWidth);
        commentToAddressField.setWidth(fieldWidth);
        commentToAddressField.getStyle().set("minHeight", "120px");
        horizontalLayout.add(label, commentToAddressField);
        return horizontalLayout;
    }

    private HorizontalLayout configureCommentField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth(labelWidth);
        commentField.setWidth(fieldWidth);
        commentField.getStyle().set("minHeight", "120px");
        horizontalLayout.add(label, commentField);
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
            WarehouseDto newWarehouseDto = new WarehouseDto();
            newWarehouseDto.setId(id);
            newWarehouseDto.setName(nameField.getValue());
            newWarehouseDto.setAddress(addressField.getValue());
            newWarehouseDto.setCommentToAddress(commentToAddressField.getValue());
            newWarehouseDto.setComment(commentField.getValue());
            newWarehouseDto.setSortNumber(sortNumberField.getValue());
            if (!sortNumberField.isEmpty() && sortNumberField.getValue()
                    .matches("^([0-9]{0,5})$")) {
                warehouseService.update(newWarehouseDto);
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
            warehouseService.deleteById(id);
            close();
        });
        return deleteButton;
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }
}


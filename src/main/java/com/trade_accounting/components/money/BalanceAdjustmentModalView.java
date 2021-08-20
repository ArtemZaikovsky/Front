package com.trade_accounting.components.money;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.BalanceAdjustmentDto;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.ReturnToSupplierDto;
import com.trade_accounting.services.interfaces.BalanceAdjustmentService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.LocalDateTime;
import java.util.List;

@UIScope
@SpringComponent
public class BalanceAdjustmentModalView extends Dialog {
    private final BalanceAdjustmentService balanceAdjustmentService;
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private BalanceAdjustmentDto dto;

    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final TextField returnNumber = new TextField();
    private final TextArea textArea = new TextArea();
    private final TextField account = new TextField();
    private final TextField cashOffice = new TextField();
    private final TextField whoChanged = new TextField();

    private final Binder<BalanceAdjustmentDto> balanceAdjustmentDtoBinder = new Binder<>(BalanceAdjustmentDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";

    private final Notifications notifications;

    public BalanceAdjustmentModalView(BalanceAdjustmentService balanceAdjustmentService, CompanyService companyService,
                                      ContractorService contractorService, Notifications notifications) {
        this.balanceAdjustmentService = balanceAdjustmentService;
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.notifications = notifications;
        setSizeFull();
        add(headerLayout(), formLayout());
    }

    public void setBalanceAdjustmentForEdit(BalanceAdjustmentDto editDto) {
        this.dto = editDto;
        returnNumber.setValue(editDto.getId().toString());
        dateTimePicker.setValue(LocalDateTime.parse(editDto.getDate()));
        textArea.setValue(editDto.getComment());
        companyDtoComboBox.setValue(companyService.getById(editDto.getCompanyId()));
        contractorDtoComboBox.setValue(contractorService.getById(editDto.getContractorId()));
        account.setValue(editDto.getAccount());
        cashOffice.setValue(editDto.getCashOffice());
        whoChanged.setValue(editDto.getWhoChanged());
    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton());
        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1(), formLayout2(), formLayout3(), formLayout4(), formLayout5());
        return verticalLayout;
    }

    private HorizontalLayout formLayout1() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(numberConfigure(), dateConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout2() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(companyConfigure(), accountConfig());
        return horizontalLayout;
    }


    private HorizontalLayout formLayout3() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(contractorConfigure(), cashOfficeConfig());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout4() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(whoChangedConfig());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout5() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(commentConfig());
        return horizontalLayout;
    }

    private H2 title() {
        H2 title = new H2("Добавление корректировки");
        return title;
    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {
            if (!balanceAdjustmentDtoBinder.validate().isOk()) {
                balanceAdjustmentDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                BalanceAdjustmentDto dto = new BalanceAdjustmentDto();
                dto.setId(Long.parseLong(returnNumber.getValue()));
                dto.setCompanyId(companyDtoComboBox.getValue().getId());

                dto.setContractorId(contractorDtoComboBox.getValue().getId());
                dto.setDate(dateTimePicker.getValue().toString());

                dto.setAccount(account.getValue());
                dto.setCashOffice(cashOffice.getValue());
                dto.setWhoChanged(whoChanged.getValue());

                dto.setComment(textArea.getValue());
                balanceAdjustmentService.create(dto);

                UI.getCurrent().navigate("balanceAdjustment");
                close();
                clearAllFieldsModalView();
                notifications.infoNotification(String.format("Корректировка № %s сохранена", dto.getId()));
            }
        });
    }
    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
            clearAllFieldsModalView();
        });
        return button;
    }

    private HorizontalLayout numberConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Корректировка №");
        label.setWidth("150px");
        returnNumber.setWidth("50px");
        horizontalLayout.add(label, returnNumber);
        balanceAdjustmentDtoBinder.forField(returnNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(BalanceAdjustmentDto::getIdValid, BalanceAdjustmentDto::setIdValid);
        return horizontalLayout;
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("От");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label, dateTimePicker);
        balanceAdjustmentDtoBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(BalanceAdjustmentDto::getDateValid, BalanceAdjustmentDto::setDateValid);
        return horizontalLayout;
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> list = companyService.getAll();
        if (list != null) {
            companyDtoComboBox.setItems(list);
        }
        companyDtoComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyDtoComboBox.setWidth("350px");
        Label label = new Label("Организация");
        label.setWidth("100px");
        horizontalLayout.add(label, companyDtoComboBox);
        balanceAdjustmentDtoBinder.forField(companyDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(BalanceAdjustmentDto::getCompanyDtoValid, BalanceAdjustmentDto::setCompanyDtoValid);
        return horizontalLayout;
    }

    private HorizontalLayout contractorConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> list = contractorService.getAll();
        if (list != null) {
            contractorDtoComboBox.setItems(list);
        }
        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
        contractorDtoComboBox.setWidth("350px");
        Label label = new Label("Контрагент");
        label.setWidth("100px");
        horizontalLayout.add(label, contractorDtoComboBox);
        balanceAdjustmentDtoBinder.forField(contractorDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(BalanceAdjustmentDto::getContractorDtoValid, BalanceAdjustmentDto::setContractorDtoValid);
        return horizontalLayout;
    }

    private HorizontalLayout commentConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, textArea);
        return horizontalLayout;
    }

    private HorizontalLayout accountConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Счет");
        label.setWidth("50px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("50px");
        horizontalLayout.add(label, account);
        return horizontalLayout;
    }

    private HorizontalLayout cashOfficeConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Касса");
        label.setWidth("50px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("50px");
        horizontalLayout.add(label, cashOffice);
        return horizontalLayout;
    }

    private HorizontalLayout whoChangedConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Кто изменил");
        label.setWidth("100px");
        horizontalLayout.setWidth("150px");
        horizontalLayout.setHeight("50px");
        horizontalLayout.add(label, whoChanged);
        return horizontalLayout;
    }

    private void clearAllFieldsModalView(){
        companyDtoComboBox.setValue(null);
        contractorDtoComboBox.setValue(null);
        dateTimePicker.setValue(null);
        textArea.setValue("");
        returnNumber.setValue("");
        account.setValue("");
        cashOffice.setValue("");
        whoChanged.setValue("");
    }
}

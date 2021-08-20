package com.trade_accounting.components.purchases;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.SupplierAccountDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.SupplierAccountService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.format.datetime.joda.LocalDateParser;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@SpringComponent
@UIScope
public class SupplierAccountModalView extends Dialog {

    private final SupplierAccountService supplierAccountService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final ContractorService contractorService;
    private final ContractService contractService;
    private SupplierAccountDto supplierAccountDto;

    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractDto> contractDtoComboBox = new ComboBox<>();
    public final  ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Checkbox isSpend = new Checkbox("Проведено");
    private final TextField supplierNumber = new TextField();
    private final TextField commentConfig = new TextField();
    private final Notifications notifications;


    public SupplierAccountModalView(SupplierAccountService supplierAccountService,
                                    CompanyService companyService,
                                    WarehouseService warehouseService,
                                    ContractorService contractorService, ContractService contractService, Notifications notifications) {
        this.supplierAccountService = supplierAccountService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.contractorService = contractorService;
        this.contractService = contractService;
        this.notifications = notifications;
        setSizeFull();
        add(upperButtonInModalView(),formToAddSupplerAccount());
    }



    public void setSupplierAccountsForEdit(SupplierAccountDto editSupplierAccounts) {
        this.supplierAccountDto = editSupplierAccounts;
        supplierNumber.setValue(supplierAccountDto.getId().toString());
        dateTimePicker.setValue( LocalDateTime.parse(supplierAccountDto.getDate()));
        commentConfig.setValue(supplierAccountDto.getComment());
        companyDtoComboBox.setValue(companyService.getById(supplierAccountDto.getCompanyId()));
        warehouseDtoComboBox.setValue(warehouseService.getById(supplierAccountDto.getWarehouseId()));
        contractDtoComboBox.setValue(contractService.getById(supplierAccountDto.getContractId()));
        contractorDtoComboBox.setValue(contractorService.getById(supplierAccountDto.getContractorId()));
    }

    private HorizontalLayout upperButtonInModalView() {
        HorizontalLayout upperButton = new HorizontalLayout();
        upperButton.add(title(),saveButton(),closeButton(),addProductButton());
        upperButton.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperButton;
    }

    private H2 title() {
        final H2 label = new H2("Добавление счета");
        label.setHeight("2.2em");
        return label;
    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {
            SupplierAccountDto saveSupplier = new SupplierAccountDto();
            saveSupplier.setId(Long.parseLong(supplierNumber.getValue()));
            saveSupplier.setDate(dateTimePicker.getValue().toString());
            saveSupplier.setCompanyId(companyDtoComboBox.getValue().getId());
            saveSupplier.setWarehouseId(warehouseDtoComboBox.getValue().getId());
            saveSupplier.setContractId(contractDtoComboBox.getValue().getId());
            saveSupplier.setContractorId(contractorDtoComboBox.getValue().getId());
            saveSupplier.setSpend(isSpend.getValue());
            saveSupplier.setComment(commentConfig.getValue());
            supplierAccountService.create(saveSupplier);

            UI.getCurrent().navigate("suppliersInvoices");
            close();
            notifications.infoNotification(String.format("Счет поставщика № %s сохранен", saveSupplier.getId()));
        });
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
        });
        return button;

    }

    private Button addProductButton() {
        return new Button("Добавить из справочника", new Icon(VaadinIcon.PLUS_CIRCLE), e -> {

        });
    }

    private VerticalLayout formToAddSupplerAccount() {
        VerticalLayout form = new VerticalLayout();
        form.add(horizontalLayout1(), horizontalLayout2(),
                horizontalLayout3(), dataPlaneConfigure(), incomingConfigure(), commentConfigure());
        return form;
    }

    private HorizontalLayout horizontalLayout1() {
        HorizontalLayout hLay1 = new HorizontalLayout();
        hLay1.add(dataConfigure(), isSpend);
        return  hLay1;
    }

    private HorizontalLayout horizontalLayout2() {
        HorizontalLayout hLay2 = new HorizontalLayout();
        hLay2.add(companyConfigure(),warehouseConfigure());
        return hLay2;
    }

    private HorizontalLayout horizontalLayout3() {
        HorizontalLayout hLay3 = new HorizontalLayout();
        hLay3.add(contractorConfigure(), contractConfigure());
        return hLay3;
    }

    private HorizontalLayout dataConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Счет поставщика №");
        label.setWidth("150px");
        supplierNumber.setWidth("50px");
        Label label2 = new Label("от");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label,supplierNumber,label2,dateTimePicker);
        return horizontalLayout;
    }

    private HorizontalLayout contractorConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> contractorDtos = contractorService.getAll();
        if(contractorDtos != null) {
            contractorDtoComboBox.setItems(contractorDtos);
        }
        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
        contractorDtoComboBox.setWidth("350px");
        Label label = new Label("Контрагент");
        label.setWidth("100px");
        horizontalLayout.add(label, contractorDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> companyDtos = companyService.getAll();
        if(companyDtos != null) {
            companyDtoComboBox.setItems(companyDtos);
        }
        companyDtoComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyDtoComboBox.setWidth("350px");
        Label label = new Label("Компания");
        label.setWidth("100px");
        horizontalLayout.add(label,companyDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout warehouseConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> warehouseDtos = warehouseService.getAll();
        if(warehouseDtos !=null) {
            warehouseDtoComboBox.setItems(warehouseDtos);
        }
        warehouseDtoComboBox.setItemLabelGenerator(WarehouseDto::getName);
        warehouseDtoComboBox.setWidth("350px");
        Label label = new Label("Склад");
        label.setWidth("100px");
        horizontalLayout.add(label,warehouseDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout contractConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractDto> contractDtos = contractService.getAll();
        if(contractDtos !=null) {
            contractDtoComboBox.setItems(contractDtos);
        }
        contractDtoComboBox.setItemLabelGenerator(ContractDto::getNumber);
        contractDtoComboBox.setWidth("350px");
        Label label = new Label("Договор");
        label.setWidth("100px");
        horizontalLayout.add(label,contractDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout dataPlaneConfigure() {
        HorizontalLayout horizontal = new HorizontalLayout();
        DatePicker dt = new DatePicker();
        Label label = new Label("План. дата оплаты");
        label.setWidth("150px");
        dt.setWidth("150px");
        horizontal.add(label,dt);
        return horizontal;
    }

    private HorizontalLayout incomingConfigure() {
        HorizontalLayout horizontal1 = new HorizontalLayout();
        DatePicker dt1 = new DatePicker();
        Label label = new Label("Входящий номер");
        label.setWidth("150px");
        TextField text = new TextField();
        text.setWidth("70px");
        Label label2 = new Label("от");
        dt1.setWidth("150px");
        horizontal1.add(label,text,label2,dt1);
        return horizontal1;
    }

    private HorizontalLayout commentConfigure() {
        HorizontalLayout horizontal3 = new HorizontalLayout();
        commentConfig.setWidth("500px");
        commentConfig.setHeight("300px");
        commentConfig.setPlaceholder("Комментарий");
        horizontal3.add(commentConfig);
        return horizontal3;
    }


}

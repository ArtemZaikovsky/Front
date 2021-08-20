package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.models.dto.ProjectDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.trade_accounting.services.interfaces.ProjectService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Route(value = "sells/shipment-edit", layout = AppView.class)
@PageTitle("Добавить отгрузку")
@PreserveOnRefresh
@SpringComponent
@UIScope
public class SalesEditShipmentView extends VerticalLayout{
    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final ProjectService projectService;
    private final WarehouseService warehouseService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final Notifications notifications;

    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "350px";
    private final TextField invoiceIdField = new TextField();
    private final DateTimePicker dateField = new DateTimePicker();
    private final Checkbox isSpend = new Checkbox("Проведено");
    private final ComboBox<CompanyDto> companySelect = new ComboBox<>();
    public final ComboBox<ContractorDto> contractorSelect = new ComboBox<>();
    public final ComboBox<ProjectDto> projectSelect = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseSelect = new ComboBox<>();

    private final Button buttonDelete = new Button("Удалить", new Icon(VaadinIcon.TRASH));

    private final H4 totalPrice = new H4();
    private final H2 title = new H2("Добавление отгрузки");

    private List<InvoiceProductDto> tempInvoiceProductDtoList = new ArrayList<>();

    private final Dialog dialogOnChangeContractor = new Dialog();
    private final Dialog dialogOnCloseView = new Dialog();

    private final Grid<InvoiceProductDto> grid = new Grid<>(InvoiceProductDto.class, false);
    private final GridPaginator<InvoiceProductDto> paginator;

    private final Editor<InvoiceProductDto> editor = grid.getEditor();
    private final Binder<InvoiceDto> binderInvoiceDto = new Binder<>(InvoiceDto.class);
    private final Binder<InvoiceDto> binderInvoiceDtoContractorValueChangeListener = new Binder<>(InvoiceDto.class);
    private String type = null;
    private String location = null;

    @Autowired
    public SalesEditShipmentView(ContractorService contractorService,
                                 CompanyService companyService,
                                 ProjectService projectService, WarehouseService warehouseService,
                                 InvoiceService invoiceService,
                                 InvoiceProductService invoiceProductService,
                                 Notifications notifications
    ) {
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.projectService = projectService;
        this.warehouseService = warehouseService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.notifications = notifications;

        configureCloseViewDialog();

        binderInvoiceDtoContractorValueChangeListener.forField(contractorSelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("contractorDto");
        binderInvoiceDtoContractorValueChangeListener.addValueChangeListener(valueChangeEvent -> {
            if (
                    valueChangeEvent.isFromClient()
                            && valueChangeEvent.getOldValue() != null
                            && !tempInvoiceProductDtoList.isEmpty()
            ) {
                dialogOnChangeContractor.open();
            }
        });

        configureGrid();
        paginator = new GridPaginator<>(grid, tempInvoiceProductDtoList, 50);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);

        add(upperButtonsLayout(), formLayout(), grid, paginator);
    }

    private void configureGrid() {
        grid.setItems(tempInvoiceProductDtoList);
        grid.addColumn(inPrDto -> inPrDto.getProductDto().getName()).setHeader("Название")
                .setKey("productDtoName").setId("Название");
        grid.addColumn(inPrDto -> inPrDto.getProductDto().getDescription()).setHeader("Описание")
                .setKey("productDtoDescr").setId("Описание");
        Grid.Column<InvoiceProductDto> firstNameColumn = grid.addColumn("amount").setHeader("Количество");
        grid.addColumn(inPrDto -> inPrDto.getProductDto().getUnitDto().getFullName()).setHeader("Единицы")
                .setKey("productDtoUnit").setId("Единицы");
        grid.addColumn("price").setHeader("Цена").setSortable(true).setId("Цена");
        grid.setHeight("36vh");
        grid.setColumnReorderingAllowed(true);

        editor.setBuffered(true);
        Div validationStatus = new Div();
        validationStatus.setId("validation");
        add(validationStatus);

        Button cancel = new Button("Cancel", e -> editor.cancel());
        cancel.addClassName("cancel");
    }

    private HorizontalLayout upperButtonsLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonSave(), configureDeleteButton(), buttonClose());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private VerticalLayout formLayout() {
        VerticalLayout upper = new VerticalLayout();
        upper.add(horizontalLayout1(),
                horizontalLayout2(),
                horizontalLayout3(),
                horizontalLayout4()
        );
        return upper;
    }

    private HorizontalLayout horizontalLayout1() {
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.add(configureDateField(),
                isSpend
        );
        return horizontalLayout1;
    }

    private HorizontalLayout horizontalLayout2() {
        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.add(configureCompanySelect(),
                configureWarehouseSelect()
        );
        return horizontalLayout2;
    }

    private HorizontalLayout horizontalLayout3() {
        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        horizontalLayout3.add(configureContractorSelect(),
                configureContractField()
        );
        return horizontalLayout3;
    }

    private HorizontalLayout horizontalLayout4() {
        HorizontalLayout horizontalLayout4 = new HorizontalLayout();
        horizontalLayout4.add(configureProjectSelect());
        return horizontalLayout4;
    }

    private HorizontalLayout configureDateField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Дата");
        label.setWidth(LABEL_WIDTH);
        dateField.setWidth(FIELD_WIDTH);
        dateField.setHelperText("По умолчанию текущая дата/время");
        horizontalLayout.add(label, dateField);
        return horizontalLayout;
    }

    private HorizontalLayout configureCompanySelect() {
        HorizontalLayout companyLayout = new HorizontalLayout();
        List<CompanyDto> companies = companyService.getAll();
        if (companies != null) {
            companySelect.setItems(companies);
        }
        companySelect.setItemLabelGenerator(CompanyDto::getName);
        companySelect.setWidth(FIELD_WIDTH);
        binderInvoiceDto.forField(companySelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("companyDto");
        Label label = new Label("Организация");
        label.setWidth(LABEL_WIDTH);
        companyLayout.add(label, companySelect);
        return companyLayout;
    }

    private HorizontalLayout configureContractorSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> contractors = contractorService.getAll();
        if (contractors != null) {
            contractorSelect.setItems(contractors);
        }
        contractorSelect.setItemLabelGenerator(ContractorDto::getName);
        contractorSelect.setWidth(FIELD_WIDTH);
        binderInvoiceDto.forField(contractorSelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("contractorDto");
        Label label = new Label("Контрагент");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, contractorSelect);
        return horizontalLayout;
    }

    private HorizontalLayout configureContractField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Договор");
        TextField textContract = new TextField();
        textContract.setWidth(FIELD_WIDTH);
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, textContract);
        return horizontalLayout;
    }

    private HorizontalLayout configureProjectSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ProjectDto> projects = projectService.getAll();
        if (projects != null) {
            projectSelect.setItems(projects);
        }
        projectSelect.setItemLabelGenerator(ProjectDto::getName);
        projectSelect.setWidth(FIELD_WIDTH);
        Label label = new Label("Проект");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, projectSelect);
        return horizontalLayout;
    }

 private HorizontalLayout configureWarehouseSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> warehouses = warehouseService.getAll();
        if (warehouses != null) {
            warehouseSelect.setItems(warehouses);
        }
        warehouseSelect.setItemLabelGenerator(WarehouseDto::getName);
        warehouseSelect.setWidth(FIELD_WIDTH);
        binderInvoiceDto.forField(warehouseSelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("warehouseDto");
        Label label = new Label("Склад");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, warehouseSelect);
        return horizontalLayout;
    }

    private H2 title() {
        title.setHeight("2.0em");
        return title;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Button buttonSave() {
        return new Button("Сохранить", buttonClickEvent -> {

            if (!binderInvoiceDto.validate().isOk()) {
                binderInvoiceDto.validate().notifyBindingValidationStatusHandlers();
            } else {

                if (dateField.getValue() == null) {
                    dateField.setValue(LocalDateTime.now());
                }
                InvoiceDto invoiceDto = saveInvoice(type);
                UI.getCurrent().navigate(location);
                notifications.infoNotification(String.format("Отгрузка № %s сохранена", invoiceDto.getId()));
            }
        });
    }

    private Button buttonClose() {
        Button buttonUnit = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        buttonUnit.addClickListener(event -> {
            dialogOnCloseView.open();
        });
        return buttonUnit;
    }

    private void closeView() {
        resetView();
        UI.getCurrent().navigate(location);
    }

    private Button configureDeleteButton() {
        buttonDelete.addClickListener(event -> {
            deleteInvoiceById(Long.parseLong(invoiceIdField.getValue()));
            resetView();
            buttonDelete.getUI().ifPresent(ui -> ui.navigate(location));
        });
        return buttonDelete;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (InvoiceProductDto invoiceProductDto : tempInvoiceProductDtoList) {
            totalPrice = totalPrice.add(invoiceProductDto.getPrice()
                    .multiply(invoiceProductDto.getAmount()));
        }
        return totalPrice;
    }

    private void setTotalPrice() {
        totalPrice.setText(
                String.format("%.2f", getTotalPrice())
        );
    }

    public void resetView() {
        invoiceIdField.clear();
        dateField.clear();
        companySelect.setValue(null);
        contractorSelect.setValue(null);
        projectSelect.setValue(null);
        warehouseSelect.setValue(null);
        isSpend.clear();
        companySelect.setInvalid(false);
        contractorSelect.setInvalid(false);
        projectSelect.setInvalid(false);
        warehouseSelect.setInvalid(false);
        title.setText("Добавление отгрузки");
        setTotalPrice();
    }

    private InvoiceDto saveInvoice(String type) {
        InvoiceDto invoiceDto = new InvoiceDto();
        if (!invoiceIdField.getValue().equals("")) {
            invoiceDto.setId(Long.parseLong(invoiceIdField.getValue()));
        }
        invoiceDto.setDate(dateField.getValue().toString());
        invoiceDto.setCompanyDto(companySelect.getValue());
        invoiceDto.setContractorDto(contractorSelect.getValue());
        invoiceDto.setWarehouseDto(warehouseSelect.getValue());
        invoiceDto.setTypeOfInvoice(type);
        invoiceDto.setSpend(isSpend.getValue());
        invoiceDto.setComment("");
        Response<InvoiceDto> invoiceDtoResponse = invoiceService.create(invoiceDto);
        InvoiceDto invoiceDtoForProducts = invoiceDtoResponse.body();
        return invoiceDtoForProducts;
    }

    public void deleteInvoiceById(Long invoiceDtoId) {
        invoiceService.deleteById(invoiceDtoId);
        notifications.infoNotification(String.format("Отгрузка № %s успешно удалена", invoiceDtoId));
    }

    private void configureCloseViewDialog() {
        dialogOnCloseView.add(new Text("Вы уверены? Несохраненные данные будут потеряны!!!"));
        dialogOnCloseView.setCloseOnEsc(false);
        dialogOnCloseView.setCloseOnOutsideClick(false);
        Span message = new Span();

        Button confirmButton = new Button("Продолжить", event -> {
            closeView();
            dialogOnCloseView.close();
        });
        Button cancelButton = new Button("Отменить", event -> {
            dialogOnCloseView.close();
        });
 //Cancel action on ESC press
        Shortcuts.addShortcutListener(dialogOnCloseView, () -> {
            dialogOnCloseView.close();
        }, Key.ESCAPE);

        dialogOnCloseView.add(new Div(confirmButton, new Div(), cancelButton));
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

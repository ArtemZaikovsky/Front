package com.trade_accounting.components.sells;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.ProductPriceDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.InvoiceService;
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
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.WeakHashMap;

@Slf4j
@Route(value = "sells/customer-order-edit", layout = AppView.class)
@PageTitle("Изменить заказ")
@PreserveOnRefresh
@SpringComponent
@UIScope
public class SalesEditCreateInvoiceView extends VerticalLayout {

    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final Notifications notifications;

    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "350px";
    private final TextField invoiceIdField = new TextField();
    private final DateTimePicker dateField = new DateTimePicker();
    private final TextField typeOfInvoiceField = new TextField();
    private final Checkbox isSpend = new Checkbox("Проведено");
    private final ComboBox<CompanyDto> companySelect = new ComboBox<>();
    public final ComboBox<ContractorDto> contractorSelect = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseSelect = new ComboBox<>();

    private final TextField amountField = new TextField();

    private final Button buttonDelete = new Button("Удалить", new Icon(VaadinIcon.TRASH));

    private final H4 totalPrice = new H4();
    private final H2 title = new H2("Добавление заказа");

    private List<InvoiceProductDto> tempInvoiceProductDtoList = new ArrayList<>();

    private final Dialog dialogOnChangeContractor = new Dialog();
    private final Dialog dialogOnCloseView = new Dialog();

    private final Grid<InvoiceProductDto> grid = new Grid<>(InvoiceProductDto.class, false);
    private final GridPaginator<InvoiceProductDto> paginator;
    private final SalesChooseGoodsModalWin salesChooseGoodsModalWin;

    private final Editor<InvoiceProductDto> editor = grid.getEditor();
    private final Binder<InvoiceProductDto> binderInvoiceProductDto = new Binder<>(InvoiceProductDto.class);
    private final Binder<InvoiceDto> binderInvoiceDto = new Binder<>(InvoiceDto.class);
    private final Binder<InvoiceDto> binderInvoiceDtoContractorValueChangeListener = new Binder<>(InvoiceDto.class);
    private String type = null;
    private String location = null;

    @Autowired
    public SalesEditCreateInvoiceView(ContractorService contractorService,
                                      CompanyService companyService,
                                      WarehouseService warehouseService,
                                      InvoiceService invoiceService,
                                      InvoiceProductService invoiceProductService,
                                      Notifications notifications,
                                      SalesChooseGoodsModalWin salesChooseGoodsModalWin
    ) {
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.notifications = notifications;
        this.salesChooseGoodsModalWin = salesChooseGoodsModalWin;

        configureRecalculateDialog();
        configureCloseViewDialog();

        salesChooseGoodsModalWin.addDetachListener(detachEvent -> {
            if (salesChooseGoodsModalWin.productSelect.getValue() != null) {
                addProduct(salesChooseGoodsModalWin.productSelect.getValue());
            }
        });

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
//        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        editor.setBinder(binderInvoiceProductDto);
        editor.setBuffered(true);
        Div validationStatus = new Div();
        validationStatus.setId("validation");
        add(validationStatus);


        amountField.setPattern("^[1-9][0-9]*$");
        amountField.setErrorMessage("Требуется целое число");
        binderInvoiceProductDto.forField(amountField)
                .withConverter(new StringToBigDecimalConverter("must be a number"))
                .withStatusLabel(validationStatus).bind("amount");
        firstNameColumn.setEditorComponent(amountField);
        Collection<Button> editButtons = Collections
                .newSetFromMap(new WeakHashMap<>());

        Grid.Column<InvoiceProductDto> editorColumn = grid.addComponentColumn(column -> {
            Button edit = new Button(new Icon(VaadinIcon.EDIT));
            edit.addClassName("edit");
            edit.addClickListener(e -> {
                editor.editItem(column);
                amountField.focus();
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        });

        grid.addComponentColumn(column -> {
            Button edit = new Button(new Icon(VaadinIcon.TRASH));
            edit.addClassName("delete");
            edit.addClickListener(e -> deleteProduct(column.getProductDto().getId()));
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        });

        editor.addOpenListener(e -> editButtons
                .forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons
                .forEach(button -> button.setEnabled(!editor.isOpen())));

        Button save = new Button("Save", e -> {
            if (binderInvoiceProductDto.validate().isOk()) {
                editor.save();
                setTotalPrice();
                paginator.setData(tempInvoiceProductDtoList);
            } else {
                binderInvoiceProductDto.validate().notifyBindingValidationStatusHandlers();
                editor.cancel();
            }
        });
        save.addClassName("save");

        Button cancel = new Button("Cancel", e -> editor.cancel());
        cancel.addClassName("cancel");

// Add a keypress listener that listens for an escape key up event.
        grid.getElement().addEventListener("keyup", event -> editor.cancel())
                .setFilter("event.key === 'Escape' || event.key === 'Esc'");

        grid.getElement().addEventListener("keyup", event -> {
            if (binderInvoiceProductDto.validate().isOk()) {
                editor.save();
                setTotalPrice();
                paginator.setData(tempInvoiceProductDtoList);
            } else {
                binderInvoiceProductDto.validate().notifyBindingValidationStatusHandlers();
                editor.cancel();
            }
            buttonAddProduct().focus();
        }).setFilter("event.key === 'Enter'");

        Div buttons = new Div(save, cancel);
        editorColumn.setEditorComponent(buttons);

//        editor.addSaveListener(
//                event -> System.out.println("save listener")
//        );
    }

    private HorizontalLayout upperButtonsLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonSave(), configureDeleteButton(), buttonClose(), buttonAddProduct());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private VerticalLayout formLayout() {
        VerticalLayout upper = new VerticalLayout();
        upper.add(horizontalLayout1(),
                horizontalLayout2()
        );
        return upper;
    }

    private HorizontalLayout horizontalLayout1() {
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.add(configureDateField(),
                configureContractorSelect(),
                isSpend
        );
        return horizontalLayout1;
    }

    private HorizontalLayout horizontalLayout2() {
        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.add(configureCompanySelect(),
                configureWarehouseSelect(),
                configureTotalPrice()
        );
        return horizontalLayout2;
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
        Label label = new Label("Компания");
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

    private HorizontalLayout configureTotalPrice() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(totalPriceTitle(), totalPrice());
        return horizontalLayout;
    }

    private H4 totalPriceTitle() {
        H4 totalPriceTitle = new H4("Итого:");
        totalPriceTitle.setHeight("2.0em");
        return totalPriceTitle;
    }

    private H4 totalPrice() {
        totalPrice.setText(getTotalPrice().toString());
        totalPrice.setHeight("2.0em");
        return totalPrice;
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

                deleteAllInvoiceProductByInvoice(
                        getListOfInvoiceProductByInvoice(invoiceDto)
                );

                addInvoiceProductToInvoicedDto(invoiceDto);
                UI.getCurrent().navigate(location);
                notifications.infoNotification(String.format("Заказ № %s сохранен", invoiceDto.getId()));
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

    private Button buttonAddProduct() {
        return new Button("Добавить продукт", new Icon(VaadinIcon.PLUS_CIRCLE), buttonClickEvent -> {
            if (!binderInvoiceDto.validate().isOk()) {
                binderInvoiceDto.validate().notifyBindingValidationStatusHandlers();
            } else {
                salesChooseGoodsModalWin.updateProductList();
                salesChooseGoodsModalWin.open();
            }
        });
    }

    public void addProduct(ProductDto productDto) {
        InvoiceProductDto invoiceProductDto = new InvoiceProductDto();
        invoiceProductDto.setProductDto(productDto);
        invoiceProductDto.setAmount(BigDecimal.ONE);
        invoiceProductDto.setPrice(
                getPriceFromProductPriceByTypeOfPriceId(productDto.getProductPriceDtos(),
                        contractorSelect.getValue().getTypeOfPriceDto().getId()
                )
        );
        if (!isProductInList(productDto)) {
            tempInvoiceProductDtoList.add(invoiceProductDto);
            paginator.setData(tempInvoiceProductDtoList);
            setTotalPrice();
        }
    }

    private BigDecimal getPriceFromProductPriceByTypeOfPriceId(List<ProductPriceDto> productPriceDtoList, Long id) {
        Optional<ProductPriceDto> productPrice = productPriceDtoList.stream().filter(productPriceDto ->
                productPriceDto.getTypeOfPriceDto().getId().equals(id)).findFirst();

        //TODO
        // Когда переделают инициализвцию продуктов (у которых есть список ProductPrice) на бэке
        // использовать return который закоментирован
        // return productPrice.get().getValue();

        return productPrice.isPresent() ? productPrice.get().getValue() : BigDecimal.ZERO;
    }

    private void deleteProduct(Long id) {
        InvoiceProductDto found = new InvoiceProductDto();
        for (InvoiceProductDto invoiceProductDto : tempInvoiceProductDtoList) {
            if (invoiceProductDto.getProductDto().getId().equals(id)) {
                found = invoiceProductDto;
                break;
            }
        }
        tempInvoiceProductDtoList.remove(found);
        paginator.setData(tempInvoiceProductDtoList);
        setTotalPrice();
    }

    public void setInvoiceDataForEdit(InvoiceDto invoiceDto) {

        if (invoiceDto.getId() != null) {
            invoiceIdField.setValue(invoiceDto.getId().toString());
            setInvoiceProductDtoListForEdit(invoiceDto);
        }

        if (invoiceDto.getDate() != null) {
            dateField.setValue(LocalDateTime.parse(invoiceDto.getDate()));
        }

        if (invoiceDto.getTypeOfInvoice() != null) {
            typeOfInvoiceField.setValue(invoiceDto.getTypeOfInvoice());
        } else {
            typeOfInvoiceField.setValue("");
        }

        if (invoiceDto.getCompanyDto() != null) {
            companySelect.setValue(invoiceDto.getCompanyDto());
        }

        if (invoiceDto.getContractorDto() != null) {
            contractorSelect.setValue(invoiceDto.getContractorDto());
        }

        isSpend.setValue(invoiceDto.isSpend());

        if (invoiceDto.getWarehouseDto() != null) {
            warehouseSelect.setValue(invoiceDto.getWarehouseDto());
        }

    }

    private boolean isProductInList(ProductDto productDto) {
        boolean isExists = false;
        for (InvoiceProductDto invoiceProductDto : tempInvoiceProductDtoList) {
            if (invoiceProductDto.getProductDto().getId().equals(productDto.getId())) {
                isExists = true;
            }
        }
        return isExists;
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

    public void setUpdateState(boolean isUpdate) {
        title.setText(isUpdate ? "Редактирование заказа" : "Добавление заказа");
        buttonDelete.setVisible(isUpdate);
    }

    public void resetView() {
        invoiceIdField.clear();
        dateField.clear();
        companySelect.setValue(null);
        contractorSelect.setValue(null);
        warehouseSelect.setValue(null);
        isSpend.clear();
        contractorSelect.setInvalid(false);
        companySelect.setInvalid(false);
        warehouseSelect.setInvalid(false);
        title.setText("Добавление заказа");
        paginator.setData(tempInvoiceProductDtoList = new ArrayList<>());
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

    private void addInvoiceProductToInvoicedDto(InvoiceDto invoiceDto) {
        for (InvoiceProductDto invoiceProductDto : tempInvoiceProductDtoList) {
            invoiceProductDto.setInvoiceDto(invoiceDto);
            invoiceProductDto.setProductDto(invoiceProductDto.getProductDto());
            invoiceProductDto.setPrice(invoiceProductDto.getPrice());
            invoiceProductDto.setAmount(invoiceProductDto.getAmount());
            invoiceProductService.create(invoiceProductDto);
        }
    }

    public List<InvoiceProductDto> getListOfInvoiceProductByInvoice(InvoiceDto invoiceDto) {
        return invoiceProductService.getByInvoiceId(invoiceDto.getId());
    }

    private void deleteAllInvoiceProductByInvoice(List<InvoiceProductDto> invoiceProductDtoList) {
        for (InvoiceProductDto invoiceProductDto : invoiceProductDtoList) {
            invoiceProductService.deleteById(invoiceProductDto.getId());
        }
    }

    public void deleteInvoiceById(Long invoiceDtoId) {
        invoiceService.deleteById(invoiceDtoId);
        notifications.infoNotification(String.format("Заказ № %s успешно удален", invoiceDtoId));
    }

    private void setInvoiceProductDtoListForEdit(InvoiceDto invoiceDto) {
        tempInvoiceProductDtoList = getListOfInvoiceProductByInvoice(invoiceDto);
        setTotalPrice();
        grid.setItems(tempInvoiceProductDtoList);
    }

    private void recalculateProductPrices() {
        for (InvoiceProductDto invoiceProductDto : tempInvoiceProductDtoList) {
            invoiceProductDto.setPrice(
                    getPriceFromProductPriceByTypeOfPriceId(
                            invoiceProductDto.getProductDto().getProductPriceDtos(),
                            contractorSelect.getValue().getTypeOfPriceDto().getId()
                    )
            );
            grid.setItems(tempInvoiceProductDtoList);
            setTotalPrice();
        }
    }

    private void configureRecalculateDialog() {
        dialogOnChangeContractor.add(new Text("Вы меняете покупателя!! Пересчитать цены на продукты?"));
        dialogOnChangeContractor.setCloseOnEsc(false);
        dialogOnChangeContractor.setCloseOnOutsideClick(false);
        Span message = new Span();

        Button confirmButton = new Button("Пересчитать", event -> {
            recalculateProductPrices();
            dialogOnChangeContractor.close();
        });
        Button cancelButton = new Button("Оставить как есть", event -> {
            dialogOnChangeContractor.close();
        });
// Cancel action on ESC press
        Shortcuts.addShortcutListener(dialogOnChangeContractor, () -> {
            dialogOnChangeContractor.close();
        }, Key.ESCAPE);

        dialogOnChangeContractor.add(new Div(confirmButton, new Div(), cancelButton));
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
// Cancel action on ESC press
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

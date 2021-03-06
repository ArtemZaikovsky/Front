package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
//import com.trade_accounting.components.purchases.XLSPrinter;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "customersOrders", layout = AppView.class)
@PageTitle("???????????? ??????????????????????")
@SpringComponent
@UIScope
public class SalesSubCustomersOrdersView extends VerticalLayout implements AfterNavigationObserver {

    private final InvoiceService invoiceService;
    private final EmployeeService employeeService;

    private final SalesEditCreateInvoiceView salesEditCreateInvoiceView;

    private final Notifications notifications;

    private final List<InvoiceDto> data;
    private final Grid<InvoiceDto> grid = new Grid<>(InvoiceDto.class, false);
    private final GridPaginator<InvoiceDto> paginator;
    private final GridFilter<InvoiceDto> filter;

    private final String typeOfInvoice = "RECEIPT";
    private final String pathForSaveSalesXlsTemplate = "src/main/resources/xls_templates/sales_templates/";

    @Autowired
    public SalesSubCustomersOrdersView(InvoiceService invoiceService,
                                       @Lazy SalesEditCreateInvoiceView salesEditCreateInvoiceView,
                                       @Lazy Notifications notifications,
                                       EmployeeService employeeService) {
        this.salesEditCreateInvoiceView = salesEditCreateInvoiceView;
        this.employeeService = employeeService;
        this.invoiceService = invoiceService;
        this.notifications = notifications;
        this.data = getData();
        paginator = new GridPaginator<>(grid, data, 50);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(upperLayout(), filter, grid, paginator);
    }

    private void configureGrid() {
        grid.addColumn("id").setHeader("???").setId("???");
        grid.addColumn(iDto -> formatDate(iDto.getDate())).setKey("date").setHeader("????????").setSortable(true)
                .setId("????????");
        grid.addColumn(iDto -> iDto.getContractorDto().getName()).setHeader("????????????????????").setKey("contractorDto")
                .setId("????????????????????");
        grid.addColumn(iDto -> iDto.getCompanyDto().getName()).setHeader("????????????????").setKey("companyDto")
                .setId("????????????????");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedIcon)).setKey("spend").setHeader("??????????????????")
                .setId("??????????????????");

        grid.addColumn(this::getTotalPrice).setHeader("??????????").setSortable(true);
        grid.addColumn("comment").setHeader("??????????????????????").setId("??????????????????????");
        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemDoubleClickListener(event -> {
            InvoiceDto editInvoice = event.getItem();
            salesEditCreateInvoiceView.setInvoiceDataForEdit(editInvoice);
            salesEditCreateInvoiceView.setUpdateState(true);
            salesEditCreateInvoiceView.setType("RECEIPT");
            salesEditCreateInvoiceView.setLocation("sells");
            UI.getCurrent().navigate("sells/customer-order-edit");
        });
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("spend", Boolean.TRUE, Boolean.FALSE);
        filter.onSearchClick(e -> {
            Map<String, String> map = filter.getFilterData();
            map.put("typeOfInvoice", typeOfInvoice);
            paginator.setData(invoiceService.search(map));
        });
        filter.onClearClick(e -> paginator.setData(invoiceService.getAll(typeOfInvoice)));
    }

    private Component getIsCheckedIcon(InvoiceDto invoiceDto) {
        if (invoiceDto.isSpend()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), textField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("??????????", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(event -> {
            salesEditCreateInvoiceView.resetView();
            salesEditCreateInvoiceView.setUpdateState(false);
            salesEditCreateInvoiceView.setType("RECEIPT");
            salesEditCreateInvoiceView.setLocation("sells");
            buttonUnit.getUI().ifPresent(ui -> ui.navigate("sells/customer-order-edit"));
        });
        return buttonUnit;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("????????????");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private TextField textField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("?????????? ?????? ??????????????????????");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateList(textField.getValue()));
        return textField;
    }

    private H2 title() {
        H2 title = new H2("???????????? ??????????????????????");
        title.setHeight("2.2em");
        return title;
    }

    private Select<String> valueSelect() {
        Select<String> select = new Select<>();
        List<String> listItems = new ArrayList<>();
        listItems.add("????????????????");
        listItems.add("??????????????");
        select.setItems(listItems);
        select.setValue("????????????????");
        select.setWidth("130px");
        select.addValueChangeListener(event -> {
            if (select.getValue().equals("??????????????")) {
                deleteSelectedInvoices();
                grid.deselectAll();
                select.setValue("????????????????");
                paginator.setData(getData());
            }
        });
        return select;
    }

    private Select<String> valueStatus() {
        Select<String> status = new Select<>();
        status.setItems("????????????");
        status.setValue("????????????");
        status.setWidth("130px");
        return status;
    }

    private Select<String> valueCreate() {
        Select<String> create = new Select<>();
        create.setItems("??????????????");
        create.setValue("??????????????");
        create.setWidth("130px");
        return create;
    }

    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setItems("????????????", "???????????????? ????????????");
        print.setValue("????????????");
        getXlsFile().forEach(x -> print.add(getLinkToSalesXls(x)));
        uploadXlsTemplates(print);
        print.setWidth("130px");
        return print;
    }

    private void uploadXlsTemplates(Select<String> print) {
        Dialog dialog = new Dialog();
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        configureUploadFinishedListener(upload, buffer, dialog, print);
        dialog.add(upload);
        print.addValueChangeListener(x -> {
            if (x.getValue().equals("???????????????? ????????????")) {
                dialog.open();
            }
        });
    }

    private void configureUploadFinishedListener(Upload upload, MemoryBuffer buffer, Dialog dialog, Select<String> print) {
        upload.addFinishedListener(event -> {
            if (getXlsFile().stream().map(File::getName).anyMatch(x -> x.equals(event.getFileName()))) {
                getErrorNotification("???????? ?? ???????? ???????????? ?????? ????????????????????");
            } else {
                File exelTemplate = new File(pathForSaveSalesXlsTemplate + event.getFileName());
                try (FileOutputStream fos = new FileOutputStream(exelTemplate)) {
                    fos.write(buffer.getInputStream().readAllBytes());
                    print.removeAll();
                    getXlsFile().forEach(x -> print.add(getLinkToSalesXls(x)));
                    log.info("xls ???????????? ?????????????? ????????????????");
                    getInfoNotification("???????? ?????????????? ????????????????");
                } catch (IOException e) {
                    getErrorNotification("?????? ???????????????? ?????????????? ?????????????????? ????????????");
                    log.error("?????? ???????????????? xls ?????????????? ?????????????????? ????????????");
                }
                dialog.close();
            }
        });
    }


    private List<File> getXlsFile() {
        File dir = new File(pathForSaveSalesXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(File::isFile).filter(x -> x.getName().contains(".xls"))
                .collect(Collectors.toList());
    }

    private Anchor getLinkToSalesXls(File file) {
        String salesTemplate = file.getName();
        List<String> sumList = new ArrayList<>();
        List<InvoiceDto> list1 = invoiceService.getAll(typeOfInvoice);
        for (InvoiceDto inc : list1) {
            sumList.add(getTotalPrice(inc));
        }
        PrintSalesXls printSalesXls = new PrintSalesXls(file.getPath(), invoiceService.getAll(typeOfInvoice),
                sumList, employeeService);
        return new Anchor(new StreamResource(salesTemplate, printSalesXls::createReport), salesTemplate);
    }

    private void updateList() {

        GridPaginator<InvoiceDto> paginatorUpdateList = new GridPaginator<>(grid, invoiceService.getAll(),100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        GridSortOrder<InvoiceDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        removeAll();
        add(upperLayout(), grid, paginator);
    }

    private void updateList(String text) {
        grid.setItems(invoiceService.findBySearchAndTypeOfInvoice(text, typeOfInvoice));
    }

    private String getTotalPrice(InvoiceDto invoiceDto) {
        List<InvoiceProductDto> invoiceProductDtoList = salesEditCreateInvoiceView.getListOfInvoiceProductByInvoice(invoiceDto);
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (InvoiceProductDto invoiceProductDto : invoiceProductDtoList) {
            totalPrice = totalPrice.add(invoiceProductDto.getPrice()
                    .multiply(invoiceProductDto.getAmount()));
        }
        return String.format("%.2f", totalPrice);
    }

    private String formatDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(stringDate);
        return formatDateTime.format(formatter);
    }

    private List<InvoiceDto> getData() {
        return invoiceService.getAll(typeOfInvoice);
    }

    private void deleteSelectedInvoices() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (InvoiceDto invoiceDto : grid.getSelectedItems()) {
                invoiceService.deleteById(invoiceDto.getId());
                notifications.infoNotification("?????????????????? ???????????? ?????????????? ??????????????");
            }
        } else {
            notifications.errorNotification("?????????????? ???????????????? ?????????????????? ???????????? ????????????");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

    private void getInfoNotification(String message) {
        Notification notification = new Notification(message, 5000);
        notification.open();
    }

    private void getErrorNotification(String message) {
        Div content = new Div();
        content.addClassName("my-style");
        content.setText(message);
        Notification notification = new Notification(content);
        notification.setDuration(5000);
        String styles = ".my-style { color: red; }";
        StreamRegistration resource = UI.getCurrent().getSession()
                .getResourceRegistry()
                .registerResource(new StreamResource("styles.css", () ->
                        new ByteArrayInputStream(styles.getBytes(StandardCharsets.UTF_8))));
        UI.getCurrent().getPage().addStyleSheet(
                "base://" + resource.getResourceUri().toString());
        notification.open();
    }
}


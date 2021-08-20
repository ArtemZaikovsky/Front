package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.PayoutDto;
import com.trade_accounting.models.dto.RetailStoreDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.PayoutService;
import com.trade_accounting.services.interfaces.RetailStoreService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "PayoutTabView", layout = AppView.class)
@PageTitle("Выплаты")
@SpringComponent
@UIScope
public class PayoutTabView extends VerticalLayout implements AfterNavigationObserver {

    private final PayoutService payoutService;
    private final RetailStoreService retailStoreService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;

    private final Grid<PayoutDto> grid = new Grid<>(PayoutDto.class, false);
    private final GridPaginator<PayoutDto> paginator;
    private final Notifications notifications;

    private final List<PayoutDto> data;

    private final TextField textFieldUpdateTextField = new TextField();
    private final String typeOfInvoice = "RECEIPT";
    private final String pathForSaveSalesXlsTemplate = "src/main/resources/xls_templates/payouts_templates/";

    public PayoutTabView(PayoutService payoutService, RetailStoreService retailStoreService,
                         CompanyService companyService, EmployeeService employeeService, Notifications notifications) {
        this.payoutService = payoutService;
        this.retailStoreService = retailStoreService;
        this.companyService = companyService;
        this.data = payoutService.getAll();
        this.employeeService = employeeService;
        this.notifications = notifications;
        configureGrid();
        this.paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(upperLayout(), grid, paginator);
    }

    private void configureGrid() {
        grid.addColumn("id").setHeader("№").setSortable(true).setId("№");
        grid.addColumn(dto -> formatDate(dto.getDate())).setKey("date").setHeader("Время").setSortable(true).setId("Дата");
        grid.addColumn(dto -> retailStoreService.getById(dto.getRetailStoreId()).getName()).setHeader("Точка продаж")
                .setSortable(true).setKey("retailDto").setId("Точка продаж");
        grid.addColumn("whoWasPaid").setHeader("Кому").setId("Кому");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Организация")
                .setSortable(true).setKey("companyDto").setId("Организация");
        grid.addColumn(dto -> retailStoreService.getById(dto.getRetailStoreId()).getRevenue()).setHeader("Сумма")
                .setSortable(true).setKey("retailSumDto").setId("Сумма");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedSend)).setKey("send").setHeader("Отправлено")
                .setSortable(true).setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedPrint)).setKey("print").setHeader("Напечатано")
                .setSortable(true).setId("Напечатано");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.setHeight("66vh");
        grid.getColumnByKey("id").setWidth("15px");
        GridSortOrder<PayoutDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        grid.setColumnReorderingAllowed(true);
    }

    private Component getIsCheckedSend(PayoutDto dto) {
        if (dto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsCheckedPrint(PayoutDto dto) {
        if (dto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private String formatDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(stringDate);
        return formatDateTime.format(formatter);
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonFilter(), textField(),
                numberField(), valueSelect(), valueStatus(), valuePrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Выплаты");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Dialog modal = new Dialog();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Html content = new Html("<div><p>Выплаты фиксируют выдачу наличных денег из точки продаж.</p>" +
                "<p>Читать инструкцию: <a href=\"#\" target=\"_blank\">Выплаты</a></p></div>");
        Button close = new Button(new Icon(VaadinIcon.CLOSE));
        close.setWidth("30px");
        close.addClickListener(e -> modal.close());
        horizontalLayout.add(content, new Div(close));
        modal.add(horizontalLayout);
        modal.setWidth("500px");
        modal.setHeight("150px");
        buttonQuestion.addClickListener(e -> modal.open());
        Shortcuts.addShortcutListener(modal, modal::close, Key.ESCAPE);
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        return buttonFilter;
    }

    private TextField textField() {
        textFieldUpdateTextField.setPlaceholder("Кому или комментарий");
        textFieldUpdateTextField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textFieldUpdateTextField.setWidth("300px");
        textFieldUpdateTextField.setValueChangeMode(ValueChangeMode.EAGER);
        textFieldUpdateTextField.addValueChangeListener(e ->
                updateListTextField());
        return textFieldUpdateTextField;
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> select = new Select<>();
        List<String> listItems = new ArrayList<>();
        listItems.add("Изменить");
        listItems.add("Удалить");
        select.setItems(listItems);
        select.setValue("Изменить");
        select.setWidth("130px");
        select.addValueChangeListener(event -> {
            if (select.getValue().equals("Удалить")) {
                deleteSelectedInvoices();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(getData());
            }
        });
        return select;
    }

    private void deleteSelectedInvoices() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (PayoutDto payoutDto : grid.getSelectedItems()) {
                payoutService.deleteById(payoutDto.getId());
                notifications.infoNotification("Выбранные выплаты успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные выплаты");
        }
    }

    private Select<String> valueStatus() {
        Select<String> status = new Select<>();
        status.setItems("Статус");
        status.setValue("Статус");
        status.setWidth("130px");
        return status;
    }

    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать", "Добавить шаблон");
        print.setValue("Печать");
        getXlsFile().forEach(x -> print.add(getLinkToSalesXls(x)));
        uploadXlsTemplates(print);
        print.setWidth("130px");
        return print;
    }


    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }


    private void updateList() {
        GridPaginator<PayoutDto> paginatorUpdateList
                = new GridPaginator<>(grid, payoutService.getAll(), 100);
        GridSortOrder<PayoutDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(upperLayout(), grid, paginatorUpdateList);
    }

    public void updateListTextField() {
        if (!(textFieldUpdateTextField.getValue().equals(""))) {
            grid.setItems(payoutService.getAllByParameters(textFieldUpdateTextField.getValue()));
        } else {
            grid.setItems(payoutService.getAllByParameters("null"));
        }
    }

    //Поправить метод(после добавления select на бэке)
    private List<PayoutDto> getData() {
        return payoutService.getAll();
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
        List<RetailStoreDto> list1 = retailStoreService.getAll();
        for (RetailStoreDto rS : list1) {
            sumList.add(getTotalPrice(rS));
        }
        PrintPayoutXls printSalesXls = new PrintPayoutXls(file.getPath(), payoutService.getAll(),
                sumList, employeeService);
        return new Anchor(new StreamResource(salesTemplate, printSalesXls::createReport), salesTemplate);
    }

    private String getTotalPrice(RetailStoreDto retailStoreDto) {
        List<RetailStoreDto> payoutDtoList = retailStoreService.getAll();
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (RetailStoreDto retailStoreDto1 : payoutDtoList) {
            totalPrice = totalPrice.add(retailStoreDto.getRevenue()
                    .multiply(retailStoreDto1.getRevenue()));
        }
        return String.format("%.2f", totalPrice);
    }

    private void uploadXlsTemplates(Select<String> print) {
        Dialog dialog = new Dialog();
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        configureUploadFinishedListener(upload, buffer, dialog, print);
        dialog.add(upload);
        print.addValueChangeListener(x -> {
            if (x.getValue().equals("Добавить шаблон")) {
                dialog.open();
            }
        });
    }

    private void configureUploadFinishedListener(Upload upload, MemoryBuffer buffer, Dialog dialog, Select<String> print) {
        upload.addFinishedListener(event -> {
            if (getXlsFile().stream().map(File::getName).anyMatch(x -> x.equals(event.getFileName()))) {
                getErrorNotification("Файл с таки именем уже существует");
            } else {
                File exelTemplate = new File(pathForSaveSalesXlsTemplate + event.getFileName());
                try (FileOutputStream fos = new FileOutputStream(exelTemplate)) {
                    fos.write(buffer.getInputStream().readAllBytes());
                    print.removeAll();
                    getXlsFile().forEach(x -> print.add(getLinkToSalesXls(x)));
                    log.info("xls шаблон успешно загружен");
                    getInfoNotification("Файл успешно загружен");
                } catch (IOException e) {
                    getErrorNotification("При загрузке шаблона произошла ошибка");
                    log.error("при загрузке xls шаблона произошла ошибка");
                }
                dialog.close();
            }
        });
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

    private void getInfoNotification(String message) {
        Notification notification = new Notification(message, 5000);
        notification.open();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}

package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.purchases.ReturnToSupplierModalView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.BalanceAdjustmentDto;
import com.trade_accounting.models.dto.ReturnToSupplierDto;
import com.trade_accounting.services.interfaces.BalanceAdjustmentService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Route(value = "balanceAdjustment", layout = AppView.class)
@PageTitle("Корректировки")
@SpringComponent
@UIScope
public class MoneySubBalanceAdjustmentView extends VerticalLayout implements AfterNavigationObserver {

    private final BalanceAdjustmentService balanceAdjustmentService;
    private final CompanyService companyService;
    private final ContractorService contractorService;

    private final Notifications notifications;
    private final BalanceAdjustmentModalView modalView;

    private final List<BalanceAdjustmentDto> data;

    private final Grid<BalanceAdjustmentDto> grid = new Grid<>(BalanceAdjustmentDto.class, false);
    private GridPaginator<BalanceAdjustmentDto> paginator;
    private final GridFilter<BalanceAdjustmentDto> filter;

    private final TextField textField = new TextField();

    @Autowired
    public MoneySubBalanceAdjustmentView(BalanceAdjustmentService balanceAdjustmentService, CompanyService companyService,
                                         ContractorService contractorService, @Lazy Notifications notifications,
                                         BalanceAdjustmentModalView modalView) {
        this.balanceAdjustmentService = balanceAdjustmentService;
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.notifications = notifications;
        this.modalView = modalView;
        this.data = loadBalanceAdjustments();
        paginator = new GridPaginator<>(grid, data, 50);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        add(configureActions(), filter, grid, paginator);
    }

    private HorizontalLayout configureActions() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonAdd(),
                buttonFilter(), filterTextField(), numberField(), valueSelect(),
                valuePrint(), buttonSettings());
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Grid<BalanceAdjustmentDto> configureGrid() {
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getDate())).setKey("date").setHeader("Время").setSortable(true).setId("Дата");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Организация")
                .setKey("companyDto").setId("Организация");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName()).setHeader("Контрагент")
                .setKey("contractorDto").setId("Контрагент");
        grid.addColumn("account").setHeader("Счет").setId("Счет");
        grid.addColumn("cashOffice").setHeader("Касса").setId("Касса");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setSortable(true);
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.addColumn("dateChanged").setHeader("Когда изменен").setId("Когда изменен");
        grid.addColumn("whoChanged").setHeader("Кто изменил").setId("Кто изменил");
        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(e -> {
            BalanceAdjustmentDto dto = e.getItem();
            BalanceAdjustmentModalView modalView = new BalanceAdjustmentModalView(balanceAdjustmentService,
                    companyService,
                    contractorService,
                    notifications);
            modalView.setBalanceAdjustmentForEdit(dto);
            modalView.open();
        });
        return grid;
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.onSearchClick(e -> paginator
                .setData(balanceAdjustmentService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(balanceAdjustmentService.getAll()));
    }

    private List<BalanceAdjustmentDto> loadBalanceAdjustments() {
        return balanceAdjustmentService.getAll();
    }

    private H2 title() {
        H2 title = new H2("Корректировки");
        title.setHeight("2.2em");
        return title;
    }

    private com.vaadin.flow.component.button.Button buttonQuestion() {
        com.vaadin.flow.component.button.Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button button = new Button(new Icon(VaadinIcon.REFRESH));
        button.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        button.addClickListener(e -> updateList());
        return button;
    }

    private Button buttonAdd() {
        Button button = new Button("Корректировка", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(e -> modalView.open());
        updateList();
        return button;
    }

    private Button buttonFilter() {
        Button button = new Button("Фильтр");
        button.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return button;
    }

    private TextField filterTextField() {
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addValueChangeListener(e -> updateList(textField.getValue()));
        textField.setWidth("300px");
        return textField;
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> select = new Select<>();
        List<String> stringList = new ArrayList<>();
        stringList.add("Изменить");
        stringList.add("Удалить");
        stringList.add("Массовое редактирование");
        select.setItems(stringList);
        select.setValue("Изменить");
        select.setWidth("130px");
        select.addValueChangeListener(e -> {
            if (select.getValue().equals("Удалить")) {
                deleteSelectBalanceAdjustments();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(loadBalanceAdjustments());
            }
        });
        return select;
    }

    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать");
        print.setValue("Печать");
        print.setWidth("130px");
        return print;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private String formatDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(stringDate);
        return formatDateTime.format(formatter);
    }

    private void updateList() {
        grid.setItems(balanceAdjustmentService.getAll());
    }

    public void updateList(String nameFilter) {
        if (!(textField.getValue().equals(""))) {
            grid.setItems(balanceAdjustmentService.searchByString(nameFilter));
        } else {
            grid.setItems(balanceAdjustmentService.searchByString("null"));
        }
    }

    private String getTotalPrice(BalanceAdjustmentDto dto) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        return String.format("%.2f", totalPrice);
    }

    private void deleteSelectBalanceAdjustments() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (BalanceAdjustmentDto item : grid.getSelectedItems()) {
                balanceAdjustmentService.deleteById(item.getId());
                notifications.infoNotification("Выбранные корректировки успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные корректировки");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
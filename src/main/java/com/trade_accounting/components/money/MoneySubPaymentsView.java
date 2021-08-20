package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.PaymentDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.PaymentService;
import com.trade_accounting.services.interfaces.ProjectService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Route(value = "MoneySubPaymentsView", layout = AppView.class)
@PageTitle("Платежи")
public class MoneySubPaymentsView extends VerticalLayout {
    private final PaymentService paymentService;
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private final ProjectService projectService;
    private final ContractService contractService;
    private final Notifications notifications;

    private final List<PaymentDto> data;
    private final Grid<PaymentDto> grid = new Grid<>(PaymentDto.class, false);
    private final GridPaginator<PaymentDto> paginator;
    private final GridFilter<PaymentDto> filter;
    private final PaymentModalWin paymentModalWin;

    MoneySubPaymentsView(PaymentService paymentService,
                         CompanyService companyService,
                         ContractorService contractorService,
                         ProjectService projectService,
                         ContractService contractService,
                         Notifications notifications,
                         PaymentModalWin paymentModalWin) {
        this.paymentService = paymentService;
        this.data = paymentService.getAll();
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.projectService = projectService;
        this.contractService = contractService;
        this.notifications = notifications;
        this.paymentModalWin = paymentModalWin;

        getGrid();
        this.paginator = new GridPaginator<>(grid, data, 100);
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getToolbar(), filter, grid, paginator);
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("time");
        filter.setFieldToIntegerField("sum");
        filter.setFieldToIntegerField("number");
        filter.setFieldToComboBox("typeOfPayment", "Входящий", "Исходящий");
        filter.setFieldToIntegerField("contractDto");
        filter.onSearchClick(e -> paginator.setData(paymentService.filter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(paymentService.getAll()));
    }

    private Grid getGrid() {
        grid.setItems(data);
        grid.addColumn("id").setHeader("ID").setId("№");
        grid.addColumn("time").setFlexGrow(10).setHeader("Дата").setId("Дата");
        grid.addColumn(pDto -> pDto.getCompanyDto().getName()).setFlexGrow(10).setSortable(true)
                .setKey("companyDto").setHeader("Компания").setId("Компания");
        grid.addColumn("sum").setFlexGrow(4).setHeader("Сумма").setId("Сумма");
        grid.addColumn("number").setFlexGrow(4).setHeader("Номер платеж").setId("Номер платежа");
        grid.addColumn("typeOfPayment").setFlexGrow(4).setHeader("Тип платежа").setId("Тип платежа");
        grid.addColumn("paymentMethods").setFlexGrow(4).setHeader("Способ Оплаты").setId("Способ оплаты");
        grid.addColumn(pDto -> pDto.getContractorDto().getName()).setFlexGrow(10).setSortable(true)
                .setKey("contractorDto").setHeader("Контрагент").setId("Контрагент");
        grid.addColumn(pDto -> pDto.getContractDto().getNumber()).setFlexGrow(3).setSortable(true)
                .setKey("contractDto").setHeader("Договор").setId("Договор");
        grid.addColumn(pDto -> pDto.getProjectDto().getName()).setFlexGrow(7).setSortable(true)
                .setKey("projectDto").setHeader("Проект").setId("Проект");
        grid.setHeight("66vh");
        grid.addItemDoubleClickListener(event -> {
            PaymentDto editPaymentDto = event.getItem();
            PaymentModalWin addPaymentModalWin = new PaymentModalWin(
                    paymentService,
                    companyService,
                    contractorService,
                    projectService,
                    contractService,
                    notifications);
            addPaymentModalWin.addDetachListener(e -> updateList());
            addPaymentModalWin.setPaymentDataForEdit(editPaymentDto);
            addPaymentModalWin.open();
        });
        return grid;
    }

    private void updateList() {
        GridPaginator<PaymentDto> paginatorUpdateList
                = new GridPaginator<>(grid, paymentService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(getToolbar(), grid, paginator);
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextContract(), getButtonRefresh(), getButton(),
                getButtonFilter(), getTextField(), getNumberField(), getSelect(), getPrint(), getButtonCog());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    private Button getButtonCog() {
        final Button buttonCog = new Button();
        buttonCog.setIcon(new Icon(VaadinIcon.COG_O));
        return buttonCog;
    }

    private NumberField getNumberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private TextField getTextField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Наименование или код");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateList(textField.getValue()));
        return textField;
    }

    private void updateList(String search) {
        if (search.isEmpty()) {
            paginator.setData(paymentService.getAll());
        } else paginator.setData(paymentService.search(search));
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private Button getButton() {
        final Button button = new Button("Платеж");
        button.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(event -> paymentModalWin.open());
        return button;
    }

    private Button getButtonRefresh() {
        final Button buttonRefresh;
        Icon circle = new Icon(VaadinIcon.REFRESH);
        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private H2 getTextContract() {
        final H2 textCompany = new H2("Платежи");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    private Button getButtonQuestion() {
        final Button buttonQuestion = new Button();
        Icon question = new Icon(VaadinIcon.QUESTION_CIRCLE_O);
        buttonQuestion.setIcon(question);
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Select<String> getSelect() {
        final Select<String> selector = new Select<>();
        selector.setItems("Изменить");
        selector.setValue("Изменить");
        selector.setWidth("130px");
        return selector;
    }

    private Select<String> getPrint() {
        Select getPrint = new Select();
        getPrint.setWidth("130px");
        getPrint.setItems("Печать", "Список всех платежей");
        getPrint.setValue("Печать");
        uploadListAllPays(getPrint);
        return getPrint;
    }

    private void uploadListAllPays(Select<String> print) {
        PaymentPrintModal paymentPrintModal = new PaymentPrintModal(paymentService);
        print.addValueChangeListener(x -> {
            if (x.getValue().equals("Список всех платежей")) {
                paymentPrintModal.open();
            }
        });
    }
}

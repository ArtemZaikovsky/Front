package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.MoneySubCashFlowDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.MoneySubCashFlowService;
import com.trade_accounting.services.interfaces.ProjectService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "MoneySubCashFlowView", layout = AppView.class)
@PageTitle("Движение денежных средств")
public class MoneySubCashFlowView extends VerticalLayout {

    private final MoneySubCashFlowService moneySubCashFlowService;
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private final ProjectService projectService;
    private final ContractService contractService;
    private final Notifications notifications;

    private final List<MoneySubCashFlowDto> data;
    private final Grid<MoneySubCashFlowDto> grid = new Grid<>(MoneySubCashFlowDto.class, false);
    private final GridPaginator<MoneySubCashFlowDto> paginator;
    private final GridFilter<MoneySubCashFlowDto> filter;
    private final PaymentModalWin paymentModalWin;

    private H2 title() {
        H2 title = new H2("Движение денежных средств");
        title.setHeight("2.2em");
        return title;
    }

    public MoneySubCashFlowView(MoneySubCashFlowService moneySubCashFlowService,
                                CompanyService companyService,
                                ContractorService contractorService,
                                ProjectService projectService,
                                ContractService contractService,
                                Notifications notifications,
                                PaymentModalWin paymentModalWin) {
        this.moneySubCashFlowService = moneySubCashFlowService;
        this.companyService = companyService;
        this.data = moneySubCashFlowService.getAll();
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
        filter.setFieldToDatePicker("time");
//        filter.setFieldToIntegerField("sum");
//        filter.setFieldToIntegerField("number");
//        filter.onSearchClick(e -> paginator.setData(paymentService.filter(filter.getFilterData())));
//        filter.onClearClick(e -> paginator.setData(paymentService.getAll()));
    }

    private Grid getGrid() {
        grid.setItems(data);
        grid.addColumn("time").setFlexGrow(10).setHeader("Дата").setId("Дата");
        grid.addColumn("bankcoming").setFlexGrow(7).setHeader("Банк Приход").setId("Банк Приход");
        grid.addColumn("bankexpense").setFlexGrow(7).setHeader("Банк Расход").setId("Банк Расход");
        grid.addColumn("bankbalance").setFlexGrow(7).setHeader("Банк Баланс").setId("Банк Баланс");
        grid.addColumn("cashcoming").setFlexGrow(7).setHeader("Касса Приход").setId("Касса Приход");
        grid.addColumn("cashexpense").setFlexGrow(7).setHeader("Касса Расход").setId("Касса Расход");
        grid.addColumn("cashbalance").setFlexGrow(7).setHeader("Касса Баланс").setId("Касса Баланс");
        grid.addColumn("allcoming").setFlexGrow(7).setHeader("Все Приход").setId("Все Приход");
        grid.addColumn("allexpense").setFlexGrow(7).setHeader("Все Расход").setId("Все Расход");
        grid.addColumn("allbalance").setFlexGrow(7).setHeader("Все Баланс").setId("Все Баланс");
        return grid;
    }

    private void updateList() {
        GridPaginator<MoneySubCashFlowDto> paginatorUpdateList
                = new GridPaginator<>(grid, moneySubCashFlowService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(getToolbar(), grid, paginator);
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextContract(), getButtonRefresh(),
                getButtonFilter(), getPrint());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
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
        final H2 textCompany = new H2("Движение денежных средств");
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


    private Select<String> getPrint() {
        Select getPrint = new Select();
        getPrint.setWidth("130px");
        getPrint.setItems("Печать", "Движение денежных средств");
        getPrint.setValue("Печать");
//        uploadListCashFlow(getPrint);
        return getPrint;
    }

}

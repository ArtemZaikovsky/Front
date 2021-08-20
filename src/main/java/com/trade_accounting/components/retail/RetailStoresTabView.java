package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.models.dto.RetailStoreDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.RetailStoreService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "RetailStoresTabView", layout = AppView.class)
@PageTitle("Точки продаж")
@SpringComponent
@UIScope
public class RetailStoresTabView extends VerticalLayout implements AfterNavigationObserver { //в задаче сказано, что проблема ещё и здесь

    private final RetailStoreService retailStoreService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final RetailStoreModalWindow retailStoreModalWindow;
    private List<RetailStoreDto> data;

    private final Grid<RetailStoreDto> grid = new Grid<>(RetailStoreDto.class, false);
    private final GridPaginator<RetailStoreDto> paginator;

    public RetailStoresTabView(RetailStoreService retailStoreService,
                               CompanyService companyService, EmployeeService employeeService) {
        this.retailStoreService = retailStoreService;
        this.data = retailStoreService.getAll();
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.retailStoreModalWindow = new RetailStoreModalWindow(retailStoreService, companyService, employeeService);
        configureGrid();
        this.paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(upperLayout(), grid, paginator);
    }

    private void configureGrid() {
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(new ComponentRenderer<>(this::isActiveCheckedIcon)).setKey("isActive").setHeader("Включена")
                .setId("Включена");
        grid.addColumn("name").setHeader("Наименование").setId("Наименование");
        grid.addColumn("activityStatus").setHeader("Активность").setId("Активность");
        grid.addColumn(unused -> "Мой склад").setHeader("Тип");
        grid.addColumn("revenue").setHeader("Выручка").setId("Выручка");
        grid.addColumn(unused -> "0").setHeader("Чеки").setWidth("20px");
        grid.addColumn(unused -> "0,00").setHeader("Средний чек").setWidth("20px");
        grid.addColumn(unused -> "0,00").setHeader("Денег в кассе").setWidth("20px");
        grid.addColumn(item -> item.getCashiersDto().stream().map(EmployeeDto::toString).collect(Collectors.joining(", ")))
                .setWidth("100px").setHeader("Кассиры").setId("Кассиры");
        grid.addColumn(unused -> "-").setHeader("Синхронизация");
        grid.addColumn(unused -> "Нет").setHeader("ФН").setWidth("20px");
        grid.setHeight("66vh");
        grid.getColumnByKey("id").setWidth("15px");
        grid.getColumnByKey("isActive").setWidth("30px");
        grid.getColumnByKey("name").setWidth("150px");
        grid.getColumnByKey("activityStatus").setWidth("150px");
        grid.addItemDoubleClickListener(event -> {
            RetailStoreDto retailStore = event.getItem();
            retailStoreModalWindow.setRetailStoreDataEdit(retailStore);
            retailStoreModalWindow.addDetachListener(e -> updateList());
            retailStoreModalWindow.open();
        });
        GridSortOrder<RetailStoreDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        grid.setColumnReorderingAllowed(true);
    }

    private Component isActiveCheckedIcon(RetailStoreDto retailStoreDto) {
        if (retailStoreDto.getIsActive()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonCreate(), buttonFilter());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Точки продаж");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonCreate() {
        Button createRetailStoreButton = new Button("Точка продаж", new Icon(VaadinIcon.PLUS_CIRCLE));
        RetailStoreModalWindow retailStoreModalWindow =
                new RetailStoreModalWindow(retailStoreService, companyService, employeeService);
        createRetailStoreButton.addClickListener(e -> {
            retailStoreModalWindow.addDetachListener(event -> updateList());
            retailStoreModalWindow.open();
        });
        createRetailStoreButton.getStyle().set("cursor", "pointer");
        return createRetailStoreButton;
    }

    private Button buttonFilter() {
        return new Button("Фильтр");
    }

    private void updateList() {
        GridPaginator<RetailStoreDto> paginatorUpdateList
                = new GridPaginator<>(grid, retailStoreService.getAll(), 100);
        GridSortOrder<RetailStoreDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(upperLayout(), grid, paginatorUpdateList);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}

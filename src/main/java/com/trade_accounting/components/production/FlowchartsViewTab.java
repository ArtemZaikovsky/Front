package com.trade_accounting.components.production;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.contractors.ContractorModalWindow;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.TechnicalCardDto;
import com.trade_accounting.models.dto.TechnicalCardGroupDto;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.TechnicalCardGroupService;
import com.trade_accounting.services.interfaces.TechnicalCardService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.List;

@SpringComponent
@UIScope
@PageTitle("Тех. карты")
@Route(value = "flowcharts", layout = AppView.class)
public class FlowchartsViewTab extends VerticalLayout {

    private final TextField text = new TextField();
    private final Grid<TechnicalCardDto> grid = new Grid<>(TechnicalCardDto.class, false);
    private final List<TechnicalCardDto> data;
    private final GridPaginator<TechnicalCardDto> paginator;
    private final TechnicalCardService technicalCardService;
    private final GridFilter<TechnicalCardDto> filter;
    private final TechnicalCardGroupDto technicalCardGroupDto;
    private final TechnicalCardGroupService technicalCardGroupService;
    private final TechnicalCardDto technicalCardDto;
    private final ProductService productService;
    private final List<ProductDto> productDtoList;

    public FlowchartsViewTab(TechnicalCardService technicalCardService,
                             TechnicalCardGroupService technicalCardGroupService,
                             ProductService productService) {
        this.technicalCardService = technicalCardService;
        this.technicalCardGroupService = technicalCardGroupService;
        this.productService = productService;
        this.technicalCardGroupDto = new TechnicalCardGroupDto();
        this.technicalCardDto = new TechnicalCardDto();
        this.productDtoList = productService.getAll();
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setSizeFull();
        this.data = getData();
        paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getToolBar(), filter, getLabelFlowchartsAndTable(), paginator);
    }

    private HorizontalLayout getToolBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonPlusFlowcharts(), buttonPlusGroup(),
                buttonFilter(), text(), bigDecimalField(), valueSelect());
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private H2 title() {
        H2 title = new H2("Тех. карты");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonPlusFlowcharts() {
        Button addFlowchartsButton = new Button("Тех. карта", new Icon(VaadinIcon.PLUS_CIRCLE));
        TechnicalCardModalWindow addTechnicalCardModal =
                new TechnicalCardModalWindow(new TechnicalCardDto(), technicalCardService, technicalCardGroupService, productDtoList);
        addFlowchartsButton.addClickListener(event -> addTechnicalCardModal.open());
        addTechnicalCardModal.addDetachListener(event -> updateList());
        addFlowchartsButton.getStyle().set("cursor", "pointer");
        return addFlowchartsButton;
    }

    private Button buttonPlusGroup() {
        Button buttonUnit = new Button("Группа", new Icon(VaadinIcon.PLUS_CIRCLE));
        TechnicalCardGroupModalWindow addTechnicalCardGroupModal =
                new TechnicalCardGroupModalWindow(technicalCardGroupDto, technicalCardGroupService);
        buttonUnit.addClickListener(event -> addTechnicalCardGroupModal.open());
        addTechnicalCardGroupModal.addDetachListener(event -> updateList());
        return buttonUnit;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private TextField text() {
        text.setPlaceholder("Наименование или комментарий");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setClearButtonVisible(true);
        text.setValueChangeMode(ValueChangeMode.EAGER);
        text.setWidth("300px");
        text.addValueChangeListener(e -> updateListTextField());
        setSizeFull();
        return text;
    }

    public void updateListTextField() {
        if (!(text.getValue().equals(""))) {
            grid.setItems(technicalCardService.search(text.getValue()));
        } else {
            grid.setItems(technicalCardService.search("null"));
        }
    }

    private BigDecimalField bigDecimalField() {
        BigDecimalField numberField = new BigDecimalField();
        numberField.setPlaceholder("0");
        numberField.setWidth("35px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setWidth("120px");
        valueSelect.setItems("Изменить");
        valueSelect.setValue("Изменить");
        return valueSelect;
    }

    private HorizontalLayout getLabelFlowchartsAndTable() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Тех. карты");
        label.setWidth("300px");
        label.setHeight("30px");
        label.getElement().getStyle().set("background-color", "#e4f1fa");
        horizontalLayout.add(label);
        horizontalLayout.add(grid);
        return horizontalLayout;
    }

    private void configureGrid() {
        grid.addColumn("id").setHeader("ID").setId("ID");
        grid.addColumn("name").setHeader("Наименование").setId("Наименование");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.addColumn("productionCost").setHeader("Затраты на производство").setId("Затраты на производство");
        grid.addColumn(iDto -> iDto.getTechnicalCardGroupDto().getName()).setHeader("Группа").setId("Группа");
        grid.setHeight("64vh");
        grid.setWidth("150vh");

        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private List<TechnicalCardDto> getData() {
        return technicalCardService.getAll();
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.onSearchClick(e ->
                paginator.setData(technicalCardService.searchTechnicalCard(filter.getFilterData())));
        filter.onClearClick(e ->
                paginator.setData(technicalCardService.getAll()));
    }

    private void updateList() {
        GridPaginator<TechnicalCardDto> paginatorUpdateList
                = new GridPaginator<>(grid, technicalCardService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(getToolBar(), filter, getLabelFlowchartsAndTable(), paginator);
    }
}

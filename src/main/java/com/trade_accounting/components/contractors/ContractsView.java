package com.trade_accounting.components.contractors;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

@Slf4j
@SpringComponent
@UIScope
@Route(value = "contracts", layout = AppView.class)
@PageTitle("Договоры")
public class ContractsView extends VerticalLayout {

    private final ContractService contractService;
    private final ContractorService contractorService;
    private final ContractModalWindow contractModalWindow;
    private final GridFilter<ContractDto> filter;
    private final GridPaginator<ContractDto> paginator;
    private final TextField textField = new TextField();
    private final Grid<ContractDto> grid;


    @Autowired
    ContractsView(ContractService contractService,
                  ContractorService contractorService,
                  ContractModalWindow contractModalWindow) {
        this.contractService = contractService;
        this.contractorService = contractorService;
        this.contractModalWindow = contractModalWindow;
        grid = new Grid<>(ContractDto.class);
        paginator = new GridPaginator<>(grid, contractService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        configureGrid();
        filter = new GridFilter<>(grid);
        configureFilter();
        add(getToolbar(), filter, grid, paginator);

//        GridSortOrder<ContractDto> gridSortOrder = new GridSortOrder(grid.getColumnByKey("number"), SortDirection.ASCENDING);
//        List<GridSortOrder<ContractDto>> gridSortOrderList = new ArrayList<>();
//        gridSortOrderList.add(gridSortOrder);
//        grid.sort(gridSortOrderList);

        contractModalWindow.addDetachListener(detachEvent -> reloadGrid());
    }

    private void configureGrid() {
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("id", "contractDate", "amount", "comment", "number");
        grid.getColumnByKey("id").setHeader("ID").setId("ID");
        grid.getColumnByKey("contractDate").setHeader("Дата заключения").setId("Дата заключения");
        grid.getColumnByKey("amount").setHeader("Сумма").setId("Сумма");
        grid.getColumnByKey("comment").setHeader("Комментарий").setId("Комментарий");
        grid.getColumnByKey("number").setHeader("Сортировочный номер").setId("Сортировочный номер");


        grid.addColumn(contractDto -> contractDto.getCompanyDto().getName())
                .setHeader("Компания").setKey("company").setId("Компания");
        grid.addColumn(contractDto -> contractDto.getContractorDto().getName())
                .setHeader("Контрагент").setKey("contractor").setId("Контрактор");
        grid.addColumn(contractDto -> contractDto.getBankAccountDto().getBank() + " " +
                contractDto.getBankAccountDto().getAccount())
                .setHeader("Банковский аккаунт").setKey("bankAccount").setId("Банковский аккаунт");
        grid.addColumn(new ComponentRenderer<>(contractDto -> {
            if (contractDto.getArchive()) {
                return new Icon(VaadinIcon.CHECK_CIRCLE);
            } else {
                Icon noIcon = new Icon(VaadinIcon.CIRCLE_THIN);
                noIcon.setColor("white");
                return noIcon;
            }
        })).setHeader("Архив").setKey("archive").setId("Архив");

        grid.addColumn(contractDto -> contractDto.getLegalDetailDto().getLastName() + " " +
                contractDto.getLegalDetailDto().getFirstName() + " " +
                contractDto.getLegalDetailDto().getMiddleName())
                .setHeader("Юридические детали").setKey("legalDetails").setId("Юридические детали");

        grid.setColumnOrder(
                grid.getColumnByKey("id"),
                grid.getColumnByKey("contractDate"),
                grid.getColumnByKey("amount"),
                grid.getColumnByKey("company"),
                grid.getColumnByKey("legalDetails"),
                grid.getColumnByKey("contractor"),
                grid.getColumnByKey("bankAccount"),
                grid.getColumnByKey("archive"),
                grid.getColumnByKey("comment"),
                grid.getColumnByKey("number"));


        grid.setHeight("66vh");
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.addItemDoubleClickListener(event -> {
            ContractDto editContract = event.getItem();
            contractModalWindow.configure(editContract);
            contractModalWindow.open();
        });
    }

    private void reloadGrid() {
        grid.setItems(contractService.getAll());
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToIntegerField("amount");
        filter.setFieldToDatePicker("contractDate");
        filter.setFieldToCheckBox("archive");
        filter.onSearchClick(e -> paginator.setData(contractService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(contractService.getAll()));
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextContract(), getButtonRefresh(), getButton(),
                getButtonFilter(), getTextField(), getNumberField(), getSelect(), getButtonCog());
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

    // WHERE!!!
    private TextField getTextField() {
        textField.setPlaceholder("Наименование или код");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(e -> updateTextField());
        setSizeFull();
        return textField;
    }

    public void updateTextField() {
        if (!(textField.getValue().equals(""))) {
            grid.setItems(contractService.searchByTerm(textField.getValue()));
        } else {
            grid.setItems(contractService.searchByTerm("null"));
        }
    }

    private Button getButtonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private Button getButton() {
        final Button button = new Button("Договор");
        button.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(event -> {
            contractModalWindow.configure();
            contractModalWindow.open();
        });
        return button;
    }

    private Button getButtonRefresh() {
        final Button buttonRefresh;
        Icon circle = new Icon(VaadinIcon.REFRESH);
        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(click -> reloadGrid());
        return buttonRefresh;
    }

    private H2 getTextContract() {
        final H2 textCompany = new H2("Договоры");
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
}

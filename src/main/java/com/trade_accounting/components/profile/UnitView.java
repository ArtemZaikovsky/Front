package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.UnitDto;
import com.trade_accounting.services.interfaces.UnitService;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = "unit", layout = AppView.class)
@PageTitle("Единицы измерения")
public class UnitView extends VerticalLayout {

    private final UnitService unitService;
    private Grid<UnitDto> grid;

    public UnitView(UnitService unitService) {
        this.unitService = unitService;
        updateList();
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextCompany(), getButtonRefresh(), getButton(),
                getButtonFilter(), getTextField(), getNumberField(), getSelect(), getButtonCog());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    private void getGrid() {
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("id", "shortName", "fullName", "sortNumber");
        grid.getColumnByKey("shortName").setHeader("Краткое наименование");
        grid.getColumnByKey("fullName").setHeader("Полное наименование");
        grid.getColumnByKey("sortNumber").setHeader("Сортировочный номер");
        grid.setHeight("64vh");
        grid.addItemDoubleClickListener(event -> {
            UnitDto editUnit = event.getItem();
            UnitModalWindow unitModalWindow =
                    new UnitModalWindow(editUnit, unitService);
            unitModalWindow.addDetachListener(e -> updateList());
            unitModalWindow.open();
        });
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
        return textField;
    }

    private Button getButtonFilter() {
        return new Button("Фильтр");
    }

    private Button getButton() {
        final Button button = new Button("Единица измерения");
        button.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        UnitModalWindow unitModalWindow =
                new UnitModalWindow(new UnitDto(), unitService);
        button.addClickListener(event -> unitModalWindow.open());
        unitModalWindow.addDetachListener(event -> updateList());
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

    private H2 getTextCompany() {
        final H2 textCompany = new H2("Единицы измерения");
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

    private void updateList() {
        grid = new Grid<>(UnitDto.class);
        GridPaginator<UnitDto> paginator = new GridPaginator<>(grid, unitService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        getGrid();
        removeAll();
        add(getToolbar(), grid, paginator);
        GridSortOrder<UnitDto> gridSortOrder = new GridSortOrder(grid.getColumnByKey("sortNumber"), SortDirection.ASCENDING);
        List<GridSortOrder<UnitDto>> gridSortOrderList = new ArrayList<>();
        gridSortOrderList.add(gridSortOrder);
        grid.sort(gridSortOrderList);
    }
}
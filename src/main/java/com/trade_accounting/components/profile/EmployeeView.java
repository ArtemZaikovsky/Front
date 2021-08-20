package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.LazyPaginator;
import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.models.dto.ImageDto;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.ImageService;
import com.trade_accounting.services.interfaces.RoleService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Route(value = "employee", layout = AppView.class)
@PageTitle("Сотрудники")
public class EmployeeView extends VerticalLayout {

    private final Grid<EmployeeDto> grid;
    private final EmployeeService employeeService;
    private final RoleService roleService;
    private final ImageService imageService;
    private final GridFilter<EmployeeDto> filter;
    private final LazyPaginator<EmployeeDto> lazyPaginator;

    public EmployeeView(EmployeeService employeeService, RoleService roleService, ImageService imageService) {
        this.employeeService = employeeService;
        this.roleService = roleService;
        this.imageService = imageService;
        this.grid = new Grid<>(EmployeeDto.class);

        configureGrid();
        this.filter = new GridFilter<>(grid);
        this.lazyPaginator = new LazyPaginator<>(grid, employeeService, 2, filter);

        setHorizontalComponentAlignment(Alignment.CENTER, lazyPaginator);
        add(upperLayout(), filter, grid, lazyPaginator);
    }

    private void updateGrid() {
        lazyPaginator.updateData(false);
        log.info("Таблица обновилась");
    }

    private void configureGrid() {
        grid.removeAllColumns();

        grid.addColumn("lastName").setHeader("Фамилия").setId("Фамилия");
        Grid.Column<EmployeeDto> photoColumn = grid.addColumn(new ComponentRenderer<>() {
            @Override
            public Component createComponent(EmployeeDto item) {
                ImageDto imageDto = item.getImageDto();
                if (imageDto != null) {
                    Image image = new Image(new StreamResource("image", () ->
                            new ByteArrayInputStream(imageDto.getContent())), "");
                    image.setHeight("48px");

                    return image;
                }
                return new Label();
            }
        }).setHeader("Фото");
        photoColumn.setKey("imageDto").setId("Фото");
        grid.addColumn("firstName").setHeader("Имя").setId("Имя");
        grid.addColumn("middleName").setHeader("Отчество").setId("Отчество");
        grid.addColumn("email").setHeader("E-mail").setId("E-mail");
        grid.addColumn("phone").setHeader("Телефон").setId("Телефон");
        grid.addColumn("description").setHeader("Описание").setId("Описание");
        grid.addColumn("roleDto").setHeader("Роль").setId("Роль");
        grid.setHeight("64vh");
        grid.addItemDoubleClickListener(event -> {
            EmployeeDto employeeDto = event.getItem();
            ImageDto imageDto = employeeDto.getImageDto();
            AddEmployeeModalWindowView addEmployeeModalWindowView =
                    new AddEmployeeModalWindowView(
                            employeeDto,
                            employeeService,
                            roleService,
                            imageService,
                            imageDto);
            addEmployeeModalWindowView.addDetachListener(e -> updateGrid());
            addEmployeeModalWindowView.open();
        });
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(ev -> updateGrid());
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Сотрудник", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickShortcut(Key.ENTER);
        buttonUnit.addClickListener(click -> {
            log.info("Вы нажали кнопку для добавление сотрудника!");
            AddEmployeeModalWindowView addEmployeeModalWindowView =
                    new AddEmployeeModalWindowView(
                            null,
                            employeeService,
                            roleService,
                            imageService,
                            new ImageDto());
            addEmployeeModalWindowView.addDetachListener(event -> updateGrid());
            addEmployeeModalWindowView.isModal();
            addEmployeeModalWindowView.open();
        });
        return buttonUnit;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private TextField text() {
        TextField text = new TextField();
        text.setPlaceholder("Поиск");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setWidth("300px");
        text.setValueChangeMode(ValueChangeMode.EAGER);
        text.addValueChangeListener(e -> fillList(text.getValue()));
        return text;
    }

    private void fillList(String text) {
        lazyPaginator.setSearchText(text);
    }

    private H2 title() {
        H2 title = new H2("Сотрудники");
        title.setHeight("2.2em");
        return title;
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setItems("Изменить");
        valueSelect.setValue("Изменить");
        valueSelect.setWidth("130px");
        return valueSelect;
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), text(), numberField(),
                valueSelect(), buttonSettings());
        upperLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upperLayout;
    }

}

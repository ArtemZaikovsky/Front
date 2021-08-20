package com.trade_accounting.components.goods;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.LazyPaginator;
import com.trade_accounting.components.util.NaiveXlsTableBuilder;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.ProductGroupDto;
import com.trade_accounting.services.interfaces.ProductGroupService;
import com.trade_accounting.services.interfaces.ProductService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.klaudeta.PaginatedGrid;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringComponent
@Route(value = "customersProducts", layout = AppView.class)
@UIScope
public class GoodsView extends VerticalLayout {

    private static final String VALUE_SELECT_WIDTH = "120px";

    private final ProductService productService;
    private final ProductGroupService productGroupService;
    private final GoodsModalWindow goodsModalWindow;
    private final ServiceModalWindow serviceModalWindow;
    private final TreeGrid<ProductGroupDto> treeGrid;
    private final GridFilter<ProductDto> filter;
    private final LazyPaginator<ProductDto> lazyPaginator;
    private final Grid<ProductDto> grid;

    @Autowired
    public GoodsView(ProductService productService,
                     ProductGroupService productGroupService,
                     GoodsModalWindow goodsModalWindow,
                     ServiceModalWindow serviceModalWindow) {
        this.productService = productService;
        this.productGroupService = productGroupService;
        this.goodsModalWindow = goodsModalWindow;
        this.serviceModalWindow = serviceModalWindow;


        treeGrid = getTreeGrid();
        this.grid = getGrid();

        this.filter = new GridFilter<>(grid);
        this.lazyPaginator = new LazyPaginator<>(grid, productService, 50, filter);
        setHorizontalComponentAlignment(Alignment.CENTER, lazyPaginator);
        add(getUpperLayout(), filter, getMiddleLayout(grid), lazyPaginator);
    }

    public void updateData() {
        lazyPaginator.updateData(false);
        updateTreeGrid(productGroupService.getAll());
    }

    private Component getUpperLayout() {
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.add(buttonQuestion(), title(), buttonRefresh());
        mainLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.add(buttonPlusGoods(),
                buttonPlusService(), buttonPlusSet(), buttonPlusGroup(),
                buttonFilter());

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(text(), BigDecimalField(), valueSelect(), valueSelectPrint(),
                valueSelectImport(), valueSelectExport(), buttonSettings());
        verticalLayout.add(upperLayout, horizontalLayout);
        mainLayout.add(verticalLayout);
        return mainLayout;
    }

    private SplitLayout getMiddleLayout(Grid<ProductDto> grid) {
        SplitLayout middleLayout = new SplitLayout();
        middleLayout.setWidth("100%");
        middleLayout.setHeight("66vh");
        middleLayout.addToPrimary(treeGrid);
        middleLayout.addToSecondary(grid);
        return middleLayout;
    }

    private GridPaginator<ProductDto> getPaginator(Grid<ProductDto> grid) {
        GridPaginator<ProductDto> gridPaginator = new GridPaginator<>(grid, new ArrayList<>(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, gridPaginator);
        return gridPaginator;
    }

    private Grid<ProductDto> getGrid() {
        Grid<ProductDto> grid = new PaginatedGrid<>(ProductDto.class);
        grid.setWidth("75%");
        grid.setHeight("100%");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.setColumns("id", "name", "description", "weight", "volume", "purchasePrice");
        grid.getColumnByKey("id").setHeader("id").setId("ID");
        grid.getColumnByKey("name").setHeader("Наименование").setId("Наименование");
        grid.getColumnByKey("weight").setHeader("Вес").setId("Вес");
        grid.getColumnByKey("volume").setHeader("Объем").setId("Объем");
        grid.getColumnByKey("description").setHeader("Артикул").setId("Артикул");
        grid.getColumnByKey("purchasePrice").setHeader("Закупочная цена").setId("Закупочная цена");

        grid.addItemDoubleClickListener(event -> {
            ProductDto productDto = event.getItem();
            goodsModalWindow.open(productDto);
        });
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        return grid;
    }

    private TreeGrid<ProductGroupDto> getTreeGrid() {
        TreeGrid<ProductGroupDto> treeGridLocal = new TreeGrid<>();
        treeGridLocal.setHeight("100%");
        treeGridLocal.setWidth("25%");
        treeGridLocal.setThemeName("dense", true);
        treeGridLocal.addClassName("treeGreed");

        Grid.Column<ProductGroupDto> column = treeGridLocal
                .addHierarchyColumn(x -> "")
                .setHeader("Товарная группа")
                .setFlexGrow(0)
                .setWidth("auto")
                .setSortProperty("sortNumber")
                .setComparator(Comparator.comparing(ProductGroupDto::getSortNumber));

        treeGridLocal.addColumn(ProductGroupDto::getName);
        HeaderRow.HeaderCell cell = treeGridLocal.appendHeaderRow().getCell(column);
        Label label = new Label();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setPadding(false);
        horizontalLayout.setMargin(false);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Button closeButton = new Button(new Icon(VaadinIcon.CLOSE));
        closeButton.setVisible(false);
        closeButton.setMaxHeight("20px");
        horizontalLayout.add(label, closeButton);
        closeButton.addClickListener(event -> {
            closeButton.setVisible(false);
            label.setText("");
            lazyPaginator.updateData(false);
            treeGridLocal.deselectAll();
        });
        cell.setComponent(horizontalLayout);
        treeGridLocal.setSelectionMode(Grid.SelectionMode.SINGLE);
        treeGridLocal.addSelectionListener(event -> {
            Optional<ProductGroupDto> optional = event.getFirstSelectedItem();
            if (optional.isPresent()) {
                lazyPaginator.updateData(false);
                label.setText(optional.get().getName());
                closeButton.setVisible(true);
            }
        });
        return treeGridLocal;
    }

    private void updateTreeGrid(List<ProductGroupDto> productGroupData) {
        treeGrid.getTreeData().clear();
        List<ProductGroupDto> buffer = new ArrayList<>();
        ProductGroupDto element;
        ProductGroupDto parent;
        while (!productGroupData.isEmpty()) {
            for (int i = 0; i < productGroupData.size(); i++) {
                element = productGroupData.get(i);
                if (element.getParentId() == null) {
                    treeGrid.getTreeData().addItem(null, element);
                    buffer.add(element);
                    productGroupData.remove(element);
                } else if (!buffer.isEmpty()) {
                    for (int j = 0; j < buffer.size(); j++) {
                        parent = buffer.get(j);
                        if (parent.getId().equals(element.getParentId())) {
                            treeGrid.getTreeData().addItem(parent, element);
                            buffer.add(element);
                            productGroupData.remove(element);
                            break;
                        }
                    }
                }
            }
        }
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Dialog dialog = new Dialog();
        Button cancelButton = new Button("Закрыть", event -> {
            dialog.close();
        });
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponentAsFirst(cancelButton);
        dialog.add(new Text("В разделе представлены все ваши товары, услуги и комплекты.\n" +
                "Для удобства товары и услуги можно группировать. Различать товары с одинаковым " +
                "артикулом по характеристикам (например, размеру или цвету) удобно с помощью модификаций. " +
                "Несколько единиц одного товара можно продавать упаковками. А комплекты позволяют продавать " +
                "наборы разных товаров и услуг как единое целое.\n" +
                "Каталог товаров можно импортировать и экспортировать."));
        dialog.setWidth("500px");
        dialog.setHeight("300px");
        buttonQuestion.addClickListener(event -> dialog.open());
        Shortcuts.addShortcutListener(dialog, () -> {
            dialog.close();
        }, Key.ESCAPE);
        dialog.add(new Div(cancelButton));
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonPlusGoods() {
        Button addGoodsButton = new Button("Товар", new Icon(VaadinIcon.PLUS_CIRCLE));
        addGoodsButton.addClickListener(e -> goodsModalWindow.open());
        addGoodsButton.getStyle().set("cursor", "pointer");
        return addGoodsButton;
    }

    private Button buttonPlusService() {
        Button addServiceButton = new Button("Услуга", new Icon(VaadinIcon.PLUS_CIRCLE));
        addServiceButton.addClickListener(e -> serviceModalWindow.open());
        addServiceButton.getStyle().set("cursor", "pointer");
        return addServiceButton;
    }

    private Button buttonPlusSet() {
        return new Button("Комплект", new Icon(VaadinIcon.PLUS_CIRCLE));
    }

    private Button buttonPlusGroup() {
        return new Button("Группа", new Icon(VaadinIcon.PLUS_CIRCLE));
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
        text.setPlaceholder("Наименование, код или артикул");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setClearButtonVisible(true);
        text.setValueChangeMode(ValueChangeMode.EAGER);
        text.addValueChangeListener(e -> updateList(text));
        text.setWidth("300px");
        return text;
    }

    private void updateList(TextField text) {
        lazyPaginator.updateData(false);
    }

    private H2 title() {
        H2 textCompany = new H2("Товары и услуги");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    private BigDecimalField BigDecimalField() {
        BigDecimalField numberField = new BigDecimalField();
        numberField.setPlaceholder("0");
        numberField.setWidth("35px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setWidth(VALUE_SELECT_WIDTH);
        valueSelect.setItems("Изменить");
        valueSelect.setValue("Изменить");
        return valueSelect;
    }

    private Select<String> valueSelectPrint() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setWidth(VALUE_SELECT_WIDTH);
        valueSelect.setPlaceholder("Печать");
        Anchor anchor = new Anchor(new StreamResource("goods.xls",
                this::buildXlsTable), "Список товаров");
        valueSelect.add(anchor);
        return valueSelect;
    }

    private Select<String> valueSelectImport() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setWidth(VALUE_SELECT_WIDTH);
        valueSelect.setItems("Импорт");
        valueSelect.setValue("Импорт");
        return valueSelect;
    }

    private Select<String> valueSelectExport() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setWidth(VALUE_SELECT_WIDTH);
        valueSelect.setItems("Экспорт");
        valueSelect.setValue("Экспорт");
        return valueSelect;
    }

    private InputStream buildXlsTable() {
        return new NaiveXlsTableBuilder<ProductDto>().header("Товары")
                .metadata("Создал: ")
                .columns("№", "Наименование", "Артикул", "Вес", "Объем", "Закупочная цена")
                .mappings(
                        (product, cell) -> cell.setCellValue(product.getId()),
                        (product, cell) -> cell.setCellValue(product.getName()),
                        (product, cell) -> cell.setCellValue(product.getDescription()),
                        (product, cell) -> cell.setCellValue(product.getWeight().doubleValue()),
                        (product, cell) -> cell.setCellValue(product.getVolume().doubleValue()),
                        (product, cell) -> cell.setCellValue(product.getPurchasePrice().doubleValue()))
                .data(productService.getAll())
                .getAsStream();

    }
}

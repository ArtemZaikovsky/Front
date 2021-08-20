package com.trade_accounting.components.util;

import com.trade_accounting.models.dto.PageDto;
import com.trade_accounting.services.interfaces.PageableService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.event.SortEvent;
import com.vaadin.flow.data.value.ValueChangeMode;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Server-side component for pagination grid.
 */
public class LazyPaginator<T> extends HorizontalLayout {
    private final int itemsPerPage;
    private int numberOfPages;
    private int currentPage = 1;

    private final Grid<T> grid;
    private PageDto<T> pageData;
    private final PageableService<T> pageableService;
    private IntegerField pageItemsTextField;
    private final Button firstPageButton = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT));
    private final Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Button lastPageButton = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));
    private Long rowCount;
    private final GridFilter<T> gridFilter;
    private Map<String, String> sortParams = Map.of("column", "id", "direction", SortOrder.ASCENDING.toString());
    private Map<String, String> filterData = new HashMap<>();

    /**
     * Creates a Paginator with specific number of items per page.
     *
     * @param grid         grid for pagination
     * @param pageableService         data for grid
     * @param itemsPerPage number items per page
     */
    public LazyPaginator(Grid<T> grid, PageableService<T> pageableService,
                         int itemsPerPage, GridFilter<T> gridFilter) {
        this.gridFilter = gridFilter;
        this.grid = grid;
        this.pageableService = pageableService;
        this.itemsPerPage = itemsPerPage;
        this.filterData.put("search", "");
        this.updatePageData();
        grid.setPageSize(itemsPerPage);
        grid.addSortListener(this::sort);
        configureButton();
        configureTextField();
        setCurrentPageAndReloadGrid(currentPage);
        configureFilter();
        add(firstPageButton, prevPageButton, pageItemsTextField, nextPageButton, lastPageButton);
    }

    public void setSearchText(String text) {
        this.filterData.put("search", text);
        this.updateData(false);
    }

    private void configureFilter() {
        gridFilter.onSearchClick(e -> {
            this.clearFilterData();
            this.filterData.putAll(gridFilter.getFilterData());
            this.updateData(false);
        });
        gridFilter.onClearClick(e -> {
            this.clearFilterData();
            this.updateData(false);
        });
    }

    /**
     * Clears additional filter data
     */
    private void clearFilterData() {
        this.filterData.keySet().removeIf(key -> !key.equals("search"));
    }

    private void configureButton() {
        nextPageButton.addClickListener(e -> setCurrentPageAndReloadGrid(currentPage + 1));
        lastPageButton.addClickListener(e -> setCurrentPageAndReloadGrid(numberOfPages));
        prevPageButton.addClickListener(e -> setCurrentPageAndReloadGrid(currentPage - 1));
        firstPageButton.addClickListener(e -> setCurrentPageAndReloadGrid(1));
    }

    private void configureTextField() {
        pageItemsTextField = new IntegerField("");
        pageItemsTextField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        pageItemsTextField.setValueChangeMode(ValueChangeMode.TIMEOUT);
        pageItemsTextField.setWidth("200px");
        pageItemsTextField.setPlaceholder(getCurrentGridPageItems());
    }

    private void updatePageData() {
        this.pageData = pageableService.getPage(this.filterData, this.sortParams, currentPage, itemsPerPage);
        this.numberOfPages = pageData.getTotalPages();
        this.rowCount = pageData.getTotalElements();
    }

    private String getCurrentGridPageItems() {
        int startElement = (currentPage - 1) * itemsPerPage + 1;
        int endElement = startElement + this.pageData.getNumberOfElements() - 1;
        return (startElement != 0 ? startElement : 1) + "-" + endElement + " из " + rowCount;
    }

    /**
     * Reload grid and sets data to grid from the value of current page.
     */
    private void reloadGrid() {
        grid.setItems(this.pageData.getContent());
        pageItemsTextField.setPlaceholder(getCurrentGridPageItems());
        grid.getDataProvider().refreshAll();
    }

    /**
     * Sets the current page of paginator. The page cannot be greater than number of pages of the paginator.
     * Also reloaded a grid to the setted page.
     *
     * @param currentPage new current page
     */
    private void setCurrentPageAndReloadGrid(int currentPage) {
        setCurrentPage(currentPage);
        this.updatePageData();
        reloadGrid();
    }

    private void setCurrentPage(int currentPage) {
        if (currentPage > numberOfPages) {
            throw new IllegalArgumentException("The current page: ["+ currentPage +"] greater than maximum number of page.");
        }
        this.currentPage = currentPage;
        firstPageButton.setEnabled(true);
        nextPageButton.setEnabled(true);
        lastPageButton.setEnabled(true);
        prevPageButton.setEnabled(true);

        if (currentPage == 1) {
            firstPageButton.setEnabled(false);
            prevPageButton.setEnabled(false);
        }
        if (currentPage == numberOfPages) {
            lastPageButton.setEnabled(false);
            nextPageButton.setEnabled(false);
        }
    }

    /**
     * Updates all necessary data for grid update.
     *
     * @param saveCurrentPage save current page flag
     */
    public void updateData(boolean saveCurrentPage){
        if (saveCurrentPage && this.numberOfPages >= currentPage) {
            this.updatePageData();
            setCurrentPage(currentPage);
        } else {
            currentPage = 1;
            this.updatePageData();
            setCurrentPage(currentPage);
        }
        reloadGrid();
    }

    private void setSortParams(String sortColumn, String sortDirection) {
        this.sortParams.put("column", sortColumn);
        this.sortParams.put("direction", sortDirection);
    }

    //FIXME двойной вызов при переходе сортировки между столбцами (сначала сбрасывается сортировка на одном, а потом ставится на другом)
    private void sort(SortEvent<Grid<T>, GridSortOrder<T>> gridGridSortOrderSortEvent) {
        if (!gridGridSortOrderSortEvent.getSortOrder().isEmpty()) {
            gridGridSortOrderSortEvent.getSortOrder().forEach(tGridSortOrder ->
                    this.setSortParams(tGridSortOrder.getSorted().getKey(), tGridSortOrder.getDirection().toString()));
        } else {
            this.setSortParams("id", SortOrder.ASCENDING.toString());
        }
        updateData(true);
    }
}

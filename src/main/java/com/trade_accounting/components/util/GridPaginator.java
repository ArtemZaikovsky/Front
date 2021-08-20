package com.trade_accounting.components.util;

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
import com.vaadin.flow.function.SerializableComparator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Server-side component for pagination grid.
 */
public class GridPaginator<T> extends HorizontalLayout {
    private int itemsPerPage;
    private int numberOfPages;
    private int currentPage;

    private final Grid<T> grid;
    private List<T> data;

    private IntegerField pageItemsTextField;
    private final Button firstPageButton = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT));
    private final Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Button lastPageButton = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));

    /**
     * Creates a Paginator with specific number of items per page.
     *
     * @param grid         grid for pagination
     * @param data         data for grid
     * @param itemsPerPage number items per page
     */
    public GridPaginator(Grid<T> grid, List<T> data, int itemsPerPage) {
        this.grid = grid;
        this.data = data;
        this.itemsPerPage = itemsPerPage;


        grid.setPageSize(itemsPerPage);
        grid.addSortListener(this::sort);
        calculateNumberOfPages();
        configureButton();
        configureTextField();
        setCurrentPageAndReloadGrid(1);
        reloadGrid();

        add(firstPageButton, prevPageButton, pageItemsTextField, nextPageButton, lastPageButton);
    }

    /**
     * Creates a Paginator with 10 items per page.
     *
     * @param grid grid for pagination
     * @param data data for grid
     */
    public GridPaginator(Grid<T> grid, List<T> data) {
        this(grid, data, 10);
    }

    private void configureButton() {
        nextPageButton.addClickListener(e -> setCurrentPageAndReloadGrid(currentPage + 1));
        lastPageButton.addClickListener(e -> setCurrentPageAndReloadGrid(getNumberOfPages()));
        prevPageButton.addClickListener(e -> setCurrentPageAndReloadGrid(getCurrentPage() - 1));
        firstPageButton.addClickListener(e -> setCurrentPageAndReloadGrid(1));
    }

    private void configureTextField() {
        pageItemsTextField = new IntegerField("");
        pageItemsTextField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        pageItemsTextField.setValueChangeMode(ValueChangeMode.TIMEOUT);
        pageItemsTextField.setWidth("200px");

        pageItemsTextField.setPlaceholder(getCurrentGridPageItems());
    }

    private List<T> getPageData() {
        int from = (currentPage - 1) * itemsPerPage;

        int to = (from + itemsPerPage);
        to = Math.min(to, data.size());

        return data.subList(from, to);
    }

    private String getCurrentGridPageItems() {
        int start = currentPage;
        int end = itemsPerPage * currentPage;

        if (start != 1) {
            start = ((itemsPerPage * currentPage) - itemsPerPage) + 1;
        }

        end = Math.min(end, data.size());

        return start + "-" + end + " из " + data.size();
    }

    private void calculateNumberOfPages() {
        if (data.size() == 0) {
            this.numberOfPages = 1;
        }else {
            this.numberOfPages = (int) Math.ceil((float) data.size() / itemsPerPage);
        }
    }

    /**
     * Reload grid and sets data to grid from the value of current page.
     */
    public void reloadGrid() {
        grid.setItems(getPageData());
        pageItemsTextField.setPlaceholder(getCurrentGridPageItems());
        grid.getDataProvider().refreshAll();
    }

    /**
     * Gets the current page of paginator.
     *
     * @return currentPage current page
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the current page of paginator. The page cannot be greater than number of pages of the paginator.
     * Also reloaded a grid to the setted page.
     *
     * @param currentPage new current page
     */
    public void setCurrentPageAndReloadGrid(int currentPage) {
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
        if (currentPage == getNumberOfPages()) {
            lastPageButton.setEnabled(false);
            nextPageButton.setEnabled(false);
        }
        reloadGrid();
    }

    /**
     * Gets the number of pages of the paginator.
     *
     * @return numberOfPages number of pages
     */
    public int getNumberOfPages() {
        return numberOfPages;
    }


    /**
     * Sets the number of items per page of the paginator. Is has to be greater than 0.
     *
     * @param itemsPerPage items per page
     */
    public void setItemsPerPage(int itemsPerPage) {
        if (itemsPerPage < 1) {
            throw new IllegalArgumentException("The number of items per page has to be greater than 0");
        }
        this.itemsPerPage = itemsPerPage;
        calculateNumberOfPages();

        setCurrentPageAndReloadGrid(1);
    }

    /**
     * Sets new data for paginator.
     * Also set current page to 1 and reload grid.
     *
     * @param data data for grid
     */
    public void setData(List<T> data) {
        setData(data, false);
    }


    /**
     * Sets new data for paginator.
     *
     * @param data data for grid
     * @param saveCurrentPage save current page flag
     */
    public void setData(List<T> data, boolean saveCurrentPage){
        SerializableComparator<T> comparator = grid.getDataCommunicator().getInMemorySorting();
        if (comparator != null){
            data = data.stream().sorted(comparator).collect(Collectors.toList());
        }

        this.data = data;
        calculateNumberOfPages();

        if (saveCurrentPage && numberOfPages >= currentPage){
            setCurrentPageAndReloadGrid(getCurrentPage());
        }else {
            setCurrentPageAndReloadGrid(1);
        }
    }

    private void sort(SortEvent<Grid<T>, GridSortOrder<T>> gridGridSortOrderSortEvent) {
        gridGridSortOrderSortEvent.getSortOrder().forEach(tGridSortOrder -> {
            SerializableComparator<T> comparator = tGridSortOrder.getSorted().getComparator(tGridSortOrder.getDirection());
            data = data.stream().sorted(comparator).collect(Collectors.toList());
        });
        setData(data, true);
    }

}

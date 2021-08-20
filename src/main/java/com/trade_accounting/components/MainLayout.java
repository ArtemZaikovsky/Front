package com.trade_accounting.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "main", layout = AppView.class)
@RouteAlias(value = "", layout = AppView.class)
@PageTitle("Главная | CRM")
public class MainLayout extends VerticalLayout {

    public MainLayout() {

    }
}

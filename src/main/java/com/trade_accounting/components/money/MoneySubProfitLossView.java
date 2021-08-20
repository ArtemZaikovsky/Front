package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "MoneySubProfitLossView", layout = AppView.class)
@PageTitle("Прибыли и убытки")
public class MoneySubProfitLossView extends VerticalLayout {

    private H2 title() {
        H2 title = new H2("Прибыли и убытки");
        title.setHeight("2.2em");
        return title;
    }

    public MoneySubProfitLossView() {
        add(title());
    }
}

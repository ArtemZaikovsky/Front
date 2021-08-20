package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.PayoutService;
import com.trade_accounting.services.interfaces.RetailStoreService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "retail", layout = AppView.class)
@PageTitle("Розница")
@SpringComponent
@UIScope
public class RetailView extends Div implements AfterNavigationObserver {

    private final Div div;
    private final RetailStoreService retailStoreService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final PayoutService payoutService;
    private final Notifications notifications;

    @Autowired
    public RetailView(RetailStoreService retailStoreService,
                      CompanyService companyService, EmployeeService employeeService,
                      PayoutService payoutService, Notifications notifications) {
        this.retailStoreService = retailStoreService;
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.payoutService = payoutService;
        this.notifications = notifications;
        div = new Div();
        add(configurationSubMenu(), div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(6);
            }
        });
    }

    private Tabs configurationSubMenu() {

        Tabs tabs = new Tabs(
                new Tab("Точки продаж"),
                new Tab("Смены"),
                new Tab("Продажи"),
                new Tab("Возвраты"),
                new Tab("Внесения"),
                new Tab("Выплаты"),
                new Tab("Операции с баллами"),
                new Tab("Предоплаты"),
                new Tab("Возвраты предоплат"),
                new Tab("Очередь облачных чеков")
        );

        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            switch (tabName) {
                case "Точки продаж":
                    div.removeAll();
                    div.add(new RetailStoresTabView(retailStoreService, companyService, employeeService));
                    break;
                case "Смены":
                    div.removeAll();
                    break;
                case "Продажи":
                    div.removeAll();
                    break;
                case "Возвраты":
                    div.removeAll();
                    break;
                case "Внесения":
                    div.removeAll();
                    break;
                case "Выплаты":
                    div.removeAll();
                    div.add(new PayoutTabView(payoutService, retailStoreService, companyService, employeeService, notifications));
                    break;
                case "Операции с баллами":
                    div.removeAll();
                    break;
                case "Предоплаты":
                    div.removeAll();
                    break;
                case "Возвраты предоплат":
                    div.removeAll();
                    break;
                case "Очередь облачных чеков":
                    div.removeAll();
                    break;
            }
        });

        return tabs;
    }
}

package com.trade_accounting.components.production;

import com.trade_accounting.components.AppView;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.TechnicalCardGroupService;
import com.trade_accounting.services.interfaces.TechnicalCardService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "production", layout = AppView.class)
@PageTitle("Производство")
public class ProductionSubMenuView extends Div implements AfterNavigationObserver {

    private final Div div;
    private final TechnicalCardService technicalCardService;
    private final TechnicalCardGroupService technicalCardGroupService;
    private final ProductService productService;

    @Autowired
    public ProductionSubMenuView(TechnicalCardService technicalCardService,
                                 TechnicalCardGroupService technicalCardGroupService,
                                 ProductService productService) {
        div = new Div();
        add(configurationSubMenu(), div);
        this.technicalCardService = technicalCardService;
        this.technicalCardGroupService = technicalCardGroupService;
        this.productService = productService;
    }

    private Tabs configurationSubMenu() {
        Tabs tabs = new Tabs(
                new Tab("Тех. карты"),
                new Tab("Заказы на производство"),
                new Tab("Тех. операции")
        );
        tabs.addSelectedChangeListener(event -> {
            String name = event.getSelectedTab().getLabel();

            switch (name) {
                case "Тех. карты":
                    div.removeAll();
                    div.add(new FlowchartsViewTab(technicalCardService, technicalCardGroupService, productService));
                    break;
                case "Заказы на производство":
                    div.removeAll();
                    div.add(new OrdersMenuView());
                    break;
                case "Тех. операции":
                    div.removeAll();
                    break;
            }
        });
        return tabs;

    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        div.removeAll();
        div.add(new FlowchartsViewTab(technicalCardService, technicalCardGroupService, productService));

    }
}


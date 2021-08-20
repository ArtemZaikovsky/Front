package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.trade_accounting.services.interfaces.WarehouseService;
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
import org.springframework.context.annotation.Lazy;

@Route(value = "sells", layout = AppView.class)
@PageTitle("Продажи")
@SpringComponent
@UIScope
public class SalesSubMenuView extends Div implements AfterNavigationObserver {//некорректно задаётся id при добавлении

    private final Div div;
    private final InvoiceService invoiceService;
    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final InvoiceProductService invoiceProductService;

    private final SalesSubCustomersOrdersView salesSubCustomersOrdersView;
    private final SalesSubShipmentView salesSubShipmentView;
    private final SalesSubInvoicesToBuyersView salesSubInvoicesToBuyersView;
    private final CommissionAgentReportModalView commissionAgentReportModalView;


    @Autowired
    public SalesSubMenuView(InvoiceService invoiceService,
                            ContractorService contractorService,
                            CompanyService companyService,
                            WarehouseService warehouseService,
                            InvoiceProductService invoiceProductService, @Lazy SalesSubCustomersOrdersView salesSubCustomersOrdersView,
                            @Lazy SalesSubShipmentView salesSubShipmentView,
                            @Lazy SalesSubInvoicesToBuyersView salesSubInvoicesToBuyersView,
                            CommissionAgentReportModalView commissionAgentReportModalView) {
        this.invoiceProductService = invoiceProductService;
        this.salesSubCustomersOrdersView = salesSubCustomersOrdersView;
        this.invoiceService = invoiceService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.salesSubShipmentView = salesSubShipmentView;
        this.salesSubInvoicesToBuyersView = salesSubInvoicesToBuyersView;
        this.commissionAgentReportModalView = commissionAgentReportModalView;

        div = new Div();
        add(configurationSubMenu(), div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
//        div.removeAll();
//        div.add(new SalesSubCustomersOrders(invoiceService, contractorService, companyService));

        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(2);
            }
        });
        getUI().ifPresent(ui -> {
            div.removeAll();
            div.add(salesSubCustomersOrdersView);
        });
    }


    private Tabs configurationSubMenu() {
        Tabs tabs = new Tabs(
                new Tab("Заказы покупателей"),
                new Tab("Счета покупателям"),
                new Tab("Отгрузки"),
                new Tab("Отчеты комиссионера"),
                new Tab("Возвраты покупателей"),
                new Tab("Счета-фактуры выданные"),
                new Tab("Прибыльность"),
                new Tab("Товары на реализации"),
                new Tab("Воронка продаж")
        );


        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            switch (tabName) {
                case "Заказы покупателей":
                    div.removeAll();
                    div.add(salesSubCustomersOrdersView);
                    break;
                case "Счета покупателям":
                    div.removeAll();
                    div.add(salesSubInvoicesToBuyersView);
                    break;
                case "Отгрузки":
                    div.removeAll();
                    div.add(salesSubShipmentView);
                    break;
                case "Отчеты комиссионера":
                    div.removeAll();
                    div.add(new SalesSubAgentReportsView(invoiceService, contractorService, companyService, warehouseService, commissionAgentReportModalView));
                    break;
                case "Возвраты покупателей":
                    div.removeAll();
                    div.add("SalesSubBuyersReturnsView");
                    break;
                case "Счета-фактуры выданные":
                    div.removeAll();
                    div.add("SalesSubIssuedInvoicesView");
                    break;
                case "Прибыльность":
                    div.removeAll();
                    div.add("SalesSubProfitabilityView");
                    break;
                case "Товары на реализации":
                    div.removeAll();
                    div.add("SalesSubGoodsForSaleView");
                    break;
                case "Воронка продаж":
                    div.removeAll();
                    div.add("SalesSubSalesFunnelView");
                    break;
            }
        });
        return tabs;
    }
}


package com.trade_accounting.components.purchases;

import com.trade_accounting.components.AppView;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
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

@Route(value = "purchases", layout = AppView.class)
@PageTitle("Закупки")
@SpringComponent
@UIScope
public class PurchasesSubMenuView extends Div implements AfterNavigationObserver { //некорректно задаётся id при добавлении

    private final Div div;
    private final InvoiceService invoiceService;
    private final PurchasesSubSuppliersOrders purchasesSubSuppliersOrders;
    private final PurchasesSubVendorAccounts purchasesSubVendorAccounts;
    private final PurchasesSubReturnToSuppliers purchasesSubReturnToSuppliers;
    private final PurchasesSubMenuInvoicesReceived purchasesSubMenuInvoicesReceived;
    private final PurchasesSubAcceptances purchasesSubAcceptances;

    @Autowired
    public PurchasesSubMenuView(InvoiceService invoiceService,
                                @Lazy PurchasesSubSuppliersOrders purchasesSubSuppliersOrders,
                                @Lazy PurchasesSubVendorAccounts purchasesSubVendorAccounts, PurchasesSubReturnToSuppliers purchasesSubReturnToSuppliers,
                                PurchasesSubMenuInvoicesReceived purchasesSubMenuInvoicesReceived,
                                PurchasesSubAcceptances purchasesSubAcceptances) {
        this.invoiceService = invoiceService;
        this.purchasesSubSuppliersOrders = purchasesSubSuppliersOrders;
        this.purchasesSubVendorAccounts = purchasesSubVendorAccounts;
        this.purchasesSubReturnToSuppliers = purchasesSubReturnToSuppliers;
        this.purchasesSubMenuInvoicesReceived = purchasesSubMenuInvoicesReceived;
        this.purchasesSubAcceptances = purchasesSubAcceptances;
        div = new Div();
        add(configurationSubMenu(), div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(1);
            }
        });
        getUI().ifPresent(ui -> {
            div.removeAll();
            div.add(purchasesSubSuppliersOrders);
        });
    }

    private Tabs configurationSubMenu() {
        Tab supplierOrdersLayout = new Tab(new Label("Заказы поставщикам"));
        Tab vendorAccountsLayout = new Tab(new Label("Счета поставщиков"));
        Tab admissionsLayout = new Tab(new Label("Приемки"));
        Tab refundsToSuppliersLayout = new Tab(new Label("Возвраты поставщикам"));
        Tab invoicesReceivedLayout = new Tab(new Label("Счета-фактуры полученные"));
        Tab purchasingManagementLayout = new Tab(new Label("Управление закупками"));

        Tabs tabs = new Tabs(
                supplierOrdersLayout,
                vendorAccountsLayout,
                admissionsLayout,
                refundsToSuppliersLayout,
                invoicesReceivedLayout,
                purchasingManagementLayout
        );

        tabs.addSelectedChangeListener(event -> {
            Tab tab = event.getSelectedTab();
            if (supplierOrdersLayout.equals(tab)) {
                div.removeAll();
                div.add(purchasesSubSuppliersOrders);
            } else if (vendorAccountsLayout.equals(tab)) {
                div.removeAll();
                div.add(purchasesSubVendorAccounts);
            } else if (refundsToSuppliersLayout.equals(tab)) {
                div.removeAll();
                div.add(purchasesSubReturnToSuppliers);
            } else if (invoicesReceivedLayout.equals(tab)){
                div.removeAll();
                div.add(purchasesSubMenuInvoicesReceived);
            } else if (admissionsLayout.equals(tab)){
                div.removeAll();
                div.add(purchasesSubAcceptances);
            }
        });
        return tabs;
    }
}

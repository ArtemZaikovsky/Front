package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.services.interfaces.BalanceAdjustmentService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.MoneySubCashFlowService;
import com.trade_accounting.services.interfaces.PaymentService;
import com.trade_accounting.services.interfaces.ProjectService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "money", layout = AppView.class)
@PageTitle("Деньги")
public class MoneySubMenuView extends Div implements AfterNavigationObserver {
    private final PaymentService paymentService;
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private final ProjectService projectService;
    private final ContractService contractService;
    private final Notifications notifications;
    private final Div div;
    private final PaymentModalWin paymentModalWin;
    private final MoneySubCashFlowService moneySubCashFlowService;
    private final BalanceAdjustmentService balanceAdjustmentService;
    private final BalanceAdjustmentModalView modalView;

    public MoneySubMenuView(PaymentService paymentService,
                            CompanyService companyService,
                            ContractorService contractorService,
                            ProjectService projectService,
                            ContractService contractService,
                            Notifications notifications,
                            PaymentModalWin paymentModalWin, MoneySubCashFlowService moneySubCashFlowService,
                            BalanceAdjustmentService balanceAdjustmentService,
                            BalanceAdjustmentModalView modalView) {
        this.paymentService = paymentService;
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.projectService = projectService;
        this.contractService = contractService;
        this.notifications = notifications;
        this.paymentModalWin = paymentModalWin;
        this.moneySubCashFlowService = moneySubCashFlowService;
        this.balanceAdjustmentService = balanceAdjustmentService;
        this.modalView = modalView;
        div = new Div();
        add(configurationSubMenu(), div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        div.removeAll();
        div.add(new MoneySubPaymentsView(paymentService, companyService,
                contractorService, projectService, contractService, notifications, paymentModalWin));

        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(5);
            }
        });
    }

    private Tabs configurationSubMenu() {
        Tabs tabs = new Tabs(
                new Tab("Платежи"),
                new Tab("Движение денежных средств"),
                new Tab("Прибыли и убытки"),
                new Tab("Взаиморасчеты"),
                new Tab("Корректировки")
        );

        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            switch (tabName) {
                case "Платежи":
                    div.removeAll();
                    div.add(new MoneySubPaymentsView(paymentService, companyService, contractorService, projectService, contractService, notifications, paymentModalWin));
                    break;
                case "Движение денежных средств":
                    div.removeAll();
                    div.add(new MoneySubCashFlowView(moneySubCashFlowService, companyService, contractorService, projectService, contractService, notifications, paymentModalWin));
                    break;
                case "Прибыли и убытки":
                    div.removeAll();
                    div.add(new MoneySubProfitLossView());
                    break;
                case "Взаиморасчеты":
                    div.removeAll();
                    div.add(new MoneySubMutualSettlementsView());
                    break;
                case "Корректировки":
                    div.removeAll();
                    div.add(new MoneySubBalanceAdjustmentView(balanceAdjustmentService,companyService,contractorService, notifications, modalView));
                    break;
            }
        });
        return tabs;

    }


}

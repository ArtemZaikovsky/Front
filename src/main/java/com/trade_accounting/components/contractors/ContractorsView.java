package com.trade_accounting.components.contractors;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.services.interfaces.AddressService;
import com.trade_accounting.services.interfaces.BankAccountService;
import com.trade_accounting.services.interfaces.ContractorGroupService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.DepartmentService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.LegalDetailService;
import com.trade_accounting.services.interfaces.ContractorStatusService;
import com.trade_accounting.services.interfaces.TypeOfContractorService;
import com.trade_accounting.services.interfaces.TypeOfPriceService;
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

@SpringComponent
@UIScope
@Route(value = "contractors", layout = AppView.class)
@PageTitle("Контрагенты")
public class ContractorsView extends Div implements AfterNavigationObserver {

    private final Div div;

    private final Notifications notifications;
    private final ContractorService contractorService;
    private final ContractorGroupService contractorGroupService;
    private final TypeOfContractorService typeOfContractorService;
    private final TypeOfPriceService typeOfPriceService;
    private final LegalDetailService legalDetailService;
    private final ContractorStatusService contractorStatusService;
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;
    private final BankAccountService bankAccountService;
    private final AddressService addressService;
    private final ContractsView contractsView;

    @Autowired
    public ContractorsView(Notifications notifications,
                           ContractorService contractorService,
                           ContractorGroupService contractorGroupService,
                           TypeOfContractorService typeOfContractorService,
                           TypeOfPriceService typeOfPriceService,
                           LegalDetailService legalDetailService,
                           ContractorStatusService contractorStatusService,
                           DepartmentService departmentService,
                           EmployeeService employeeService,
                           BankAccountService bankAccountService,
                           AddressService addressService,
                           ContractsView contractsView) {
        this.notifications = notifications;
        this.contractorService = contractorService;
        this.contractorGroupService = contractorGroupService;
        this.typeOfContractorService = typeOfContractorService;
        this.typeOfPriceService = typeOfPriceService;
        this.legalDetailService = legalDetailService;
        this.contractsView = contractsView;
        this.contractorStatusService = contractorStatusService;
        this.departmentService = departmentService;
        this.employeeService = employeeService;
        this.bankAccountService = bankAccountService;
        this.addressService = addressService;
        div = new Div();
        add(configurationSubMenu(), div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {

        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(4);
            }
        });
        getUI().ifPresent(ui -> {
            div.removeAll();
            div.add(new ContractorsTabView(notifications, contractorService, contractorGroupService,
                    typeOfContractorService,
                    typeOfPriceService,
                    legalDetailService,
                    contractorStatusService,
                    departmentService,
                    employeeService,
                    bankAccountService,
                    addressService));
        });
    }

    private Tabs configurationSubMenu() {

        Tabs tabs = new Tabs(new Tab("Контрагенты"), new Tab("Договоры"));

        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);

        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            switch (tabName) {
                case "Контрагенты":
                    div.removeAll();
                    div.add(new ContractorsTabView(notifications, contractorService, contractorGroupService,
                            typeOfContractorService, typeOfPriceService, legalDetailService, contractorStatusService,
                            departmentService, employeeService, bankAccountService, addressService));
                    break;
                case "Договоры":
                    div.removeAll();
                    div.add(contractsView);
                    break;
            }
        });
        return tabs;
    }
}

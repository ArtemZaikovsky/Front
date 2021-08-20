package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.services.interfaces.AddressService;
import com.trade_accounting.services.interfaces.BankAccountService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.CurrencyService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.ImageService;
import com.trade_accounting.services.interfaces.LegalDetailService;
import com.trade_accounting.services.interfaces.RoleService;
import com.trade_accounting.services.interfaces.TypeOfContractorService;
import com.trade_accounting.services.interfaces.UnitService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "profile", layout = AppView.class)
@PageTitle("Профиль")
public class ProfileView extends Div implements AfterNavigationObserver {

    private final UnitService unitService;
    private final CompanyService companyService;
    private final AddressService addressService;
    private final LegalDetailService legalDetailService;
    private final EmployeeService employeeService;
    private final WarehouseService warehouseService;
    private final CurrencyService currencyService;
    private final RoleService roleService;
    private final ImageService imageService;
    private final Div div;
    private final Div divMenu;
    private final TypeOfContractorService typeOfContractorService;
    private final BankAccountService bankAccountService;

    public ProfileView(UnitService unitService, CompanyService companyService,
                       AddressService addressService, LegalDetailService legalDetailService, EmployeeService employeeService, WarehouseService warehouseService,
                       CurrencyService currencyService, RoleService roleService, ImageService imageService, TypeOfContractorService typeOfContractorService, BankAccountService bankAccountService) {
        this.unitService = unitService;
        this.companyService = companyService;
        this.addressService = addressService;
        this.legalDetailService = legalDetailService;
        this.employeeService = employeeService;
        this.warehouseService = warehouseService;
        this.currencyService = currencyService;
        this.roleService = roleService;
        this.imageService = imageService;
        this.typeOfContractorService = typeOfContractorService;
        this.bankAccountService = bankAccountService;
        div = new Div();
        divMenu = new Div();
        add(divMenu, div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(12);
            }
        });
        getUI().ifPresent(ui -> {
            divMenu.removeAll();
            divMenu.add(configurationSubMenu());
            div.removeAll();
            div.add(new CompanyView(companyService, addressService, legalDetailService, typeOfContractorService, bankAccountService));
        });
    }

    private Tabs configurationSubMenu() {
        Tabs tabs = new Tabs(
                new Tab("Юр. лица"),
                new Tab("Сотрудники"),
                new Tab("Склады"),
                new Tab("Валюты"),
                new Tab("Единицы измерения")
        );
        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            switch (tabName) {
                case "Юр. лица":
                    div.removeAll();
                    div.add(new CompanyView(companyService, addressService, legalDetailService, typeOfContractorService, bankAccountService));
                    break;
                case "Сотрудники":
                    div.removeAll();
                    div.add(new EmployeeView(employeeService, roleService, imageService));
                    break;
                case "Склады":
                    div.removeAll();
                    div.add(new WareHouseView(warehouseService));
                    break;
                case "Валюты":
                    div.removeAll();
                    div.add(new CurrencyView(currencyService));
                    break;
                case "Единицы измерения":
                    div.removeAll();
                    div.add(new UnitView(unitService));
                    break;
                default:
            }
        });
        return tabs;
    }
}

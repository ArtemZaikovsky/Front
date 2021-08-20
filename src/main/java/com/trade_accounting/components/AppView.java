package com.trade_accounting.components;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route
public class AppView extends AppLayout {

    public AppView() {
        addToNavbar(configurationMenu());
    }

    private Tabs configurationMenu() {
        VerticalLayout indicators = new VerticalLayout(VaadinIcon.TRENDING_UP.create(), new Label("Показатели"));
        indicators.addClickListener(e -> indicators.getUI().ifPresent(ui -> ui.navigate("indicators")));

        VerticalLayout purchases = new VerticalLayout(VaadinIcon.CART.create(), new Label("Закупки"));
        purchases.addClickListener(e -> purchases.getUI().ifPresent(ui -> ui.navigate("purchases")));

        VerticalLayout sales = new VerticalLayout(VaadinIcon.BRIEFCASE.create(), new Label("Продажи"));
        sales.addClickListener(e -> sales.getUI().ifPresent(ui -> ui.navigate("sells")));

        VerticalLayout products = new VerticalLayout(VaadinIcon.STOCK.create(), new Label("Товары"));
        products.addClickListener(e -> products.getUI().ifPresent(ui -> ui.navigate("goods")));

        VerticalLayout counterparties = new VerticalLayout(VaadinIcon.USERS.create(), new Label("Контрагенты"));
        counterparties.addClickListener(e -> counterparties.getUI().ifPresent(ui -> ui.navigate("contractors")));

        VerticalLayout money = new VerticalLayout(VaadinIcon.MONEY.create(), new Label("Деньги"));
        money.addClickListener(e -> money.getUI().ifPresent(ui -> ui.navigate("money")));

        VerticalLayout retail = new VerticalLayout(VaadinIcon.SHOP.create(), new Label("Розница"));
        retail.addClickListener(e -> retail.getUI().ifPresent(ui -> ui.navigate("retail")));

        VerticalLayout production = new VerticalLayout(VaadinIcon.FACTORY.create(), new Label("Производство"));
        production.addClickListener(e -> production.getUI().ifPresent(ui -> ui.navigate("production")));

        VerticalLayout tasks = new VerticalLayout(VaadinIcon.CHECK_SQUARE_O.create(), new Label("Задачи"));
        tasks.addClickListener(e -> tasks.getUI().ifPresent(ui -> ui.navigate("tasks")));

        VerticalLayout applications = new VerticalLayout(VaadinIcon.COGS.create(), new Label("Приложения"));
        applications.addClickListener(e -> applications.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout notifications = new VerticalLayout(VaadinIcon.BELL.create(), new Label("Уведомления"));
        notifications.addClickListener(e -> notifications.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout faq = new VerticalLayout(VaadinIcon.QUESTION_CIRCLE_O.create(), new Label("FAQ"));
        faq.addClickListener(e -> faq.getUI().ifPresent(ui -> ui.navigate("")));

        MenuBar menuBar = new MenuBar();
        menuBar.setOpenOnHover(true);
        VerticalLayout profile = new VerticalLayout(VaadinIcon.USER.create(), menuBar);
        MenuItem profileMenuItem = menuBar.addItem("Профиль");
        profile.addClickListener(e -> profile.getUI().ifPresent(ui -> ui.navigate("profile")));
        profileMenuItem.addClickListener(e -> profile.getUI().ifPresent(ui -> ui.navigate("profile")));
        SubMenu projectSubMenu = profileMenuItem.getSubMenu();

        MenuItem settings = projectSubMenu.addItem("Настройки пользователя");
        settings.addClickListener(e -> profile.getUI().ifPresent(ui -> ui.navigate("profile/settings")));

        MenuItem logout = projectSubMenu.addItem("Выход");
        logout.addClickListener(e -> profile.getUI().ifPresent(ui -> ui.navigate("logout")));

        List<VerticalLayout> verticalLayouts = List.of(
                indicators,
                purchases,
                sales,
                products,
                counterparties,
                money,
                retail,
                production,
                tasks,
                applications,
                notifications,
                faq,
                profile
        );

        Tabs tabs = new Tabs();
        for (VerticalLayout verticalLayout : verticalLayouts) {
            verticalLayout.getStyle().set("alignItems", "center");
            verticalLayout.setSpacing(false);
            verticalLayout.setPadding(false);
            tabs.add(new Tab(verticalLayout));
        }
        tabs.setWidthFull();
        tabs.setFlexGrowForEnclosedTabs(1);
        return tabs;
    }
}

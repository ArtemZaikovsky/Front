package com.trade_accounting.components.authentication;

import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "logout", layout = AppView.class)
public class LogoutView extends VerticalLayout {
    public LogoutView() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().getPage().setLocation("/");
    }
}

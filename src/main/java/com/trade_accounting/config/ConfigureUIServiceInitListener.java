package com.trade_accounting.config;

import com.trade_accounting.components.authentication.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import org.springframework.stereotype.Component;

import static com.trade_accounting.config.SecurityConstants.TOKEN_ATTRIBUTE_NAME;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }

    private void beforeEnter(BeforeEnterEvent event) {
        WrappedSession wrappedSession = VaadinSession.getCurrent().getSession();

        if (wrappedSession.getAttribute(TOKEN_ATTRIBUTE_NAME) == null) {
            UI.getCurrent().navigate(LoginView.class);
        }

    }

}


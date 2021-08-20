package com.trade_accounting.components.util;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@SpringComponent
@UIScope
public class Notifications {

    private final String errorStyle = "error-style";

    private final String finishedStyle = "finished-style";

    private final String errorCss = ".error-style { color: white; }";

    private final String finishedCss = ".finished-style { color: black; }";

    private final Button ok = new Button("OK");

    private final VerticalLayout verticalLayout = new VerticalLayout(ok);

    private final Notification notification = new Notification();

    public Notifications() {
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        ok.addClickListener(event -> notification.close());
        notification.setPosition(Notification.Position.BOTTOM_END);
    }

    public void infoNotification(String message) {
        notification.removeThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        abstractNotification(message, finishedCss, finishedStyle);
    }

    public void errorNotification(String message) {
        notification.removeThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        abstractNotification(message, errorCss, errorStyle);
    }

    private void abstractNotification(String message, String css, String styleName) {
        H5 text = new H5(message);
        text.addClassName(styleName);
        notification.removeAll();
        notification.add(text);
        notification.setDuration(5000);
        notification.add(verticalLayout);

        StreamRegistration resource = UI.getCurrent().getSession().getResourceRegistry()
                .registerResource(new StreamResource("styles.css", () ->
                        new ByteArrayInputStream(css.getBytes(StandardCharsets.UTF_8))));

        UI.getCurrent().getPage().addStyleSheet(
                "base://" + resource.getResourceUri().toString());
        notification.open();
    }
}

package com.trade_accounting.config;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.authentication.LoginView;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import javax.servlet.http.HttpServletResponse;

@ParentLayout(AppView.class)
public class RedirectRouteNotFoundError extends VerticalLayout implements
        HasErrorParameter<NotFoundException> {

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {

        /* выводит строку на главной что запрашиваемая страница не найдена */
        add(new Label(String.format("The page %s does not exist.", event.getLocation().getPath())));

        /* если запрашиваемая страница не найдена, перенаправляет на /login (думаю так лучше не делать)
        так как если авторизованный пользователь попробует перейти на страницу которой нет его выкинет
        на страницу логина, но думаю решение по этому моменту в конце надо принимать.
        */
        //event.rerouteTo(LoginView.class);

        // возвращает ошибку 404, я думаю в appconfig можно обработать  и перенаправить
        // на страницу 404(создать нужно) так будет правильней. оставить только return,
        // а вышестоящий код закомментировать, чтоб небыло перенаправления.
        return HttpServletResponse.SC_NOT_FOUND;
    }
}


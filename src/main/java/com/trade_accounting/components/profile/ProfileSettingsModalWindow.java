package com.trade_accounting.components.profile;

import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class ProfileSettingsModalWindow extends Dialog {

    private PasswordField currentPassword = new PasswordField();
    private PasswordField updatePassword = new PasswordField();
    private PasswordField updatePasswordCheck = new PasswordField();

    private final String labelWidth = "200px";
    private final String fieldWidth = "300px";

    private final EmployeeService employeeService;
    private Div div;

    public ProfileSettingsModalWindow(EmployeeService employeeService) {
        this.employeeService = employeeService;
        div = new Div();

        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
        add(mainLayout());
    }

    private HorizontalLayout mainLayout() {
        HorizontalLayout main = new HorizontalLayout();
        div.removeAll();
        div.add(currentEmployeePassword(),
                updateEmployeePassword(),
                updateEmployeePasswordCheck(),
                buttons()
        );
        add(div);
        return main;
    }

    private Component currentEmployeePassword() {
        EmployeeDto employee = employeeService.getPrincipal();
        Label label = new Label("Текущий пароль:");
        label.setWidth(labelWidth);
        currentPassword.setWidth(fieldWidth);
        currentPassword.setRequired(true);
        currentPassword.setErrorMessage("Неверный пароль");
        currentPassword.setValueChangeMode(ValueChangeMode.ON_BLUR);
        currentPassword.addValueChangeListener(event -> {
            Object v = event.getValue();
            if (!v.equals((employee.getPassword()))) {
                currentPassword.setInvalid(true);
            } else currentPassword.setInvalid(false);
        });
        HorizontalLayout checkPassword = new HorizontalLayout(label, currentPassword);
        return checkPassword;
    }

    private Component updateEmployeePassword() {
        Label label = new Label("Новый пароль:");
        label.setWidth(labelWidth);
        updatePassword.setWidth(fieldWidth);
        updatePassword.setRequired(true);
        HorizontalLayout newPasswordLayout = new HorizontalLayout(label, updatePassword);
        return newPasswordLayout;
    }

    private Component updateEmployeePasswordCheck() {
        Label label = new Label("Проверка пароля:");
        label.setWidth(labelWidth);
        updatePasswordCheck.setWidth(fieldWidth);
        updatePasswordCheck.setRequired(true);
        updatePasswordCheck.setErrorMessage("Пароли не совпадают");
        updatePasswordCheck.setValueChangeMode(ValueChangeMode.ON_BLUR);
        updatePasswordCheck.addValueChangeListener(event -> {
            Object v = event.getValue();
            if (!v.equals(updatePassword.getValue())) {
                updatePasswordCheck.setInvalid(true);
            } else updatePasswordCheck.setInvalid(false);
        });
        HorizontalLayout doubleCheck = new HorizontalLayout(label, updatePasswordCheck);
        return doubleCheck;
    }

    private Button getButtonSave() {
        Button buttonSave = new Button("Сохранить");
        buttonSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        EmployeeDto employee = employeeService.getPrincipal();
        buttonSave.addClickListener(click -> {
            if (currentPassword.isInvalid() == false && updatePasswordCheck.isInvalid() == false) {
                employee.setPassword(updatePassword.getValue());
                employeeService.update(employee);
            } else {
                Dialog dialog = new Dialog();
                dialog.add(new Text("Введены неверные данные"));
                dialog.setWidth("400px");
                dialog.setHeight("70px");
                dialog.open();
            }
            close();
        });
        return buttonSave;
    }

    private Button getButtonCancel() {
        return new Button("Отменить", event -> close());
    }

    private Component buttons() {
        HorizontalLayout buttonsLayout = new HorizontalLayout(getButtonSave(), getButtonCancel());
        return buttonsLayout;
    }
}

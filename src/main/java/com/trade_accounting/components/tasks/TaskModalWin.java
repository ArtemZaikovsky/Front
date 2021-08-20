package com.trade_accounting.components.tasks;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;


public class TaskModalWin extends Dialog {


    public TaskModalWin() {

        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
        setWidth("800px");
        setHeight("450px");

        add(getHeader());
        add(getContent());
    }

    private Component getHeader() {
        HorizontalLayout header = new HorizontalLayout();
        Button saveButton = new Button("Сохранить", event -> {
            // save's action
            close();
        });
        Button closeButton = new Button("Закрыть", e -> {
            close();
        });
        header.add(saveButton, closeButton);

        return header;
    }

    private Component getContent() {
        HorizontalLayout contentField = new HorizontalLayout();

        TextArea taskDescriptionArea = new TextArea("Описание задачи");
        taskDescriptionArea.getStyle().set("maxHeight", "400px");
        taskDescriptionArea.setHeight("300px");
        taskDescriptionArea.setWidth("450px");

        contentField.add(taskDescriptionArea);
        contentField.add(componentsFields());
        return contentField;
    }

    private Component componentsFields() {
        VerticalLayout verticalComponents = new VerticalLayout();
        verticalComponents.setWidth("300px");

        Checkbox statusField = new Checkbox("Выполнена");

        Select<String> executor = new Select<>("Исполнитель 1", "Исполнитель 2");
        executor.setLabel("Исполнитель");

        DatePicker term = new DatePicker();
        term.setLabel("Срок");

        Div div = new Div();
        Anchor contactor = new Anchor("/tasks", "контрагентом");
        Anchor document = new Anchor("/tasks", "документом");
        div.add("Связать с : ");
        div.add(contactor);
        div.add(", ");
        div.add(document);
        div.getElement().getStyle().set("font-size", "14px");

        verticalComponents.add(statusField);
        verticalComponents.add(executor);
        verticalComponents.add(term);
        verticalComponents.add(div);


        return verticalComponents;
    }
}

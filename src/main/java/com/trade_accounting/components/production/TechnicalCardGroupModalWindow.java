package com.trade_accounting.components.production;

import com.trade_accounting.models.dto.TechnicalCardGroupDto;
import com.trade_accounting.services.interfaces.TechnicalCardGroupService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class TechnicalCardGroupModalWindow extends Dialog {

    private final TextField idField = new TextField(); //"Id"
    private final TextField nameField = new TextField(); //"Наименование"
    private final TextArea commentField = new TextArea(); //"Комментарий"
    private final TextField sortNumberField = new TextField(); //"Код"

    private final Binder<TechnicalCardGroupDto> technicalCardGroupDtoBinder = new Binder<>(TechnicalCardGroupDto.class);

    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "400px";
    private static final String MODAL_WINDOW_WIDTH = "650px";

    private final TechnicalCardGroupService technicalCardGroupService;

    private final TechnicalCardGroupDto technicalCardGroupDto;

    public TechnicalCardGroupModalWindow(TechnicalCardGroupDto technicalCardGroupDto,
                                         TechnicalCardGroupService technicalCardGroupService){
        this.technicalCardGroupDto = technicalCardGroupDto;
        this.technicalCardGroupService = technicalCardGroupService;

        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);

        idField.setValue(getFieldValueNotNull(String.valueOf(technicalCardGroupDto.getId())));
        nameField.setValue(getFieldValueNotNull(technicalCardGroupDto.getName()));
        commentField.setValue(getFieldValueNotNull(technicalCardGroupDto.getComment()));
        sortNumberField.setValue(getFieldValueNotNull(technicalCardGroupDto.getSortNumber()));

        add(new Text("Группа технических карт"), header(), technicalCardGroupAccordion());
        setWidth(MODAL_WINDOW_WIDTH);
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        nameField.setWidth("445px");
        header.add(getSaveButton(), getCancelButton());
        return header;
    }

    private Details technicalCardGroupAccordion() {
        Details technicalCardGroupDetails = new Details("", new Text(" "));
        technicalCardGroupDetails.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        technicalCardGroupDetails.setOpened(true);
        technicalCardGroupDetails.addContent(
                configureNameField(),
                configureCommentField(),
                configureSortNumberField()
                );
        add(technicalCardGroupDetails);
        return technicalCardGroupDetails;
    }

    private HorizontalLayout configureNameField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        nameField.setWidth(FIELD_WIDTH);
        technicalCardGroupDtoBinder.forField(nameField)
                .asRequired("Не заполнено!")
                .bind("name");
        Label label = new Label("Наименование");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, nameField);
        return horizontalLayout;
    }

    private HorizontalLayout configureCommentField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        commentField.setWidth("345px");
        technicalCardGroupDtoBinder.forField(commentField)
                .asRequired("Не заполнено!")
                .bind("comment");
        Label label = new Label("Комментарий");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, commentField);
        return horizontalLayout;
    }

    private HorizontalLayout configureSortNumberField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        sortNumberField.setWidth(FIELD_WIDTH);
        technicalCardGroupDtoBinder.forField(sortNumberField)
                .asRequired("Не заполнено!")
                .bind("sortNumber");
        Label label = new Label("Код");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, sortNumberField);
        return horizontalLayout;
    }

    private Button getSaveButton() {
        if (nameField.isEmpty()) {
            return new Button("Добавить", event -> {
                saveFields(technicalCardGroupDto);
                technicalCardGroupService.create(technicalCardGroupDto);
                close();
            });
        } else {
            return new Button("Изменить", event -> {
                saveFields(technicalCardGroupDto);
                technicalCardGroupService.update(technicalCardGroupDto);
            });
        }
    }

    private void saveFields(TechnicalCardGroupDto technicalCardGroupDto) {
        technicalCardGroupDto.setName(nameField.getValue());
        technicalCardGroupDto.setComment(commentField.getValue());
        technicalCardGroupDto.setSortNumber(sortNumberField.getValue());
    }

    private Button getCancelButton() {
        return new Button("Закрыть", event -> close());
    }
}

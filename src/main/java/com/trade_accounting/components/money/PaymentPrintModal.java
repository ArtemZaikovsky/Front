package com.trade_accounting.components.money;

import com.trade_accounting.services.interfaces.PaymentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class PaymentPrintModal extends Dialog {
    private static final String LABEL_WIDTH = "500px";
    private final PaymentService paymentService;

    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/payments_templates/";

    @Autowired
    public PaymentPrintModal(PaymentService paymentService) {
        this.paymentService = paymentService;
        add(header(), configurePrintSelect(), valueSelectPrint(), footer());
    }

    private HorizontalLayout header() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        H3 title = new H3("Создание печатной формы");
        title.setHeight("1.2em");
        horizontalLayout.add(title);
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        return horizontalLayout;
    }

    private HorizontalLayout configurePrintSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Создать печатную форму " +
                "по шаблону 'Список всех платежей'?");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label);
        horizontalLayout.setSpacing(true);
        return horizontalLayout;
    }

    private ComboBox<String> valueSelectPrint() {
        ComboBox<String> print = new ComboBox<>();
        print.setWidth("300px");
        print.setPlaceholder("Печатная форма");
        print.setItems("Открыть в браузере", "Скачать в формате Excel", "Скачать в формате PDF");
        downloadXls(print);
        return print;
    }

    private List<File> getXlsFile() {
        File dir = new File(pathForSaveXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(File::isFile).filter(x -> x.getName().contains(".xls"))
                .collect(Collectors.toList());
    }

    private Anchor getLinkToPaymentsXls(File file) {
        String paymentsTemplate = file.getName();
        PrintPaymentsXls printPaymentsXls = new PrintPaymentsXls(
                file.getPath(), paymentService.getAll());
        return new Anchor(new StreamResource(paymentsTemplate, printPaymentsXls::createReport),
                "Скачать файл");
    }

    private void downloadXls(ComboBox<String> print) {
        print.addValueChangeListener(x -> {
            if (x.getValue().equals("Скачать в формате Excel")) {
                getXlsFile().forEach(i -> add(getLinkToPaymentsXls(i)));
            } else {
                close();
            }
        });
    }

    private HorizontalLayout footer() {
        HorizontalLayout footer = new HorizontalLayout();
        VerticalLayout cancelverticalLayout = new VerticalLayout();
        cancelverticalLayout.add(getCancelButton());
        cancelverticalLayout.setWidth("100px");
        cancelverticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.BASELINE);
        footer.add(cancelverticalLayout);
        footer.setHeight("100px");
        footer.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        return footer;
    }

    private Button getCancelButton() {
        return new Button("Отмена", new Icon(VaadinIcon.CLOSE), event -> {
            close();
        });
    }
}

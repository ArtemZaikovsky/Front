package com.trade_accounting.components.contractors;

import com.trade_accounting.components.util.ValidTextField;
import com.trade_accounting.models.dto.BankAccountDto;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.LegalDetailDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.stream.Stream;

@SpringComponent
@UIScope
public class ContractModalWindow extends Dialog {

    private static final String FIELD_WIDTH = "400px";

    private Long contractId;
    private final DatePicker dateField = new DatePicker();
    private final TextField amountField = new TextField();
    private final Checkbox archiveField = new Checkbox();
    private final TextField commentField = new TextField();
    private final ValidTextField numberField = new ValidTextField();

    private final ComboBox<ContractorDto> selectContractor = new ComboBox<>();
    private final ComboBox<CompanyDto> selectCompany = new ComboBox<>();
    private final ComboBox<BankAccountDto> selectBankAccount = new ComboBox<>();
    private final ComboBox<LegalDetailDto> selectLegalDetail = new ComboBox<>();


    private final String labelWidth = "100px";

    private final String fieldWidth = "400px";

    private final ContractService contractService;
    private final ContractorService contractorService;
    private final CompanyService companyService;

    @Autowired
    public ContractModalWindow(ContractService contractService, ContractorService contractorService,
                               CompanyService companyService) {
        this.contractService = contractService;
        this.contractorService = contractorService;
        this.companyService = companyService;

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(header(""), accordionCompany());
    }

    private void setFields(ContractDto dto) {
        if (dto != null) {
            contractId = dto.getId();
            setField(dateField, dto.getDate());
            setField(amountField, dto.getAmount().toString());
            setField(archiveField, dto.getArchive());
            setField(commentField, dto.getComment());
            setField(numberField, dto.getNumber());
        }
        selectContractor.setValue(dto.getContractorDto());
        if (dto.getCompanyDto() != null) {
            selectCompany.setValue(dto.getCompanyDto());
        }

        if (dto.getLegalDetailDto() != null) {
            setField(selectLegalDetail, dto.getLegalDetailDto());
        }

        setField(selectBankAccount, dto.getBankAccountDto());

    }


    private void setField(AbstractField field, Object value) {
        if (value != null) {
            field.setValue(value);
        }
    }

    private HorizontalLayout header(String titleText) {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2(titleText);
        title.setId("title");
        title.setHeight("2.2em");
        title.setWidth("345px");
        header.add(title);
        header.add(getDeleteButton(), buttonSave(), buttonCancel());
        header.setId("header");
        return header;
    }

    private Accordion accordionCompany() {
        Accordion accordion = new Accordion();
        accordion.setWidth("575px");

        VerticalLayout layoutContract = new VerticalLayout();
        layoutContract.add(
                configureDateField(),
                configureAmountField(),
                configureArchiveField(),
                configureCommentField(),
                configureNumberField()
        );
        accordion.add("Договор", layoutContract).addThemeVariants(DetailsVariant.FILLED);

        VerticalLayout layoutInfo = new VerticalLayout();
        layoutInfo.add(
                configureContractor());
        accordion.add("Контрактор", layoutInfo).addThemeVariants(DetailsVariant.FILLED);

        VerticalLayout layoutPersons = new VerticalLayout();
        layoutPersons.add(
                configureCompany());
        accordion.add("Компания", layoutPersons).addThemeVariants(DetailsVariant.FILLED);

        VerticalLayout layoutDetails = new VerticalLayout();
        layoutDetails.add(
                configureLegalDetails());
        accordion.add("Юридические детали", layoutDetails).addThemeVariants(DetailsVariant.FILLED);

        VerticalLayout layoutBankAccount = new VerticalLayout();
        layoutBankAccount.add(
                configureBankAccount()
        );
        accordion.add("Банковские реквизиты", layoutBankAccount).addThemeVariants(DetailsVariant.FILLED);

        return accordion;
    }

    private HorizontalLayout configureContractor() {
        selectContractor.setItems(contractorService.getAll());
        selectContractor.setItemLabelGenerator(contractorDto -> contractorDto.getName());
        selectContractor.setWidth(FIELD_WIDTH);

        return getHorizontalLayout("Контрактор", selectContractor);
    }

    private HorizontalLayout configureCompany() {
        selectCompany.setItems(companyService.getAll());
        selectCompany.setItemLabelGenerator(
                companyDto -> companyDto.getName() + ", ИНН: " + companyDto.getInn()
        );
        selectCompany.addValueChangeListener(event -> {
            if (selectCompany.getValue() != null) {
//       TODO         selectBankAccount.setItems(selectCompany.getValue().getBankAccountDto());
//                selectLegalDetail.setItems(selectCompany.getValue().getLegalDetailDto());
            }
        });
        selectCompany.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Компания", selectCompany);
    }

    private HorizontalLayout configureBankAccount() {
        selectBankAccount.setWidth(FIELD_WIDTH);
        selectBankAccount.setItemLabelGenerator(
                bankAccountDto -> bankAccountDto.getBank() + " " + bankAccountDto.getAccount());
        return getHorizontalLayout("Банковский аккаунт", selectBankAccount);
    }

    private HorizontalLayout configureLegalDetails() {
        selectLegalDetail.setWidth(FIELD_WIDTH);
        selectLegalDetail.setItemLabelGenerator(
                legalDetailDto -> legalDetailDto.getLastName() + " "
                        + legalDetailDto.getFirstName() + " "
                        + legalDetailDto.getMiddleName()
        );
        return getHorizontalLayout("Юридические детали", selectLegalDetail);
    }

    private HorizontalLayout configureNumberField() {
        numberField.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Сортировочный номер", numberField);
    }

    private HorizontalLayout configureCommentField() {
        commentField.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Комментарий", commentField);
    }

    private HorizontalLayout configureArchiveField() {
        archiveField.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Архив", archiveField);
    }

    private HorizontalLayout configureAmountField() {
        amountField.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Сумма", amountField);
    }

    private HorizontalLayout configureDateField() {
        dateField.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Дата заключения", dateField);
    }

    private HorizontalLayout getHorizontalLayout(String title, Component field) {
        Label label = new Label(title);
        label.setWidth("100px");
        return new HorizontalLayout(label, field);
    }

    private Button buttonSave() {
        return new Button("Сохранить", event -> {
            ContractDto contractDto = new ContractDto();
            contractDto.setDate(dateField.getValue().toString());
            contractDto.setAmount(new BigDecimal(amountField.getValue()));
            contractDto.setArchive(archiveField.getValue());
            contractDto.setComment(commentField.getValue());
            contractDto.setNumber(numberField.getValue());
            contractDto.setContractorDto(selectContractor.getValue());
            contractDto.setCompanyDto(selectCompany.getValue());
            contractDto.setBankAccountDto(selectBankAccount.getValue());
            contractDto.setLegalDetailDto(selectLegalDetail.getValue());
            contractDto.setId(contractId);

            if (contractId == null) {
                contractService.create(contractDto);
            } else {
                contractService.update(contractDto);
            }
            clearAllFields();
            close();
        });
    }

    private Button buttonCancel() {
        return new Button("Закрыть", event -> {
            clearAllFields();
            close();
        });
    }

    private Button getDeleteButton() {
        return new Button("Удалить", event -> {
            contractService.deleteById(contractId);
            clearAllFields();
            close();
        });
    }

    public void configure() {
        setHeader("Добавление");
    }

    public void configure(ContractDto contractDto) {
        setHeader("Редактирование");
        setFields(contractDto);
    }

    private void clearAllFields() {
        dateField.clear();
        amountField.clear();
        archiveField.clear();
        commentField.clear();
        numberField.clear();
        Stream.of(selectContractor, selectCompany, selectBankAccount, selectLegalDetail)
                .forEach(select -> select.clear());
    }

    private void setHeader(String text) {
        H2 title = (H2) this.getChildren()
                .filter(element -> element.getId().isPresent() && element.getId().get().equals("header"))
                .findFirst().get()
                .getChildren().filter(component -> component.getId().isPresent() && component.getId().get().equals("title"))
                .findFirst().get();
        title.setText(text);
    }
}

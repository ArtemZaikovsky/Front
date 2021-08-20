package com.trade_accounting.components.profile;

import com.trade_accounting.models.dto.AddressDto;
import com.trade_accounting.models.dto.BankAccountDto;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.LegalDetailDto;
import com.trade_accounting.models.dto.TypeOfContractorDto;
import com.trade_accounting.services.interfaces.AddressService;
import com.trade_accounting.services.interfaces.BankAccountService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.LegalDetailService;
import com.trade_accounting.services.interfaces.TypeOfContractorService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CompanyModal extends Dialog {

    private static final String FIELD_WIDTH = "400px";

    private Long companyId;
    private final TextField name = new TextField();
    private final TextField inn = new TextField();
    private final TextArea commentToAddress = new TextArea();
    private final TextField email = new TextField();
    private final TextField phone = new TextField();
    private final TextField fax = new TextField();
    private final TextField leader = new TextField();
    private final TextField leaderManagerPosition = new TextField();
    private final TextField leaderSignature = new TextField();
    private final TextField chiefAccountant = new TextField();
    private final TextField chiefAccountantSignature = new TextField();
    private final Select<String> payerVat = new Select<>();
    private final TextField sortNumber = new TextField();
    private final TextField stamp = new TextField();

    private Long addressId;
    private final TextField addressIndex = new TextField();
    private final TextField addressCountry = new TextField();
    private final TextField addressRegion = new TextField();
    private final TextField addressCity = new TextField();
    private final TextField addressStreet = new TextField();
    private final TextField addressHouse = new TextField();
    private final TextField addressApartment = new TextField();
    private final TextField addressAnother = new TextField();

    private Long legalDetailId;
    private final TextField legalDetailLastName = new TextField();
    private final TextField legalDetailFirstName = new TextField();
    private final TextField legalDetailMiddleName = new TextField();
    private final TextArea legalDetailAddress = new TextArea();
    private final TextArea legalDetailCommentToAddress = new TextArea();
    private final TextField legalDetailInn = new TextField();
    private final TextField legalDetailOkpo = new TextField();
    private final TextField legalDetailOgrnip = new TextField();
    private final TextField legalDetailNumberOfTheCertificate = new TextField();
    private final DatePicker legalDetailDateOfTheCertificate = new DatePicker();

    private Long legalDetailAddressId;
    private final Checkbox checkboxAddress = new Checkbox();
    private final TextField legalDetailAddressIndex = new TextField();
    private final TextField legalDetailAddressCountry = new TextField();
    private final TextField legalDetailAddressRegion = new TextField();
    private final TextField legalDetailAddressCity = new TextField();
    private final TextField legalDetailAddressStreet = new TextField();
    private final TextField legalDetailAddressHouse = new TextField();
    private final TextField legalDetailAddressApartment = new TextField();
    private final TextField legalDetailAddressAnother = new TextField();

    private Long typeOfContractorId;
    private final Select<TypeOfContractorDto> typeOfContractorDtoSelect = new Select<>();

    private List<BankAccountDto> bankAccountDtos = new ArrayList<>();
    Set<Long> bankAccountDtoId = new HashSet<>();
    Set<Long> bankAccountDtoForDeleteId = new HashSet<>();
    private final TextField bankAccountBic = new TextField();
    private final TextField bankAccountBank = new TextField();
    private final TextField bankAccountAddress = new TextField();
    private final TextField bankAccountAccount = new TextField();
    private final TextField bankAccountKorAccount = new TextField();
    private final TextField bankAccountSortNumber = new TextField();
    private final Checkbox bankAccountMainAccount = new Checkbox();


    private Accordion accordionInfoAboutCompany = new Accordion();

    private final CompanyService companyService;
    private final AddressService addressService;
    private final LegalDetailService legalDetailService;
    private final TypeOfContractorService typeOfContractorService;
    private final BankAccountService bankAccountService;

    public CompanyModal(CompanyService companyService, AddressService addressService, LegalDetailService legalDetailService, TypeOfContractorService typeOfContractorService, BankAccountService bankAccountService) {
        this.companyService = companyService;
        this.addressService = addressService;
        this.legalDetailService = legalDetailService;
        this.typeOfContractorService = typeOfContractorService;
        this.bankAccountService = bankAccountService;
        configureModal("Добавление", null);
    }

    public CompanyModal(CompanyDto companyDto, CompanyService companyService, AddressService addressService, LegalDetailService legalDetailService, TypeOfContractorService typeOfContractorService, BankAccountService bankAccountService) {
        this.companyService = companyService;
        this.addressService = addressService;
        this.legalDetailService = legalDetailService;
        this.typeOfContractorService = typeOfContractorService;
        this.bankAccountService = bankAccountService;
        configureModal("Редактирование", companyDto);
        setFields(companyDto);
    }

    private void configureModal(String title, CompanyDto dto) {
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        accordionInfoAboutCompany = accordionCompany();
        accordionInfoAboutCompany.add("Банковские реквизиты", configureBankAccount(dto)).addThemeVariants(DetailsVariant.FILLED);
        add(header(title), accordionInfoAboutCompany);
    }

    private void setFields(CompanyDto dto) {
        companyId = dto.getId();
        setField(name, dto.getName());
        setField(inn, dto.getInn());
        setField(commentToAddress, dto.getCommentToAddress());
        setField(email, dto.getEmail());
        setField(phone, dto.getPhone());
        setField(fax, dto.getFax());
        setField(leader, dto.getLeader());
        setField(leaderManagerPosition, dto.getLeaderManagerPosition());
        setField(leaderSignature, dto.getLeaderSignature());
        setField(chiefAccountant, dto.getChiefAccountant());
        setField(chiefAccountantSignature, dto.getChiefAccountantSignature());
        setField(payerVat, Boolean.TRUE.equals(dto.getPayerVat()) ? "Да" : "Нет");
        setField(sortNumber, dto.getSortNumber());
        setField(stamp, dto.getStamp());

        if (dto.getAddressId() != null) {
            addressId = dto.getAddressId();
            AddressDto addressDto = addressService.getById(addressId);
            setField(addressIndex, addressDto.getIndex());
            setField(addressCountry, addressDto.getCountry());
            setField(addressRegion, addressDto.getRegion());
            setField(addressCity, addressDto.getCity());
            setField(addressStreet, addressDto.getStreet());
            setField(addressHouse, addressDto.getHouse());
            setField(addressApartment, addressDto.getApartment());
            setField(addressAnother, addressDto.getAnother());
        }

        if (dto.getLegalDetailDtoId() != null) {
            legalDetailId = dto.getLegalDetailDtoId();
            LegalDetailDto legalDetailDto = legalDetailService.getById(legalDetailId);
            setField(legalDetailLastName, legalDetailDto.getLastName());
            setField(legalDetailFirstName, legalDetailDto.getFirstName());
            setField(legalDetailMiddleName, legalDetailDto.getMiddleName());
            setField(legalDetailCommentToAddress, legalDetailDto.getCommentToAddress());
            setField(legalDetailInn, legalDetailDto.getInn());
            setField(legalDetailOkpo, legalDetailDto.getOkpo());
            setField(legalDetailOgrnip, legalDetailDto.getOgrn());
            setField(legalDetailNumberOfTheCertificate, legalDetailDto.getNumberOfTheCertificate());
            setDate(legalDetailDateOfTheCertificate, legalDetailDto.getDate());

            if (legalDetailDto.getAddressDtoId() != null) {
                legalDetailAddressId = legalDetailDto.getAddressDtoId();
                AddressDto legalDetailAddressDto = addressService.getById(legalDetailAddressId);
                setField(legalDetailAddressIndex, legalDetailAddressDto.getIndex());
                setField(legalDetailAddressCountry, legalDetailAddressDto.getCountry());
                setField(legalDetailAddressRegion, legalDetailAddressDto.getRegion());
                setField(legalDetailAddressCity, legalDetailAddressDto.getCity());
                setField(legalDetailAddressStreet, legalDetailAddressDto.getStreet());
                setField(legalDetailAddressHouse, legalDetailAddressDto.getHouse());
                setField(legalDetailAddressApartment, legalDetailAddressDto.getApartment());
                setField(legalDetailAddressAnother, legalDetailAddressDto.getAnother());
                if (legalDetailAddressId == addressId) {
                    checkboxAddress.setValue(true);
                } else {
                    checkboxAddress.setValue(false);
                }
            }

            typeOfContractorId = legalDetailDto.getTypeOfContractorDtoId();
            if (typeOfContractorId != null) {
                TypeOfContractorDto typeOfContractorDto = typeOfContractorService.getById(typeOfContractorId);
                typeOfContractorDtoSelect.setValue(typeOfContractorDto);
            }
        }
    }

    private HorizontalLayout header(String titleText) {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2(titleText);
        title.setHeight("2.2em");
        title.setWidth("345px");
        header.add(title);
        header.add(buttonSave(), buttonCancel());
        return header;
    }

    private Button buttonSave() {
        return new Button("Сохранить", event -> {
            AddressDto addressDto = new AddressDto();
            addressDto.setId(addressId);
            addressDto.setIndex(addressIndex.getValue());
            addressDto.setCountry(addressCountry.getValue());
            addressDto.setRegion(addressRegion.getValue());
            addressDto.setCity(addressCity.getValue());
            addressDto.setStreet(addressStreet.getValue());
            addressDto.setHouse(addressHouse.getValue());
            addressDto.setApartment(addressApartment.getValue());
            addressDto.setAnother(addressAnother.getValue());
            if (addressId == null) {
                addressId = addressService.create(addressDto).getId();
            } else {
                addressService.update(addressDto);
            }

            LegalDetailDto legalDetailDto = new LegalDetailDto();
            legalDetailDto.setId(legalDetailId);
            legalDetailDto.setLastName(legalDetailLastName.getValue());
            legalDetailDto.setFirstName(legalDetailFirstName.getValue());
            legalDetailDto.setMiddleName(legalDetailMiddleName.getValue());
            if (checkboxAddress.getValue()) {
                legalDetailDto.setAddressDtoId(addressId);
                if (legalDetailAddressId != null && legalDetailAddressId != addressId) {
                    addressService.deleteById(legalDetailAddressId);
                }
            } else {
                AddressDto legalDetailAddressDto = new AddressDto();
                legalDetailAddressDto.setId(legalDetailAddressId);
                legalDetailAddressDto.setIndex(legalDetailAddressIndex.getValue());
                legalDetailAddressDto.setCountry(legalDetailAddressCountry.getValue());
                legalDetailAddressDto.setRegion(legalDetailAddressRegion.getValue());
                legalDetailAddressDto.setCity(legalDetailAddressCity.getValue());
                legalDetailAddressDto.setStreet(legalDetailAddressStreet.getValue());
                legalDetailAddressDto.setHouse(legalDetailAddressHouse.getValue());
                legalDetailAddressDto.setApartment(legalDetailAddressApartment.getValue());
                legalDetailAddressDto.setAnother(legalDetailAddressAnother.getValue());
                if (legalDetailAddressId == null) {
                    legalDetailAddressId = addressService.create(legalDetailAddressDto).getId();
                } else {
                    addressService.update(legalDetailAddressDto);
                }
                legalDetailDto.setAddressDtoId(legalDetailAddressId);
            }
            legalDetailDto.setCommentToAddress(legalDetailCommentToAddress.getValue());
            legalDetailDto.setInn(legalDetailInn.getValue());
            legalDetailDto.setOkpo(legalDetailOkpo.getValue());
            legalDetailDto.setOgrn(legalDetailOgrnip.getValue());
            legalDetailDto.setNumberOfTheCertificate(legalDetailNumberOfTheCertificate.getValue());
            legalDetailDto.setDate(legalDetailDateOfTheCertificate.getValue() != null
                    ? legalDetailDateOfTheCertificate.getValue().toString() : null);
            legalDetailDto.setTypeOfContractorDtoId(typeOfContractorDtoSelect.getValue().getId());
            if (legalDetailId == null) {
                legalDetailId = legalDetailService.create(legalDetailDto).getId();
            } else {
                legalDetailService.update(legalDetailDto);
            }

            CompanyDto companyDto = new CompanyDto();
            companyDto.setId(companyId);
            companyDto.setName(name.getValue());
            companyDto.setInn(inn.getValue());
            companyDto.setAddressId(addressId);
            companyDto.setCommentToAddress(commentToAddress.getValue());
            companyDto.setEmail(email.getValue());
            companyDto.setPhone(phone.getValue());
            companyDto.setFax(fax.getValue());
            companyDto.setSortNumber(sortNumber.getValue());
            if (payerVat.getValue() != null) {
                companyDto.setPayerVat(payerVat.getValue().equals("Да"));
            }
            companyDto.setStamp(stamp.getValue());
            companyDto.setLeader(leader.getValue());
            companyDto.setLeaderManagerPosition(leaderManagerPosition.getValue());
            companyDto.setLeaderSignature(leaderSignature.getValue());
            companyDto.setChiefAccountant(chiefAccountant.getValue());
            companyDto.setChiefAccountantSignature(chiefAccountantSignature.getValue());
            companyDto.setLegalDetailDtoId(legalDetailId);
            for (BankAccountDto bankAccountDto : bankAccountDtos) {
                bankAccountDtoId.add(bankAccountService.create(bankAccountDto).getId());
            }

            for (Long id : bankAccountDtoForDeleteId) {
                bankAccountService.deleteById(id);
                bankAccountDtoId.remove(id);
            }
            companyDto.setBankAccountDtoIds(bankAccountDtoId.stream().collect(Collectors.toList()));
            if (companyId == null) {
                companyService.create(companyDto);
            } else {
                companyService.update(companyDto);
            }
            close();
        });
    }

    private Button buttonCancel() {
        return new Button("Закрыть", event -> close());
    }

    private Accordion accordionCompany() {
        Accordion accordion = new Accordion();
        accordion.setWidth("575px");

        VerticalLayout layoutInfo = new VerticalLayout();
        layoutInfo.add(
                configureName(),
                configureInn(),
                configureCommentToAddress(),
                configureEmail(),
                configurePhone(),
                configureFax(),
                configureSortNumber(),
                configurePayerVat(),
                configureStamp());

        VerticalLayout address = new VerticalLayout();
        address.add(
                configureAddressIndex(),
                configureAddressCountry(),
                configureAddressRegion(),
                configureAddressCity(),
                configureAddressStreet(),
                configureAddressHouse(),
                configureAddressApartment(),
                configureAddressAnother());
        accordion.add("Фактический адрес", address).addThemeVariants(DetailsVariant.FILLED);

        accordion.add("О юр. лице", layoutInfo).addThemeVariants(DetailsVariant.FILLED);

        VerticalLayout layoutPersons = new VerticalLayout();
        layoutPersons.add(
                configureLeader(),
                configureLeaderManagerPosition(),
                configureLeaderSignature(),
                configureChiefAccountant(),
                configureChiefAccountantSignature());
        accordion.add("Главные лица", layoutPersons).addThemeVariants(DetailsVariant.FILLED);

        VerticalLayout legalDetailAddress = new VerticalLayout();
        legalDetailAddress.add(
                configureCheckboxAddress(),
                configureLegalDetailAddressIndex(),
                configureLegalDetailAddressCountry(),
                configureLegalDetailAddressRegion(),
                configureLegalDetailAddressCity(),
                configureLegalDetailAddressStreet(),
                configureLegalDetailAddressHouse(),
                configureLegalDetailAddressApartment(),
                configureLegalDetailAddressAnother());
        accordion.add("Юридический адрес", legalDetailAddress).addThemeVariants(DetailsVariant.FILLED);

        VerticalLayout layoutDetails = new VerticalLayout();
        layoutDetails.add(
                configureLegalDetailLastName(),
                configureLegalDetailFirstName(),
                configureLegalDetailMiddleName(),
                configureLegalDetailCommentToAddress(),
                configureLegalDetailInn(),
                configureLegalDetailOkpo(),
                configureLegalDetailOgrnip(),
                configureLegalDetailNumberOfTheCertificate(),
                configureLegalDetailDateOfTheCertificate(),
                configureTypeOfContractor());
        accordion.add("Юридические детали", layoutDetails).addThemeVariants(DetailsVariant.FILLED);

        return accordion;
    }

    private Details getBankAccount(Long id) {
        BankAccountDto bankAccountDto = bankAccountService.getById(id);
        bankAccountDtoId.add(id);
        VerticalLayout layoutBankAccount = new VerticalLayout();
        layoutBankAccount.add(new Button(new Icon(VaadinIcon.CLOSE_BIG), event -> {
            bankAccountDtoForDeleteId.add(id);
            layoutBankAccount.setEnabled(false);
        }));
        Checkbox checkbox = new Checkbox();
        checkbox.setLabel("Основной счет?");
        checkbox.setReadOnly(true);
        if (bankAccountDto.getMainAccount()) {
            checkbox.setValue(true);
        } else {
            checkbox.setValue(false);
        }
        layoutBankAccount.add(checkbox);
        layoutBankAccount.add(new Label("БИК: " + bankAccountDto.getRcbic()));
        layoutBankAccount.add(new Label("Адрес: " + bankAccountDto.getAddress()));
        layoutBankAccount.add(new Label("К/с: " + bankAccountDto.getCorrespondentAccount()));
        layoutBankAccount.add(new Label("Р/с: " + bankAccountDto.getAccount()));
        layoutBankAccount.add(new Label("Текущий остаток: " + bankAccountDto.getSortNumber()));
        return new Details(bankAccountDto.getBank(), layoutBankAccount);
    }

    private VerticalLayout configureBankAccount(CompanyDto dto) {
        VerticalLayout layoutBankAccounts = new VerticalLayout();

        VerticalLayout newBankAccount = new VerticalLayout();
        bankAccountMainAccount.setLabel("Основной счет");
        newBankAccount.add(bankAccountMainAccount);
        bankAccountBic.setLabel("БИК");
        newBankAccount.add(bankAccountBic);
        bankAccountBank.setLabel("Банк");
        newBankAccount.add(bankAccountBank);
        bankAccountAddress.setLabel("Адрес");
        newBankAccount.add(bankAccountAddress);
        bankAccountKorAccount.setLabel("Кор. счет");
        newBankAccount.add(bankAccountKorAccount);
        bankAccountAccount.setLabel("Счет");
        newBankAccount.add(bankAccountAccount);
        bankAccountSortNumber.setLabel("Текущий остаток");
        newBankAccount.add(bankAccountSortNumber);

        newBankAccount.add(new Button(new Icon(VaadinIcon.PLUS), event -> {
            bankAccountDtos.add(new BankAccountDto(null, bankAccountBic.getValue(),
                    bankAccountBank.getValue(), bankAccountAddress.getValue(),
                    bankAccountKorAccount.getValue(), bankAccountAccount.getValue(),
                    bankAccountMainAccount.getValue(), bankAccountSortNumber.getValue()));
            VerticalLayout tempNewBankAccount = new VerticalLayout();
            Checkbox checkbox = new Checkbox();
            checkbox.setLabel("Основной счет?");
            checkbox.setReadOnly(true);
            checkbox.setValue(bankAccountMainAccount.getValue());
            tempNewBankAccount.add(checkbox);
            tempNewBankAccount.add(new Label("БИК: " + bankAccountBic.getValue()));
            tempNewBankAccount.add(new Label("Адрес: " + bankAccountAddress.getValue()));
            tempNewBankAccount.add(new Label("К/с: " + bankAccountKorAccount.getValue()));
            tempNewBankAccount.add(new Label("Р/с: " + bankAccountAccount.getValue()));
            tempNewBankAccount.add(new Label("Текущий остаток: " + bankAccountSortNumber.getValue()));
            layoutBankAccounts.add(new Details(bankAccountBank.getValue(), tempNewBankAccount));
            bankAccountBic.clear();
            bankAccountBank.clear();
            bankAccountAddress.clear();
            bankAccountKorAccount.clear();
            bankAccountAccount.clear();
            bankAccountMainAccount.clear();
            bankAccountSortNumber.clear();
        }));

        layoutBankAccounts.add(new Details("Новый счет", newBankAccount));
        if (dto != null)
            for (Long id : dto.getBankAccountDtoIds()) {
                layoutBankAccounts.add(getBankAccount(id));
                bankAccountDtoId.add(id);
            }
        return layoutBankAccounts;
    }

    private HorizontalLayout configureName() {
        name.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Наименование", name);
    }

    private HorizontalLayout configureInn() {
        inn.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("ИНН", inn);
    }

    private HorizontalLayout configureAddressIndex() {
        addressIndex.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Индекс", addressIndex);
    }

    private HorizontalLayout configureAddressCountry() {
        addressCountry.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Страна", addressCountry);
    }

    private HorizontalLayout configureAddressRegion() {
        addressRegion.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Регион", addressRegion);
    }

    private HorizontalLayout configureAddressCity() {
        addressCity.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Город", addressCity);
    }

    private HorizontalLayout configureAddressStreet() {
        addressStreet.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Улица", addressStreet);
    }

    private HorizontalLayout configureAddressHouse() {
        addressHouse.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Номер дома", addressHouse);
    }

    private HorizontalLayout configureAddressApartment() {
        addressApartment.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Номер квартиры", addressApartment);
    }

    private HorizontalLayout configureAddressAnother() {
        addressAnother.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Примечание", addressAnother);
    }

    private HorizontalLayout configureCommentToAddress() {
        commentToAddress.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Комментарий к адресу", commentToAddress);
    }

    private HorizontalLayout configureEmail() {
        email.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("E-mail", email);
    }

    private HorizontalLayout configurePhone() {
        phone.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Телефон", phone);
    }

    private HorizontalLayout configureFax() {
        fax.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Факс", fax);
    }

    private HorizontalLayout configureSortNumber() {
        sortNumber.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Нумерация", sortNumber);
    }

    private HorizontalLayout configurePayerVat() {
        payerVat.setWidth(FIELD_WIDTH);
        payerVat.setItems("Да", "Нет");
        return getHorizontalLayout("Плательщик НДС", payerVat);
    }

    private HorizontalLayout configureStamp() {
        stamp.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Печать", stamp);
    }

    private HorizontalLayout configureLeader() {
        leader.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Руководитель", leader);
    }

    private HorizontalLayout configureLeaderManagerPosition() {
        leaderManagerPosition.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Должность руководителя", leaderManagerPosition);
    }

    private HorizontalLayout configureLeaderSignature() {
        leaderSignature.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Подпись руководителя", leaderSignature);
    }

    private HorizontalLayout configureChiefAccountant() {
        chiefAccountant.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Главный бухгалтер", chiefAccountant);
    }

    private HorizontalLayout configureChiefAccountantSignature() {
        chiefAccountantSignature.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Подпись гл. бухгалтера", chiefAccountantSignature);
    }

    private HorizontalLayout configureLegalDetailLastName() {
        legalDetailLastName.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Фамилия", legalDetailLastName);
    }

    private HorizontalLayout configureLegalDetailFirstName() {
        legalDetailFirstName.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Имя", legalDetailFirstName);
    }

    private HorizontalLayout configureLegalDetailMiddleName() {
        legalDetailMiddleName.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Отчество", legalDetailMiddleName);
    }

    private HorizontalLayout configureLegalDetailCommentToAddress() {
        legalDetailCommentToAddress.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Комментарий к адресу", legalDetailCommentToAddress);
    }

    private HorizontalLayout configureLegalDetailInn() {
        legalDetailInn.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("ИНН", legalDetailInn);
    }

    private HorizontalLayout configureLegalDetailOkpo() {
        legalDetailOkpo.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("ОКПО", legalDetailOkpo);
    }

    private HorizontalLayout configureLegalDetailOgrnip() {
        legalDetailOgrnip.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("ОГРНИП", legalDetailOgrnip);
    }

    private HorizontalLayout configureLegalDetailNumberOfTheCertificate() {
        legalDetailNumberOfTheCertificate.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Номер сертификата", legalDetailNumberOfTheCertificate);
    }

    private HorizontalLayout configureLegalDetailDateOfTheCertificate() {
        legalDetailDateOfTheCertificate.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Дата сертификата", legalDetailDateOfTheCertificate);
    }

    private HorizontalLayout configureTypeOfContractor() {
        typeOfContractorDtoSelect.setWidth(FIELD_WIDTH);
        List<TypeOfContractorDto> typeOfContractorDto = typeOfContractorService.getAll();
        typeOfContractorDtoSelect.setItemLabelGenerator(TypeOfContractorDto::getName);
        typeOfContractorDtoSelect.setItems(typeOfContractorDto);
        add(typeOfContractorDtoSelect);
        return getHorizontalLayout("Тип компании", typeOfContractorDtoSelect);
    }

    private HorizontalLayout getHorizontalLayout(String title, Component field) {
        Label label = new Label(title);
        label.setWidth("100px");
        return new HorizontalLayout(label, field);
    }

    private HorizontalLayout configureCheckboxAddress() {
        checkboxAddress.setLabel("Совпадает с фактическим?");
        checkboxAddress.setValue(false);
        add(checkboxAddress);
        return getHorizontalLayout(" ", checkboxAddress);
    }

    private HorizontalLayout configureLegalDetailAddressIndex() {
        legalDetailAddressIndex.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Индекс", legalDetailAddressIndex);
    }

    private HorizontalLayout configureLegalDetailAddressCountry() {
        legalDetailAddressCountry.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Страна", legalDetailAddressCountry);
    }

    private HorizontalLayout configureLegalDetailAddressRegion() {
        legalDetailAddressRegion.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Регион", legalDetailAddressRegion);
    }

    private HorizontalLayout configureLegalDetailAddressCity() {
        legalDetailAddressCity.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Город", legalDetailAddressCity);
    }

    private HorizontalLayout configureLegalDetailAddressStreet() {
        legalDetailAddressStreet.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Улица", legalDetailAddressStreet);
    }

    private HorizontalLayout configureLegalDetailAddressHouse() {
        legalDetailAddressHouse.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Номер дома", legalDetailAddressHouse);
    }

    private HorizontalLayout configureLegalDetailAddressApartment() {
        legalDetailAddressApartment.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Номер квартиры", legalDetailAddressApartment);
    }

    private HorizontalLayout configureLegalDetailAddressAnother() {
        legalDetailAddressAnother.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Примечание", legalDetailAddressAnother);
    }

    private void setField(AbstractField field, String value) {
        if (value != null) {
            field.setValue(value);
        }
    }

    private void setDate(AbstractField field, LocalDate date) {
        if (date != null) {
            field.setValue(date);
        }
    }
}

package com.trade_accounting.components.goods;

import com.trade_accounting.models.dto.AttributeOfCalculationObjectDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.ImageDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.ProductGroupDto;
import com.trade_accounting.models.dto.ProductPriceDto;
import com.trade_accounting.models.dto.TaxSystemDto;
import com.trade_accounting.models.dto.TypeOfPriceDto;
import com.trade_accounting.models.dto.UnitDto;
import com.trade_accounting.services.interfaces.AttributeOfCalculationObjectService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.ImageService;
import com.trade_accounting.services.interfaces.ProductGroupService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.TaxSystemService;
import com.trade_accounting.services.interfaces.TypeOfPriceService;
import com.trade_accounting.services.interfaces.UnitService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.validator.BigDecimalRangeValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringComponent
@UIScope
@Slf4j
public class ServiceModalWindow extends Dialog {

    private final UnitService unitService;
    private final ContractorService contractorService;
    private final TaxSystemService taxSystemService;
    private final ProductService productService;
    private final ImageService imageService;
    private final ProductGroupService productGroupService;
    private final AttributeOfCalculationObjectService attributeOfCalculationObjectService;
    private final TypeOfPriceService typeOfPriceService;
    private final TextField nameTextField = new TextField();
    private final TextField descriptionField = new TextField();
    private final BigDecimalField weightNumberField = new BigDecimalField();
    private final BigDecimalField volumeNumberField = new BigDecimalField();
    private final BigDecimalField purchasePriceNumberField = new BigDecimalField();
    private final ComboBox<UnitDto> unitDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final ComboBox<TaxSystemDto> taxSystemDtoComboBox = new ComboBox<>();
    private final ComboBox<ProductGroupDto> productGroupDtoComboBox = new ComboBox<>();
    private final ComboBox<AttributeOfCalculationObjectDto> attributeOfCalculationObjectComboBox = new ComboBox<>();

    private final HorizontalLayout typeOfPriceLayout = new HorizontalLayout();
    private Map<TypeOfPriceDto, BigDecimalField> bigDecimalFields;
    private List<ImageDto> imageDtoList;
    private List<ImageDto> imageDtoListForRemove;
    private final HorizontalLayout imageHorizontalLayout = new HorizontalLayout();

    private final HorizontalLayout footer = new HorizontalLayout();
    private final Binder<ProductDto> productDtoBinder = new Binder<>(ProductDto.class);
    private final Binder<ProductPriceDto> priceDtoBinder = new Binder<>(ProductPriceDto.class);

    @Autowired
    public ServiceModalWindow(UnitService unitService,
                              ContractorService contractorService,
                              TaxSystemService taxSystemService,
                              ProductService productService,
                              ImageService imageService,
                              ProductGroupService productGroupService,
                              AttributeOfCalculationObjectService attributeOfCalculationObjectService,
                              TypeOfPriceService typeOfPriceService) {
        this.unitService = unitService;
        this.contractorService = contractorService;
        this.taxSystemService = taxSystemService;
        this.productService = productService;
        this.imageService = imageService;
        this.productGroupService = productGroupService;
        this.attributeOfCalculationObjectService = attributeOfCalculationObjectService;
        this.typeOfPriceService = typeOfPriceService;


        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
        add(getHeader());

        nameTextField.setPlaceholder("Введите наименование");
        productDtoBinder.forField(nameTextField)
                .withValidator(text -> text.length() >= 3, "Не менее трёх символов", ErrorLevel.ERROR)
                .bind(ProductDto::getName, ProductDto::setName);
        nameTextField.setValueChangeMode(ValueChangeMode.EAGER);
        add(getHorizontalLayout("Наименование", nameTextField));

//        weightNumberField.setPlaceholder("Введите вес");
//        productDtoBinder.forField(weightNumberField)
//                .withValidator(Objects::nonNull, "Введите вес")
//                .withValidator(new BigDecimalRangeValidator("Не верное значение", BigDecimal.ZERO, new BigDecimal("99999999999999999999")))//                .withValidator(value -> value < 0, "Не может быть меньше 0")
//                .bind(ProductDto::getWeight, ProductDto::setWeight);
//        weightNumberField.setValueChangeMode(ValueChangeMode.EAGER);
//        add(getHorizontalLayout("Вес", weightNumberField));

//        volumeNumberField.setPlaceholder("Введите объём");
//        productDtoBinder.forField(volumeNumberField)
//                .withValidator(Objects::nonNull, "Введите объём")
//                .withValidator(new BigDecimalRangeValidator("Не верное значение", BigDecimal.ZERO, new BigDecimal("99999999999999999999")))//                .withValidator(value -> value < 0, "Не может быть меньше 0")
//                .bind(ProductDto::getVolume, ProductDto::setVolume);
//        volumeNumberField.setValueChangeMode(ValueChangeMode.EAGER);
//        add(getHorizontalLayout("Объем", volumeNumberField));

        purchasePriceNumberField.setPlaceholder("Введите закупочную цену");
        productDtoBinder.forField(purchasePriceNumberField)
                .withValidator(Objects::nonNull, "Введите закупочную цену")
                .withValidator(new BigDecimalRangeValidator("Не верное значение", BigDecimal.ZERO, new BigDecimal("99999999999999999999")))//                .withValidator(value -> value < 0, "Не может быть меньше 0")
                .bind(ProductDto::getPurchasePrice, ProductDto::setPurchasePrice);
        purchasePriceNumberField.setValueChangeMode(ValueChangeMode.EAGER);
        add(getHorizontalLayout("Закупочная цена", purchasePriceNumberField));

        descriptionField.setPlaceholder("Введите описание");
        productDtoBinder.forField(descriptionField)
                .withValidator(text -> text.length() >= 3, "Не менее трёх символов", ErrorLevel.ERROR)
                .bind(ProductDto::getName, ProductDto::setName);
        descriptionField.setValueChangeMode(ValueChangeMode.EAGER);
        add(getHorizontalLayout("Описание", descriptionField));

        productDtoBinder.forField(unitDtoComboBox)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("unitDto");
        unitDtoComboBox.setItemLabelGenerator(UnitDto::getFullName);
        add(getHorizontalLayout("Единицы измерения", unitDtoComboBox));

//        productDtoBinder.forField(contractorDtoComboBox)
//                .withValidator(Objects::nonNull, "Не заполнено!")
//                .bind("contractorDto");
//        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
//        add(getHorizontalLayout("Поставщик", contractorDtoComboBox));

        productDtoBinder.forField(taxSystemDtoComboBox)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("taxSystemDto");
        taxSystemDtoComboBox.setItemLabelGenerator(TaxSystemDto::getName);
        add(getHorizontalLayout("Система налогообложения", taxSystemDtoComboBox));

        productDtoBinder.forField(productGroupDtoComboBox)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("productGroupDto");
        productGroupDtoComboBox.setItemLabelGenerator(ProductGroupDto::getName);
        add(getHorizontalLayout("Группа продуктов", productGroupDtoComboBox));

        productDtoBinder.forField(attributeOfCalculationObjectComboBox)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("attributeOfCalculationObjectDto");
        attributeOfCalculationObjectComboBox.setItemLabelGenerator(AttributeOfCalculationObjectDto::getName);
        add(getHorizontalLayout("Признак предмета расчета", attributeOfCalculationObjectComboBox));

        add(getHorizontalLayout("Типы цен", typeOfPriceLayout));
        footer.getStyle().set("padding-bottom", "30px");
        footer.getStyle().set("padding-top", "30px");
        add(footer);
    }

    @Override
    public void open() {
        init();
        footer.add(getFooterHorizontalLayout(getAddButton()));
        super.open();
    }


    public void open(ProductDto productDto) {
        init();
        productDto = productService.getById(productDto.getId());
        nameTextField.setValue(productDto.getName());
        descriptionField.setValue(productDto.getDescription());
        weightNumberField.setValue(productDto.getWeight());
        volumeNumberField.setValue(productDto.getVolume());
        purchasePriceNumberField.setValue(productDto.getPurchasePrice());

        unitDtoComboBox.setValue(productDto.getUnitDto());
        contractorDtoComboBox.setValue(productDto.getContractorDto());
        taxSystemDtoComboBox.setValue(productDto.getTaxSystemDto());
        productGroupDtoComboBox.setValue(productDto.getProductGroupDto());
        attributeOfCalculationObjectComboBox.setValue(productDto.getAttributeOfCalculationObjectDto());
        imageDtoList = productDto.getImageDtos();
        for (ImageDto imageDto : imageDtoList) {
            StreamResource resource = new StreamResource("image", () -> new ByteArrayInputStream(imageDto.getContent()));
            Image image = new Image(resource, "image");
            image.setHeight("100px");
            imageHorizontalLayout.add(image, getRemoveImageButton(productDto, image, imageDto));
        }
        initTypeOfPriceFrom(productDto.getProductPriceDtos());
        footer.add(getRemoveButton(productDto), getFooterHorizontalLayout(getUpdateButton(productDto)));

        super.open();
    }

    private void init() {
        nameTextField.clear();
        descriptionField.clear();
        weightNumberField.clear();
        volumeNumberField.clear();
        purchasePriceNumberField.clear();
        footer.removeAll();
        imageHorizontalLayout.removeAll();
        typeOfPriceLayout.removeAll();
        bigDecimalFields = new HashMap<>();
        imageDtoList = new ArrayList<>();
        imageDtoListForRemove = new ArrayList<>();
        unitDtoComboBox.setItems(unitService.getAll());
        contractorDtoComboBox.setItems(contractorService.getAll());//изменил
        taxSystemDtoComboBox.setItems(taxSystemService.getAll());
        productGroupDtoComboBox.setItems(productGroupService.getAll());
        attributeOfCalculationObjectComboBox.setItems(attributeOfCalculationObjectService.getAll());
        typeOfPriceLayout.add(getTypeOfPriceForm(typeOfPriceService.getAll()));
    }

    private Component getHeader() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        H2 title = new H2("Добавление услуги/работы");
        title.setHeight("2.2em");
        title.setWidth("345px");
        horizontalLayout.add(title);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(false);
//        verticalLayout.add(getImageButton(), imageHorizontalLayout);
        horizontalLayout.add(title, verticalLayout);
        return horizontalLayout;
    }

    private HorizontalLayout getFooterHorizontalLayout(Button addOrUpdateButton) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        horizontalLayout.add(addOrUpdateButton);
        horizontalLayout.add(new Button("Закрыть", event -> close()));
        horizontalLayout.setMargin(false);
        horizontalLayout.setWidth("100%");
        return horizontalLayout;
    }

    private Component getTypeOfPriceForm(List<TypeOfPriceDto> typeOfPriceDtos) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(false);
        verticalLayout.setSpacing(false);
        typeOfPriceDtos.forEach(typeOfPriceDto -> {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            Label label = new Label(typeOfPriceDto.getName());
            label.setWidth("150px");
            horizontalLayout.add(label);
            BigDecimalField field = new BigDecimalField();
            field.setWidth("200px");

            priceDtoBinder.forField(field)
                    .withValidator(Objects::nonNull, "Не заполнено!")
                    .withValidator(new BigDecimalRangeValidator("Не может быть меньше 0", BigDecimal.ZERO, new BigDecimal("99999999999999999999")))
                    .bind("value");
            field.setValueChangeMode(ValueChangeMode.EAGER);

            bigDecimalFields.put(typeOfPriceDto, field);
            horizontalLayout.add(field);
            verticalLayout.add(horizontalLayout);
        });
        return verticalLayout;
    }

    private void initTypeOfPriceFrom(List<ProductPriceDto> list) {
        list.forEach(productPriceDto -> {
            bigDecimalFields.get(productPriceDto.getTypeOfPriceDto()).setValue(productPriceDto.getValue());
        });
    }

//    private Component getImageButton() {
//        Button imageButton = new Button("Добавить фото");
//        Dialog dialog = new Dialog();
//        MultiFileMemoryBuffer memoryBuffer = new MultiFileMemoryBuffer();
//        Upload upload = new Upload(memoryBuffer);
//
//        upload.addFinishedListener(event -> {
//            try {
//                ImageDto imageDto = new ImageDto();
//                final String fileName = event.getFileName();
//                String fileExtension = fileName.substring(fileName.indexOf("."));
//                imageDto.setFileExtension(fileExtension);
//                imageDto.setContent(memoryBuffer.getInputStream(event.getFileName()).readAllBytes());
//                imageDtoList.add(imageDto);
//                StreamResource resource = new StreamResource("image", () -> new ByteArrayInputStream(imageDto.getContent()));
//                Image image = new Image(resource, "image");
//                image.setHeight("100px");
//                imageHorizontalLayout.add(image);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            dialog.close();
//        });
//
//        HorizontalLayout layout = new HorizontalLayout();
//        layout.setWidth("500px");
//        layout.getStyle().set("overflow", "auto");
//        dialog.add(upload);
//        imageButton.addClickListener(x -> dialog.open());
//        return imageButton;
//    }

    private Button getAddButton() {
        return new Button("Добавить", event -> {
            ProductDto productDto = new ProductDto();
            if (!productDtoBinder.validate().isOk() || !priceDtoBinder.validate().isOk()) {
                productDtoBinder.validate().notifyBindingValidationStatusHandlers();
                priceDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                updateProductDto(productDto);
                productService.create(productDto);
                Notification.show(String.format("Товар %s добавлен", productDto.getName()));
                close();
            }
        });
    }

    private Button getRemoveButton(ProductDto productDto) {
        Button deleteButton = new Button("Удалить", buttonClickEvent -> {
            productService.deleteById(productDto.getId());
            productDto.getImageDtos().forEach(el -> imageService.deleteById(el.getId()));
            Notification.show(String.format("Товар %s удален", productDto.getName()));
            close();
        });
        deleteButton.setMinWidth("100px");
        return deleteButton;
    }

    private Button getRemoveImageButton(ProductDto productDto, Image image, ImageDto imageDto) {
        Button deleteButton = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE_O), buttonClickEvent -> {
            imageHorizontalLayout.remove(image);
            productDto.getImageDtos().remove(imageDto);
            imageDtoListForRemove.add(imageDto);
        });

        deleteButton.setMinWidth("10px");
        return deleteButton;
    }

    private Button getUpdateButton(ProductDto productDto) {
        return new Button("Изменить", event -> {
            updateProductDto(productDto);
            productService.update(productDto);
            imageDtoListForRemove.forEach(el -> imageService.deleteById(el.getId()));

            Notification.show(String.format("Товар %s изменен", productDto.getName()));
            close();
        });
    }

    private void updateProductDto(ProductDto productDto) {
        productDto.setName(nameTextField.getValue());
        productDto.setWeight(weightNumberField.getValue());
        productDto.setVolume(volumeNumberField.getValue());
        productDto.setPurchasePrice(purchasePriceNumberField.getValue());
        productDto.setDescription(descriptionField.getValue());
        productDto.setUnitDto(unitDtoComboBox.getValue());
        productDto.setContractorDto(contractorDtoComboBox.getValue());
        productDto.setTaxSystemDto(taxSystemDtoComboBox.getValue());
        productDto.setProductGroupDto(productGroupDtoComboBox.getValue());
        productDto.setAttributeOfCalculationObjectDto((attributeOfCalculationObjectComboBox.getValue()));
        productDto.setImageDtos(imageDtoList);

        if (productDto.getProductPriceDtos() == null) {
            productDto.setProductPriceDtos(new ArrayList<>());
        }

        bigDecimalFields.forEach((typeOfPriceDto, bigDecimalField) -> {
            AtomicBoolean b = new AtomicBoolean(true);
            productDto.getProductPriceDtos().forEach(productPriceDto -> {
                if (productPriceDto.getTypeOfPriceDto().equals(typeOfPriceDto)) {

                    productPriceDto.setValue(bigDecimalField.getValue());

                    b.set(false);
                }
            });
            if (b.get()) {
                ProductPriceDto productPriceDto = new ProductPriceDto();
                productPriceDto.setTypeOfPriceDto(typeOfPriceDto);
                productPriceDto.setValue(bigDecimalField.getValue());
                productDto.getProductPriceDtos().add(productPriceDto);
            }
        });

    }

    private <T extends Component & HasSize> HorizontalLayout getHorizontalLayout(String labelText, T field) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label(labelText);
        field.setWidth("400px");
        label.setWidth("200px");
        horizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, label);
        horizontalLayout.add(label, field);
        return horizontalLayout;
    }

}

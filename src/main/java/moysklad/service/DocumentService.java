package moysklad.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import moysklad.data.DocumentRepository;
import moysklad.model.Position;
import moysklad.model.Product;
import moysklad.model.documents.Move;
import moysklad.model.documents.ProductDocInfo;
import moysklad.model.documents.Sale;
import moysklad.model.documents.Supply;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

@Stateless
public class DocumentService {

    private Logger log;
    private Validator validator;
    private DocumentRepository repository;
    private ProductService productService;
    private StoreService storeService;
    private PositionService positionService;

    public DocumentService() {
    }

    @Inject
    public DocumentService(Logger log, Validator validator, DocumentRepository repository,
                           ProductService productService, StoreService storeService, PositionService positionService) {
        this.log = log;
        this.validator = validator;
        this.repository = repository;
        this.productService = productService;
        this.storeService = storeService;
        this.positionService = positionService;
    }

    @Transactional
    public Supply createSupply(@NotNull Supply supply) throws Exception {
        log.info("Поступление под кодом " + supply.getCode() + " + на склад: " + supply.getStoreID());

        for (ProductDocInfo productDocInfo : supply.getPositions()) {
            validateProductDocInfo(productDocInfo);

            Product product = productService.findById(UUID.fromString(productDocInfo.getProductID()));
            product.setLastPurchasePrice(productDocInfo.getPurchasePrice());

            Position atCurrentStore = new Position();
            atCurrentStore.setProduct(product);
            atCurrentStore.setStore(storeService.findById(UUID.fromString(supply.getStoreID())));
            atCurrentStore.setStock(productDocInfo.getQuantity());
            positionService.persistOrUpdate(atCurrentStore);
        }
        repository.save(supply);
        return supply;
    }

    @Transactional
    public Move createMove(@NotNull Move move) throws Exception {
        log.info("Перемещение под кодом " + move.getCode()
                + " со склада " + move.getStoreID() + " на склад: " + move.getToStoreID());

        for (ProductDocInfo productDocInfo : move.getPositions()) {
            validateProductDocInfo(productDocInfo);
            Product product = productService.findById(UUID.fromString(productDocInfo.getProductID()));

            Position atCurrentStore = positionService.find(storeService.findById(UUID.fromString(move.getStoreID())), product);
            if (atCurrentStore.getStock().compareTo(productDocInfo.getQuantity()) == -1)
                throw new IllegalArgumentException("Not enough stock to move");

            Position toOtherStore = new Position();
            toOtherStore.setProduct(product);
            toOtherStore.setStore(storeService.findById(UUID.fromString(move.getToStoreID())));
            toOtherStore.setStock(productDocInfo.getQuantity());
            positionService.persistOrUpdate(toOtherStore);
            atCurrentStore.setStock(atCurrentStore.getStock().subtract(productDocInfo.getQuantity()));
        }
        repository.save(move);
        return move;
    }

    @Transactional
    public Sale createSale(@NotNull Sale sale) throws Exception {
        log.info("Отпуск товара под кодом " + sale.getCode()
                + " со склада " + sale.getStoreID());

        for (ProductDocInfo productDocInfo : sale.getPositions()) {
            validateProductDocInfo(productDocInfo);
            Product product = productService.findById(UUID.fromString(productDocInfo.getProductID()));
            product.setLastSellingPrice(productDocInfo.getSellingPrice());

            Position atCurrentStore = positionService.
                    find(storeService.findById(UUID.fromString(sale.getStoreID())), product);
            if (atCurrentStore.getStock().compareTo(new BigDecimal("0")) == -1
                    || atCurrentStore.getStock().compareTo(productDocInfo.getQuantity()) == -1)
                throw new IllegalArgumentException("Not enough stock to sale");
            atCurrentStore.setStock(atCurrentStore.getStock().subtract(productDocInfo.getQuantity()));
        }
        repository.save(sale);
        return sale;
    }


    public <T> List<T> findAllDocuments(Class<T> docClass) throws Exception {
        return repository.findAllOrderedByDate(docClass);
    }

    public <T> void validateDocument(Class<T> docClass, T doc, String code) throws ValidationException {
        Set<ConstraintViolation<T>> violations = validator.validate(doc);
        if (!violations.isEmpty()) throw new ConstraintViolationException(new HashSet<>(violations));

        T document = null;
        try {
            document = repository.findByCode(docClass, code);
        } catch (Exception e) {
        }
        if (document != null) {
            throw new ValidationException("Unique Violation. Document already exists");
        }
    }

    public void validateProductDocInfo(ProductDocInfo position) {
        Set<ConstraintViolation<ProductDocInfo>> violations = validator.validate(position);
        if (!violations.isEmpty()) throw new ConstraintViolationException(new HashSet<>(violations));
    }


}

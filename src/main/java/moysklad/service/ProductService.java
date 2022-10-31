package moysklad.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import moysklad.data.ProductRepository;
import moysklad.model.Product;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

@Stateless
public class ProductService {

    private Logger log;
    private Validator validator;
    private ProductRepository repository;

    public ProductService() {
    }

    @Inject
    public ProductService(Logger log, Validator validator, ProductRepository repository) {
        this.log = log;
        this.validator = validator;
        this.repository = repository;
    }

    public Product findById(UUID id) throws Exception {
        return repository.findById(id);
    }

    public List<Product> findByName(String name) throws Exception {
        return repository.findByName(name);
    }

    public Product add(Product product) throws Exception {
        log.info("Importing " + product.getName());
        repository.save(product);
        return product;
    }

    @Transactional
    public Product update(@NotNull Product product) throws Exception {
        Product persistedProduct = repository.findById(product.getId());
        log.info("Updating product with id: " + product.getId());

        if (product.getArticle() != null) persistedProduct.setArticle(product.getArticle());
        if (product.getName() != null) persistedProduct.setName(product.getName());

        if (!product.getLastPurchasePrice().equals(new BigDecimal("0.00")))
            persistedProduct.setLastPurchasePrice(product.getLastPurchasePrice());
        if (!product.getLastSellingPrice().equals(new BigDecimal("0.00")))
            persistedProduct.setLastSellingPrice(product.getLastSellingPrice());

        return persistedProduct;
    }


    @Transactional
    public void deleteById(UUID id) throws Exception {
        Product persistedProduct = repository.findById(id);
        repository.remove(persistedProduct);
        log.info("Deleting product with id: " + id);
    }

    public List<Product> findAllOrderedByName() throws Exception {
        return repository.findAllOrderedByName();
    }

    public void validateProduct(Product product) throws ConstraintViolationException {
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        if (!violations.isEmpty()) throw new ConstraintViolationException(new HashSet<>(violations));
    }

}

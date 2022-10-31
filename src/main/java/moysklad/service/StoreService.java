package moysklad.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import moysklad.data.StoreRepository;
import moysklad.model.Product;
import moysklad.model.Store;

import java.util.*;
import java.util.logging.Logger;

@Stateless
public class StoreService {

    private Logger log;
    private Validator validator;
    private StoreRepository repository;

    public StoreService() {
    }

    @Inject
    public StoreService(Logger log, Validator validator, StoreRepository repository) {
        this.repository = repository;
        this.validator = validator;
        this.log = log;
    }


    public List<Store> findAllOrderedByName() throws Exception {
        return repository.findAllOrderedByName();
    }

    public Store findById(UUID id) throws Exception {
        return repository.findById(id);
    }

    public Store add(Store store) throws Exception {
        log.info("Importing " + store.getName());
        repository.save(store);
        return store;
    }

    @Transactional
    public Store update(@NotNull Store store) throws Exception {
        Store persistedStore = repository.findById(store.getId());
        log.info("Updating store with id: " + store.getId());
        if (store.getName() != null) persistedStore.setName(store.getName());
        return persistedStore;
    }


    @Transactional
    public void delete(UUID id) throws Exception {
        Store persistedStore = repository.findById(id);
        repository.remove(persistedStore);
        log.info("Deleting store with id: " + id);
    }

    public void validateStore(Store store) throws ConstraintViolationException, ValidationException {
        Set<ConstraintViolation<Store>> violations = validator.validate(store);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }
        if (storeAlreadyExists(store.getName())) {
            throw new ValidationException("Unique Name Violation. Store already exists");
        }
    }

    public boolean storeAlreadyExists(String name) {
        Store store = null;
        try {
            store = repository.findByName(name);
        } catch (Exception e) {
        }
        return store != null;
    }
}

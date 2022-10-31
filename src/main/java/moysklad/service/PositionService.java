package moysklad.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

import jakarta.validation.constraints.NotNull;
import moysklad.data.PositionRepository;
import moysklad.model.Position;
import moysklad.model.Product;
import moysklad.model.Store;
import moysklad.model.reports.AllProducts;
import moysklad.model.reports.ProductStock;

import java.util.List;
import java.util.UUID;

@Stateless
public class PositionService {

    private PositionRepository positionRepository;

    public PositionService() {

    }

    @Inject
    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public void create(Position position) throws Exception {
        positionRepository.save(position);
    }

    public Position find(@NotNull Store store, @NotNull Product product) throws Exception {
        return positionRepository.find(store, product);
    }

    @Transactional
    public void persistOrUpdate(Position position) throws Exception {
        try {
            Position persisted = find(position.getStore(), position.getProduct());
            persisted.setStock(persisted.getStock().add(position.getStock()));
        } catch (NoResultException e) {
            positionRepository.save(position);
        }
    }

    public List<AllProducts> reportAllProducts() throws Exception {
        return positionRepository.findAllReport();
    }

    public List<ProductStock> reportProductStock() throws Exception {
        return positionRepository.findStockReport();
    }

    public List<ProductStock> reportProductStockByStore(UUID storeId) throws Exception {
        return positionRepository.findStockByStore(storeId);
    }

}

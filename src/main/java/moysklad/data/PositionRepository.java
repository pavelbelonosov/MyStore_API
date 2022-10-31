package moysklad.data;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import moysklad.model.Position;
import moysklad.model.Product;
import moysklad.model.Store;
import moysklad.model.reports.AllProducts;
import moysklad.model.reports.ProductStock;

@ApplicationScoped
public class PositionRepository {

    private EntityManager em;

    public PositionRepository() {
    }

    @Inject
    public PositionRepository(EntityManager em) {
        this.em = em;
    }

    public void save(@NotNull Position position) throws Exception {
        em.persist(position);
    }

    public void remove(@NotNull Position position) throws Exception {
        em.remove(position);
    }

    public Position find(@NotNull Store store, @NotNull Product product) throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Position> criteria = cb.createQuery(Position.class);
        Root<Position> position = criteria.from(Position.class);
        criteria.select(position).where(cb.and(
                cb.equal(position.get("store"), store),
                cb.equal(position.get("product"), product)));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<AllProducts> findAllReport() throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AllProducts> criteria = cb.createQuery(AllProducts.class);
        Root<Position> position = criteria.from(Position.class);
        criteria.select(cb.construct(AllProducts.class,
                        position.get("product").get("article"),
                        position.get("product").get("name"),
                        position.get("product").get("lastPurchasePrice"),
                        position.get("product").get("lastSellingPrice"))).
                groupBy(position.get("product").get("id"));
        return em.createQuery(criteria).getResultList();
    }

    public List<ProductStock> findStockReport() throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductStock> criteria = cb.createQuery(ProductStock.class);
        Root<Position> position = criteria.from(Position.class);
        criteria.select(cb.construct(
                        ProductStock.class,
                        position.get("product").get("article"),
                        position.get("product").get("name"),
                        cb.sum(position.<BigDecimal>get("stock")))).
                groupBy(position.get("product"));

        return em.createQuery(criteria).getResultList();
    }

    public List<ProductStock> findStockByStore(@NotNull UUID id) throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductStock> criteria = cb.createQuery(ProductStock.class);
        Root<Position> position = criteria.from(Position.class);
        criteria.select(cb.construct(
                        ProductStock.class,
                        position.get("product").get("article"),
                        position.get("product").get("name"),
                        cb.sum(position.<BigDecimal>get("stock")))).
                where(cb.equal(position.get("store").<UUID>get("id"), id)).
                groupBy(position.get("product"));
        return em.createQuery(criteria).getResultList();
    }
}

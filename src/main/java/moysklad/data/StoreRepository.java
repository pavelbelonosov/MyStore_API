package moysklad.data;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotNull;
import moysklad.model.Store;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class StoreRepository {

    private EntityManager em;

    public StoreRepository() {
    }

    @Inject
    public StoreRepository(EntityManager em) {
        this.em = em;
    }

    public Store findById(@NotNull UUID id) throws Exception {
        Store store = em.find(Store.class, id);
        if (store == null) throw new IllegalArgumentException("Store with given ID not present");
        return store;
    }

    public Store findByName(@NotNull String name) throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Store> criteria = cb.createQuery(Store.class);
        Root<Store> store = criteria.from(Store.class);
        criteria.select(store).where(cb.equal(store.get("name"), name));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Store> findAllOrderedByName() throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Store> criteria = cb.createQuery(Store.class);
        Root<Store> store = criteria.from(Store.class);
        store.fetch("positions", JoinType.LEFT);
        criteria.select(store).orderBy(cb.asc(store.get("name")));
        return em.createQuery(criteria).getResultList();
    }

    public void save(@NotNull Store store) throws Exception {
        em.persist(store);
    }

    public void remove(@NotNull Store store) throws Exception {
        em.remove(store);
    }
}

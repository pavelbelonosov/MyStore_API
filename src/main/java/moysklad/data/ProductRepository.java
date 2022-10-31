package moysklad.data;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import moysklad.model.Product;

@ApplicationScoped
public class ProductRepository {

    private EntityManager em;

    public ProductRepository() {
    }

    @Inject
    public ProductRepository(EntityManager em) {
        this.em = em;
    }

    public Product findById(@NotNull UUID id) throws Exception {
        Product product = em.find(Product.class, id);
        if (product == null) throw new IllegalArgumentException("Product with given ID not present");
        return product;
    }

    public List<Product> findByArticle(@NotNull String article) throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product).where(cb.equal(product.get("article"), article));
        return em.createQuery(criteria).getResultList();
    }

    public List<Product> findByName(@NotNull String name) throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product).where(cb.equal(product.get("name"), name));
        return em.createQuery(criteria).getResultList();
    }

    public List<Product> findAllOrderedByName() throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product).orderBy(cb.asc(product.get("name"))).distinct(true);
        return em.createQuery(criteria).getResultList();
    }

    public void save(@NotNull Product product) throws Exception {
        em.persist(product);
    }

    public void remove(@NotNull Product product) throws Exception {
        em.remove(product);
    }

}

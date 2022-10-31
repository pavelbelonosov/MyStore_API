package moysklad.data;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class DocumentRepository {

    private EntityManager em;

    public DocumentRepository(){
    }

    @Inject
    public DocumentRepository(EntityManager em){
        this.em = em;
    }

    public <T> T findById(Class<T> docClass, UUID id) throws Exception{
        T document = em.find(docClass, id);
        if(document == null) throw new IllegalArgumentException("Document with given ID not found");
        return document;
    }

    public <T> T findByCode(Class<T> docClass, String code) throws Exception{
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = cb.createQuery(docClass);
        Root<T> document = criteria.from(docClass);
        criteria.select(document).where(cb.equal(document.get("code"), code));
        return em.createQuery(criteria).getSingleResult();
    }

    public <T> void save(@NotNull T doc) throws Exception {
        em.persist(doc);
    }

    public <T> void remove(@NotNull T doc) throws Exception {
        em.remove(doc);
    }

    public <T> List<T> findAllOrderedByDate(Class<T> docClass) throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = cb.createQuery(docClass);
        Root<T> document = criteria.from(docClass);
        document.fetch("positions", JoinType.LEFT);
        criteria.select(document).orderBy(cb.desc(document.get("created")));
        return em.createQuery(criteria).getResultList();
    }
}

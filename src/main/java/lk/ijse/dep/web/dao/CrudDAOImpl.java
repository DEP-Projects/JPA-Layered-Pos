package lk.ijse.dep.web.dao;

import lk.ijse.dep.web.entity.SuperEntity;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class CrudDAOImpl<T extends SuperEntity, K extends Serializable> implements CrudDAO<T, K> {

    private EntityManager entityManager;
    private Class<T> entityClass;

    public CrudDAOImpl() {
        entityClass = (Class<T>)(((ParameterizedType)(this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]);
    }

    protected EntityManager getEntityManager(){
        return this.entityManager;
    }

    @Override
    public void setEntityManager(EntityManager entityManager) throws Exception {
        this.entityManager = entityManager;
    }

    @Override
    public void save(T entity) throws Exception {
        entityManager.persist(entity);
    }

    @Override
    public void update(T entity) throws Exception {
        entityManager.merge(entity);
    }

    @Override
    public void delete(K key) throws Exception {
        entityManager.remove(entityManager.getReference(entityClass, key));
    }

    @Override
    public List<T> getAll() throws Exception {
        return entityManager.createQuery("SELECT x FROM "+ entityClass.getName()+ " x").getResultList();
    }

    @Override
    public T get(K key) throws Exception {
        return entityManager.find(entityClass, key);
    }
}

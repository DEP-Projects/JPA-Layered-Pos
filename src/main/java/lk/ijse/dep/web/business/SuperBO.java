package lk.ijse.dep.web.business;

import javax.persistence.EntityManager;
import java.sql.Connection;

public interface SuperBO {

    public void setEntityManager(EntityManager entityManager) throws Exception;

}

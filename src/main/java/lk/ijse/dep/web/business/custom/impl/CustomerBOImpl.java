package lk.ijse.dep.web.business.custom.impl;

import lk.ijse.dep.web.business.custom.CustomerBO;
import lk.ijse.dep.web.dao.DAOFactory;
import lk.ijse.dep.web.dao.DAOTypes;
import lk.ijse.dep.web.dao.custom.CustomerDAO;
import lk.ijse.dep.web.dto.CustomerDTO;
import lk.ijse.dep.web.entity.Customer;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerBOImpl implements CustomerBO {

    private CustomerDAO customerDAO;
    private EntityManager entityManager;

    @Override
    public void setEntityManager(EntityManager entityManager) throws Exception {
        this.entityManager = entityManager;
        customerDAO.setEntityManager(entityManager);
    }

    public CustomerBOImpl() {

        customerDAO = DAOFactory.getInstance().getDAO(DAOTypes.CUSTOMER);
    }



    @Override
    public void saveCustomer(CustomerDTO dto) throws Exception {
        entityManager.getTransaction().begin();
        customerDAO.save(new Customer(dto.getId(), dto.getName(), dto.getAddress()));
        entityManager.getTransaction().commit();
    }

    @Override
    public void updateCustomer(CustomerDTO dto) throws Exception {
        entityManager.getTransaction().begin();
        customerDAO.update(new Customer(dto.getId(), dto.getName(), dto.getAddress()));
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteCustomer(String customerId) throws Exception {
        entityManager.getTransaction().begin();
        customerDAO.delete(customerId);
        entityManager.getTransaction().commit();
    }

    @Override
    public List<CustomerDTO> findAllCustomers() throws Exception {
        entityManager.getTransaction().begin();
        List<CustomerDTO> collect = customerDAO.getAll().stream().
                map(c -> new CustomerDTO(c.getId(), c.getName(), c.getAddress())).collect(Collectors.toList());
        entityManager.getTransaction().commit();
        return collect;
    }
}

package lk.ijse.dep.web.business.custom.impl;

import lk.ijse.dep.web.business.custom.ItemBO;
import lk.ijse.dep.web.dao.DAOFactory;
import lk.ijse.dep.web.dao.DAOTypes;
import lk.ijse.dep.web.dao.custom.ItemDAO;
import lk.ijse.dep.web.dto.CustomerDTO;
import lk.ijse.dep.web.dto.ItemDTO;
import lk.ijse.dep.web.entity.Item;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class ItemBOImpl implements ItemBO {

    private ItemDAO itemDAO;
    private EntityManager entityManager;

    @Override
    public void setEntityManager(EntityManager entityManager) throws Exception {
        this.entityManager = entityManager;
        itemDAO.setEntityManager(entityManager);
    }

    public ItemBOImpl() {

        itemDAO = DAOFactory.getInstance().getDAO(DAOTypes.ITEM);

    }

    @Override
    public void saveItem(ItemDTO dto) throws Exception {
         entityManager.getTransaction().begin();
         itemDAO.save(new Item(dto.getCode(), dto.getDescription(), dto.getUnitPrice(), dto.getQtyOnHand()));
         entityManager.getTransaction().commit();
    }

    @Override
    public void updateItem(ItemDTO dto) throws Exception {
         entityManager.getTransaction().begin();
         itemDAO.update(new Item(dto.getCode(), dto.getDescription(), dto.getUnitPrice(), dto.getQtyOnHand()));
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteItem(String code) throws Exception {
        entityManager.getTransaction().begin();
         itemDAO.delete(code);
        entityManager.getTransaction().commit();
    }

    @Override
    public List<ItemDTO> findAllItems() throws Exception {
        entityManager.getTransaction().begin();
        List<ItemDTO> collect = itemDAO.getAll().stream().
                map(i -> new ItemDTO(i.getCode(), i.getDescription(), i.getUnitPrice(), i.getQtyOnHand())).collect(Collectors.toList());
        entityManager.getTransaction().commit();
        return collect;
    }

}

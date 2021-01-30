package lk.ijse.dep.web.business.custom.impl;

import lk.ijse.dep.web.business.custom.OrderBO;
import lk.ijse.dep.web.dao.DAOFactory;
import lk.ijse.dep.web.dao.DAOTypes;
import lk.ijse.dep.web.dao.custom.CustomerDAO;
import lk.ijse.dep.web.dao.custom.ItemDAO;
import lk.ijse.dep.web.dao.custom.OrderDAO;
import lk.ijse.dep.web.dao.custom.OrderDetailDAO;
import lk.ijse.dep.web.dto.OrderDTO;
import lk.ijse.dep.web.entity.Item;
import lk.ijse.dep.web.entity.Order;
import lk.ijse.dep.web.entity.OrderDetail;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OrderBOImpl implements OrderBO {

    private OrderDAO orderDAO;
    private OrderDetailDAO orderDetailDAO;
    private ItemDAO itemDAO;
    private CustomerDAO customerDAO;
    private EntityManager entityManager;

    @Override
    public void setEntityManager(EntityManager entityManager) throws Exception {
        this.entityManager = entityManager;
        itemDAO.setEntityManager(entityManager);
        orderDAO.setEntityManager(entityManager);
        orderDetailDAO.setEntityManager(entityManager);
        customerDAO.setEntityManager(entityManager);
    }

    public OrderBOImpl() {
        orderDAO = DAOFactory.getInstance().getDAO(DAOTypes.ORDER);
        orderDetailDAO = DAOFactory.getInstance().getDAO(DAOTypes.ORDER_DETAIL);
        itemDAO = DAOFactory.getInstance().getDAO(DAOTypes.ITEM);
    }


    @Override
    public void placeOrder(OrderDTO dto) throws Exception {
        entityManager.getTransaction().begin();
        try {
            boolean result = false;

            /* 1. Saving the order */
            orderDAO.save(new Order(dto.getOrderId(), Date.valueOf(dto.getOrderDate()), customerDAO.get(dto.getCustomerId())));

            /* 2. Saving Order Details -> Updating the stock */
            List<OrderDetail> orderDetails = dto.getOrderDetails().stream().
                    map(detail -> new OrderDetail(dto.getOrderId(), detail.getItemCode(), detail.getQty(), detail.getUnitPrice()))
                    .collect(Collectors.toList());
            for (OrderDetail orderDetail : orderDetails) {
                orderDetailDAO.save(orderDetail);

                /* 3. Let's update the stock */
                Item item = itemDAO.get(orderDetail.getOrderDetailPK().getItemCode());
                if (item.getQtyOnHand() - orderDetail.getQty() < 0) {
                    throw new RuntimeException("Invalid stock");
                }
                item.setQtyOnHand(item.getQtyOnHand() - orderDetail.getQty());
                itemDAO.update(item);

            }

            entityManager.getTransaction().commit();

        } catch (Throwable t) {
            entityManager.getTransaction().rollback();
            throw t;
        }
    }

    @Override
    public List<OrderDTO> searchOrdersByCustomerName(String name) throws Exception {
        return null;
    }
}

package com.bookStore.admin.order.dao;

import java.util.List;
import java.util.Map;

import com.bookStore.commons.beans.Order;
import com.bookStore.commons.beans.OrderItem;
import com.bookStore.commons.beans.Product;
import com.bookStore.commons.beans.User;

public interface IOrderDao {

	List<Order> selectOrder();

	List<Order> selectOrderByCondition(Map<Object, Object> map);

	Order selectOrderById(String id);

	void deleteOrderById(String id);

	void deleteOrderItemById(String id);

	User selectUserById(String id);

	List<Product> selectProductById(String id);

	List<OrderItem> selectOrderItemById(String id);

	int selectOrderByCount(Order order);


}

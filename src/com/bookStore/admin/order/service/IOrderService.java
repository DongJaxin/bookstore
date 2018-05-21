package com.bookStore.admin.order.service;

import java.util.List;

import com.bookStore.commons.beans.Order;
import com.bookStore.commons.beans.OrderItem;
import com.bookStore.commons.beans.Product;
import com.bookStore.commons.beans.User;
import com.bookStore.utils.PageModel;

public interface IOrderService {

	List<Order> findOrder();

	List<Order> findOrderByManyCondition(Order order, PageModel pageModel);

	Order findOrderById(String id);

	void delOrderById(String id);

	User findUserById(String id);

	List<Product> findProductById(String id);

	List<OrderItem> findOrderItemById(String id);

	int findOrderByCount(Order order);

}

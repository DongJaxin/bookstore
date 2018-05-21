package com.bookStore.admin.order.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookStore.admin.order.dao.IOrderDao;
import com.bookStore.admin.order.service.IOrderService;
import com.bookStore.commons.beans.Order;
import com.bookStore.commons.beans.OrderItem;
import com.bookStore.commons.beans.Product;
import com.bookStore.commons.beans.User;
import com.bookStore.utils.PageModel;
@Service
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private IOrderDao orderDao;
	
	@Override
	public List<Order> findOrder() {
		// TODO Auto-generated method stub
		return orderDao.selectOrder();
	}

	@Override
	public List<Order> findOrderByManyCondition(Order order,PageModel pageModel) {
		Map<Object, Object> map = new HashMap<>();
		map.put("order", order);
		map.put("start", pageModel.getFirstLimitParam());
		map.put("pageSize", pageModel.getPageSize());
		return orderDao.selectOrderByCondition(map);
	}

	@Override
	public Order findOrderById(String id) {
		// TODO Auto-generated method stub
		return orderDao.selectOrderById(id);
	}

	@Override
	public void delOrderById(String id) {
		
		orderDao.deleteOrderItemById(id);
		orderDao.deleteOrderById(id);
	}

	@Override
	public User findUserById(String id) {
		// TODO Auto-generated method stub
		return orderDao.selectUserById(id);
	}

	@Override
	public List<Product> findProductById(String id) {
		// TODO Auto-generated method stub
		return orderDao.selectProductById(id);
	}

	@Override
	public List<OrderItem> findOrderItemById(String id) {
		// TODO Auto-generated method stub
		return orderDao.selectOrderItemById(id);
	}

	@Override
	public int findOrderByCount(Order order) {
		// TODO Auto-generated method stub
		return orderDao.selectOrderByCount(order);
	}

}

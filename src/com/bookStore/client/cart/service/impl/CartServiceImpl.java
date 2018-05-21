package com.bookStore.client.cart.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookStore.client.cart.dao.ICartDao;
import com.bookStore.client.cart.service.ICartService;
import com.bookStore.commons.beans.Order;
import com.bookStore.commons.beans.OrderItem;
import com.bookStore.commons.beans.Product;

@Service
public class CartServiceImpl implements ICartService {

	@Autowired
	private ICartDao cartDao;
//	@Override
//	public void addOrderItem(OrderItem item) {
//
//		cartDao.insertOrderItem(item);
//	}
//
//	@Override
//	public void creatOrder(Order order) {
//
//		cartDao.insertOrder(order);
//
//	}

	@Override
	public void paySuccess(String out_trade_no) {
		
		cartDao.updateOrderPaystate(out_trade_no);
	}

@Override
public void creatOrder(Order order, Map<Product, Integer> cart) throws Exception {
	cartDao.insertOrder(order);
	
	
	for(Product p: cart.keySet()){
		OrderItem item = new OrderItem();
		item.setOrder(order);
		item.setProduct(p);
		item.setBuynum(cart.get(p));
		
		cartDao.updateProductPnum(item);
		
		cartDao.insertOrderItem(item);
	}
}

}

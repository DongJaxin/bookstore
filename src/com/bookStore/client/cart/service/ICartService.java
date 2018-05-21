package com.bookStore.client.cart.service;

import java.util.Map;

import com.bookStore.commons.beans.Order;
import com.bookStore.commons.beans.OrderItem;
import com.bookStore.commons.beans.Product;

public interface ICartService {

	//void addOrderItem(OrderItem item);

	//void creatOrder(Order order);

	void paySuccess(String out_trade_no);

	void creatOrder(Order order, Map<Product, Integer> cart) throws Exception;

}

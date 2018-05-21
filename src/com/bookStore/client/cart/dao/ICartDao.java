package com.bookStore.client.cart.dao;

import com.bookStore.commons.beans.Order;
import com.bookStore.commons.beans.OrderItem;

public interface ICartDao {

	void insertOrderItem(OrderItem item);

	void insertOrder(Order order);

	void updateOrderPaystate(String out_trade_no);

	void updateProductPnum(OrderItem item);

}

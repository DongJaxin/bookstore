package com.bookStore.admin.product.dao;

import java.util.List;
import java.util.Map;

import com.bookStore.commons.beans.Product;
import com.bookStore.commons.beans.ProductList;

public interface IAdminProductDao {

	List<Product> selectProduct();

	List<Product> selectProductByManyCondition(Map map);

	void insertProduct(Product product);

	Product selectProductById(String id);

	void updateProduct(Product product);

	void deleteProduct(String id);

	List<ProductList> selectProductList(Map map);

}

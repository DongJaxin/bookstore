package com.bookStore.client.product.service;

import java.util.List;

import com.bookStore.commons.beans.Notice;
import com.bookStore.commons.beans.Product;
import com.bookStore.utils.PageModel;

public interface IProductService {

	List<Product> findProductByCategory(String category, PageModel pageModel);

	int findProductByCount(String category);

	Product findProductById(String id);

	int findProductByNameCount(String name);

	List<Product> findProductByName(String name, PageModel pageModel);

	Notice findRecentNotice();

	List<Product> findWeekHotProduct();

	
}

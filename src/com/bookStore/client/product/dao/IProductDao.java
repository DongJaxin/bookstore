package com.bookStore.client.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bookStore.commons.beans.Notice;
import com.bookStore.commons.beans.Product;

public interface IProductDao {

	List<Product> selectProductByCategory(Map map);

	int selectProductByCategoryCount(@Param("category")String category);

	Product selectProductById(String id);

	int selectProductByNameCount(@Param("name")String name);

	List<Product> selectProductByName(Map map);

	Notice selectRecentNotice();

	List<Product> selectWeekHotProduct();

}

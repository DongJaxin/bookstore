package com.bookStore.client.product.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookStore.client.product.dao.IProductDao;
import com.bookStore.client.product.service.IProductService;
import com.bookStore.commons.beans.Notice;
import com.bookStore.commons.beans.Product;
import com.bookStore.utils.PageModel;

@Service
public class ProductServiceImpl implements IProductService {

	@Autowired
	private IProductDao productDao;
	
	@Override
	public List<Product> findProductByCategory(String category, PageModel pageModel) {
		// TODO Auto-generated method stub
		Map map = new HashMap<>();
		map.put("category", category);
		map.put("start", pageModel.getFirstLimitParam());
		map.put("pageSize", pageModel.getPageSize());
		
		return productDao.selectProductByCategory(map);
	}

	@Override
	public int findProductByCount(String category) {
		// TODO Auto-generated method stub
		return productDao.selectProductByCategoryCount(category);
	}

	@Override
	public Product findProductById(String id) {
		// TODO Auto-generated method stub
		return productDao.selectProductById(id);
	}

	@Override
	public int findProductByNameCount(String name) {
		// TODO Auto-generated method stub
		return productDao.selectProductByNameCount(name);
	}

	@Override
	public List<Product> findProductByName(String name, PageModel pageModel) {
		Map map = new HashMap<>();
		map.put("name", name);
		map.put("start", pageModel.getFirstLimitParam());
		map.put("pageSize", pageModel.getPageSize());
		
		return productDao.selectProductByName(map);
	}

	@Override
	public Notice findRecentNotice() {
		// TODO Auto-generated method stub
		return productDao.selectRecentNotice();
	}

	@Override
	public List<Product> findWeekHotProduct() {
		// TODO Auto-generated method stub
		return productDao.selectWeekHotProduct();
	}

	

}

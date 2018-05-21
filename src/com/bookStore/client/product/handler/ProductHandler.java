package com.bookStore.client.product.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookStore.client.product.service.IProductService;
import com.bookStore.commons.beans.Notice;
import com.bookStore.commons.beans.Product;
import com.bookStore.utils.PageModel;

@Controller
@RequestMapping("/client/products")
public class ProductHandler {

	@Autowired
	private IProductService productService;
	
	@RequestMapping("/findProductByCategory.do")
	public String findProductByCategory(Model model,@RequestParam(defaultValue="1")Integer pageIndex,String category){
		PageModel pageModel = new PageModel();
		pageModel.setPageIndex(pageIndex);
		
		int count = productService.findProductByCount(category);
		pageModel.setRecordCount(count);
		
		List<Product> products = productService.findProductByCategory(category,pageModel);
		//System.out.println(products);
		
		model.addAttribute("pageModel", pageModel);
		model.addAttribute("category", category);
		model.addAttribute("products", products);
		return "/client/product_list.jsp";
	
	}
	
	@RequestMapping("/findProductById.do")
	public String findProductById(String id,Model model){
		Product product = productService.findProductById(id);
		model.addAttribute("p", product);
		return "/client/info.jsp";
	}
	
	@RequestMapping("/findProductByName.do")
	public String findProductByName(String name,
			@RequestParam(defaultValue="1")Integer pageIndex,Model model){
		
		PageModel pageModel = new PageModel();
		pageModel.setPageIndex(pageIndex);
		
		int count = productService.findProductByNameCount(name);
		pageModel.setRecordCount(count);
		//System.out.println(count);
		List<Product> products = productService.findProductByName(name, pageModel);
		model.addAttribute("products", products);
		model.addAttribute("pageModel", pageModel);
	
		return "/client/product_search_list.jsp";
	}
	
	
	@RequestMapping("/showIndex.do")
	public String showIndex(Model model){
		
		Notice notice = productService.findRecentNotice();
		model.addAttribute("notice", notice);
		
		List<Product> products = productService.findWeekHotProduct();
		model.addAttribute("products", products);

		return "/client/index.jsp";
		
	}
	
}

package com.bookStore.admin.order.handler;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookStore.admin.order.service.IOrderService;
import com.bookStore.commons.beans.Order;
import com.bookStore.commons.beans.OrderItem;
import com.bookStore.commons.beans.Product;
import com.bookStore.commons.beans.User;
import com.bookStore.utils.PageModel;

@Controller
@RequestMapping("/admin/orders")
public class OrderHandler {

	@Autowired
	private IOrderService orderService;
	
	@RequestMapping("/findOrders.do")
	public String findOrders(Model model){
		List<Order> orders = orderService.findOrder();
		model.addAttribute("orders", orders);
		return "/admin/orders/list.jsp";
	}
	
	@RequestMapping("/findOrderByManyCondition.do")
	public String findOrderByManyCondition(Order order,Model model,@RequestParam(defaultValue="1")Integer pageIndex){
		PageModel pageModel = new PageModel();
		pageModel.setPageIndex(pageIndex);
		System.out.println(pageModel.getPageIndex());
		List<Order> orders = orderService.findOrderByManyCondition(order,pageModel);
		int count = orderService.findOrderByCount(order);
		pageModel.setRecordCount(count);
		
		model.addAttribute("order", order);
		model.addAttribute("orders", orders);
		model.addAttribute("pageModel", pageModel);
		return "/admin/orders/list.jsp";
	}
	
	@RequestMapping("/findOrderById.do")
	public String findOrderById(String id,Model model,HttpSession session){
		Order order = orderService.findOrderById(id);
		//System.out.println(order);
		User user = orderService.findUserById(id);
		//System.out.println(user);
		order.setUser(user);
		List<OrderItem> orderItems = orderService.findOrderItemById(id);
		//System.out.println(order);
		model.addAttribute("order", order);
		model.addAttribute("orderItem", orderItems);
		return "/admin/orders/view.jsp";
	}
	
	@RequestMapping("/delOrderById.do")
	public String delOrderById(String id){
		orderService.delOrderById(id);
		return "redirect:/admin/orders/findOrderByManyCondition.do";
	}
	
}

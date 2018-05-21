package com.bookStore.client.user.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bookStore.client.user.service.IUserService;
import com.bookStore.commons.beans.Order;
import com.bookStore.commons.beans.OrderItem;
import com.bookStore.commons.beans.User;
import com.bookStore.utils.ActiveCodeUtils;

@Controller
@RequestMapping("/client/user")
public class UserHandler {

	@Autowired
	private IUserService userService;

	@RequestMapping("/findEmail.do")
	@ResponseBody
	public String findEmail(String email) {
		User user = userService.findEmail(email);
		if (user != null) {
			return "EXIST";
		} else {
			return "OK";
		}
	}

	@RequestMapping("/register.do")
	public String register(HttpSession session,User user,String checkcode, HttpServletRequest request) {// 验证码检错weixie
		String checkcode_session = (String) session.getAttribute("checkcode_session");
		if(checkcode.equals(checkcode_session)){
			
			user.setActiveCode(ActiveCodeUtils.createActiveCode());
			int rows = userService.addUser(user, request);
			if (rows > 0) {
				return "/client/registersuccess.jsp";
			} else {
				return "/client/register.jsp";
			}
		}else{
			request.setAttribute("checkcode_error", "jiaoyanmacuowu,qingchongxinshuru!");
			return "/client/register.jsp";
		}
	}

	@RequestMapping("/activeUser.do")
	public String activeUser(String activeCode) {
		int rows = userService.activeUser(activeCode);
		if (rows > 0) {
			return "/index.jsp";
		} else {
			return "/client/register.jsp";
		}
	}

	@RequestMapping("/myAccount.do")
	public String myAccount(HttpSession session,HttpServletRequest request) throws UnsupportedEncodingException {
		User login_user = (User) session.getAttribute("login_user");
		//System.out.println(login_user);
		if (login_user == null) {
			//调用自动登录方法
			login_user = autologin(request);
			if(login_user !=null){//自动登录成功
				//System.out.println("自动登录执行");
				session.setAttribute("login_user", login_user);
				return "/client/myAccount.jsp";
			}else{
				return "/client/login.jsp";
			}
		} else {
			return "/client/myAccount.jsp";
		}
	}

	@RequestMapping("/login.do")
	public String login(User user, String remember, String autologin, Model model,HttpSession session,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
		User login_user = userService.findUserByUsernameAndPassword(user);
		if (login_user != null) {//用户存在
			if (login_user.getState() == 1) {//用户已激活
				session.setAttribute("login_user", login_user);
				//自动登录选中
				if("1".equals(autologin)){
					addCookie( autologin,login_user, request, response);
				}else if("1".equals(remember)){
					//调用保存用户名到COOKIEfangfa
					addCookie(autologin,login_user, request, response);
				}
				if (login_user.getRole().equals("普通用户")) {//用户是普通用户
					return "/client/index.jsp";
				} else {//用户是超级用户
					return "/admin/login/home.jsp";
				}
			} else {//用户未激活
				model.addAttribute("error", "用户未激活,请激活后使用!");
				return "/client/login.jsp";
			}
		} else {//用户名或密码错误
			model.addAttribute("error", "用户名或密码错误!");
			return "/client/login.jsp";
		}
	}
	
	@RequestMapping("/logout.do")
	public String logout(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		session.removeAttribute("login_user");
		Cookie cookie1 = new Cookie("bookstore_username", null);
		cookie1.setPath(request.getContextPath()+"/");
		cookie1.setMaxAge(0);
		Cookie cookie2 = new Cookie("bookstore_password", null);
		cookie2.setPath(request.getContextPath()+"/");
		cookie2.setMaxAge(0);
		response.addCookie(cookie1);
		response.addCookie(cookie2);
		
		return "/client/login.jsp";
	}
	@RequestMapping("/adminLogout.do")
	public String adminLogout(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		session.removeAttribute("login_user");
		Cookie cookie1 = new Cookie("bookstore_username", null);
		cookie1.setPath(request.getContextPath()+"/");
		cookie1.setMaxAge(0);
		Cookie cookie2 = new Cookie("bookstore_password", null);
		cookie2.setPath(request.getContextPath()+"/");
		cookie2.setMaxAge(0);
		response.addCookie(cookie1);
		response.addCookie(cookie2);
		
		return "/admin/login/login.jsp";
	}
	
	@RequestMapping("/modifyUser.do")
	public String modifyUser(User user,HttpSession session,Model model){
		//獲取登錄用戶信息
		User login_user = (User) session.getAttribute("login_user");
		//把ID赋值给user对象
		user.setId(login_user.getId());
		
		System.out.println(user);
		int rows = userService.modifyUser(user);
		if(rows>0){
			model.addAttribute("error", "用户信息修改成功,请重新登陆!");
			return "/client/login.jsp";
		}else{
			model.addAttribute("modify_fail", "用户信息修改失败!");
			return "/client/modifyuserinfo.jsp";
		}
		
	}
	
	
	@RequestMapping("/findOrderByUser.do")
	public String findOrderByUser(HttpSession session,Model model){
		User login_user = (User) session.getAttribute("login_user");
		List<Order> orders = userService.findOrderByUser(login_user.getId());
		model.addAttribute("orders", orders);
		return "/client/orderlist.jsp";
		
	}
	
	@RequestMapping("/findOrderById.do")
	public String findOrderById(String id,Model model){
		List<OrderItem> items = userService.findOrderItemById(id);
		model.addAttribute("items",items);
		return "/client/orderInfo.jsp";
	}
	
	@RequestMapping("/delOrderById.do")
	public String delOrderById(String id,String flag){
		
		if("1".equals(flag)){
			//payed,delet orders and items
			userService.removeOrderById(id);
		}else{
			//not payed
			userService.removeOrderByIdClient(id);
		}
		return "/client/user/findOrderByUser.do";
	}

	
	public void addCookie(String flag,User user,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		
		Cookie cookie1 = new Cookie("bookstore_username", URLEncoder.encode(user.getUsername(),"utf-8"));
		cookie1.setPath(request.getContextPath()+"/");
		cookie1.setMaxAge(60*60*24*3);
		response.addCookie(cookie1);
		if("1".equals(flag)){
			Cookie cookie2 = new Cookie("bookstore_password", URLEncoder.encode(user.getPassword(),"utf-8"));
			cookie2.setPath(request.getContextPath()+"/");
			cookie2.setMaxAge(60*60*24*3);
			response.addCookie(cookie2);
		}
		
		
		
	}
	
	//自动登录方法
	public User autologin(HttpServletRequest request) throws UnsupportedEncodingException{
		User user = new User();
		String username = null;
		String password = null;
		Cookie[] cookies = request.getCookies();
		//从cookie找用户名密码
		for(Cookie cookie : cookies){
			if("bookstore_username".equals(cookie.getName())){
				username = URLDecoder.decode(cookie.getValue(),"utf-8");
			}
			if("bookstore_password".equals(cookie.getName())){
				password = URLDecoder.decode(cookie.getValue(),"utf-8");
			}
		}
		//把用户名密码放到user对象
		user.setUsername(username);
		user.setPassword(password);
		//返回根据cookie用户名密码查找数据库结果
		return userService.findUserByUsernameAndPassword(user);
		
	}
	
	
}



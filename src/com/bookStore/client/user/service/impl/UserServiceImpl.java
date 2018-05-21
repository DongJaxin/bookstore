package com.bookStore.client.user.service.impl;

import java.security.GeneralSecurityException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookStore.client.user.dao.IUserDao;
import com.bookStore.client.user.service.IUserService;
import com.bookStore.commons.beans.Order;
import com.bookStore.commons.beans.OrderItem;
import com.bookStore.commons.beans.User;
import com.bookStore.utils.MailUtils;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;
	@Override
	public User findEmail(String email) {
		return userDao.selectEmail(email);
	}
	
	@Override
	public int addUser(User user,HttpServletRequest request) {
		String emailMsg = "欢迎注册网上邮箱,点击<a href='http://localhost:8080/"
						+request.getContextPath()+"/client/user/activeUser.do?activeCode="
						+user.getActiveCode()+"'>激活</a>请激活后使用!";
		try {
			try {
				MailUtils.sendMail(user.getEmail(), emailMsg);
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userDao.insertUser(user);
	}
	@Override
	public int activeUser(String activeCode) {
		// TODO Auto-generated method stub
		return userDao.updateUserByState(activeCode);
	}

	@Override
	public User findUserByUsernameAndPassword(User user) {
		// TODO Auto-generated method stub
		return userDao.selectUserByUsernameAndPassword(user);
	}

	@Override
	public int modifyUser(User user) {
		// TODO Auto-generated method stub
		return userDao.updateUser(user);
	}

	@Override
	public List<Order> findOrderByUser(Integer id) {
		// TODO Auto-generated method stub
		return userDao.selectOrderByUser( id);
	}

	@Override
	public List<OrderItem> findOrderItemById(String id) {
		// TODO Auto-generated method stub
		return userDao.selectOrderItemById( id);
	}

	@Override
	public void removeOrderById(String id) {
		// TODO Auto-generated method stub
		userDao.deleteOrderById(id);
		userDao.deleteOrderItemById(id);
	}

	@Override
	public void removeOrderByIdClient(String id) {
		// TODO Auto-generated method stub
		
		List<OrderItem> items = userDao.selectOrderItemById(id);
		for(OrderItem item : items){
			userDao.updateProductPnum(item);
		}
		userDao.deleteOrderById(id);
		userDao.deleteOrderItemById(id);
	}

}

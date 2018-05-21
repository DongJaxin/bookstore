package com.bookStore.client.cart.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.bookStore.client.cart.service.ICartService;
import com.bookStore.client.product.service.IProductService;
import com.bookStore.commons.beans.Order;
import com.bookStore.commons.beans.OrderItem;
import com.bookStore.commons.beans.Product;
import com.bookStore.commons.beans.User;
import com.bookStore.utils.AlipayConfig;
import com.bookStore.utils.IdUtils;

@Controller
@RequestMapping("/client/cart")
public class CartHandler {

	@Autowired
	private IProductService productService;
	
	@Autowired
	private ICartService cartService;
	
	@RequestMapping("/addCart.do")
	public String addCart(String id,HttpSession session){
		//根据ID查找放入购物车的商品
		Product product = productService.findProductById(id);
		//查看session域有无cart
		Map<Product,Integer> cart =(Map<Product, Integer>) session.getAttribute("cart");
		//如果session无cart
		if(cart == null){
			 cart = new HashMap<Product, Integer>();
		}
		//向购物车放入商品，如果商品已经在购物车，count是原来商品关键字的值
		//如果不在，值为null
		Integer count = cart.put(product, 1);
		//如果存在商品，则count值加1
		if(count !=null){
			cart.put(product, count+1);
		}
		
		session.setAttribute("cart", cart);
		return "/client/cart.jsp";
	}
	
	@RequestMapping("/changeCart.do")
	public String changeCart(String id,Integer count,HttpSession session){
		
		Product product = productService.findProductById(id);
		
		Map<Product,Integer> cart =(Map<Product, Integer>) session.getAttribute("cart");
		
		if(count == 0){
			cart.remove(product);
		}else{
			cart.put(product, count);			
		}
		return "/client/cart.jsp";
	}
	
	@RequestMapping("/createOrder.do")
	public String createOrder(Order order,HttpSession session,Model model) throws Exception{
		
		User login_user = (User) session.getAttribute("login_user");
		
		order.setId(IdUtils.getUUID());
		order.setUser(login_user);
		Map<Product,Integer> cart =(Map<Product, Integer>) session.getAttribute("cart");
		
		cartService.creatOrder(order,cart);
		
//		for(Product p:cart.keySet()){
//			OrderItem item = new OrderItem();
//			item.setOrder(order);
//			item.setProduct(p);
//			item.setBuynum(cart.get(p));
//			
//			cartService.addOrderItem(item);
//		}
//		
//		cartService.creatOrder(order);
		
		session.removeAttribute("cart");
		
		model.addAttribute("order", order);
		return "/client/confirm.jsp";
		
	}
	
	@RequestMapping("/pay.do")
	public void pay(String id,String money,String name,HttpServletResponse response) throws IOException, AlipayApiException{
		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
		
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = id;
		//付款金额，必填
		String total_amount = money;
		//订单名称，必填
		String subject = name;
		//商品描述，可空
		String body = "";
		
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
				+ "\"total_amount\":\""+ total_amount +"\"," 
				+ "\"subject\":\""+ subject +"\"," 
				+ "\"body\":\""+ body +"\"," 
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		
		//若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
		//alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
		//		+ "\"total_amount\":\""+ total_amount +"\"," 
		//		+ "\"subject\":\""+ subject +"\"," 
		//		+ "\"body\":\""+ body +"\"," 
		//		+ "\"timeout_express\":\"10m\"," 
		//		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		//请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节
		
		//请求
		String result = alipayClient.pageExecute(alipayRequest).getBody();
		
		//输出
		response.getWriter().println(result);
	}
	
	
	@RequestMapping("/paysuccess.do")
	public String paysuccess(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException{
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

		//——请在这里编写您的程序（以下代码仅作参考）——
		if(signVerified) {
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		
			//付款金额
			String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
			
			cartService.paySuccess(out_trade_no);
			
			return "/client/paysuccess.jsp";
			//out.println("trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount);
		}else {
			return null;
			//out.println("验签失败");
		}
	}
}

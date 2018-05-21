/**
 * 
 */
package com.bookStore.admin.notice.handler;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookStore.admin.notice.service.INoticeService;
import com.bookStore.commons.beans.Notice;
import com.bookStore.utils.PageModel;

@Controller
@RequestMapping("/admin/notices")
public class NoticeHandler {

	@Autowired
	private INoticeService noticeService;
	
	@RequestMapping("/ListNoticeServlet.do")
	public String ListNoticeServlet(Notice n,Model model){
		List<Notice> notice = noticeService.ListNoticeServlet();
		System.out.println(notice);
		model.addAttribute("notices", notice);
		return "/admin/notices/list.jsp";
	}
	
	@RequestMapping("/findByIdNoticeServlet.do")
	public String findByIdNoticeServlet(String id,Model model,HttpSession session){
		Notice notice = noticeService.findByIdNoticeServlet(id);
		session.setAttribute("notice", notice);
		model.addAttribute("n", notice);
		return "/admin/notices/edit.jsp";
	}
	
	@RequestMapping("/editNoticeServlet.do")
	public String editNoticeServlet(Notice notice,HttpSession session){
		Notice target = (Notice) session.getAttribute("notice");
		//System.out.println(target);
		notice.setN_id(target.getN_id());
		noticeService.editNoticeServlet(notice);
		return "/admin/notices/ListNoticeServlet.do";
	}
	
	@RequestMapping("/deleteNoticeServlet.do")
	public String deleteNoticeServlet(String id){
		noticeService.deleteNotice(id);
		return "/admin/notices/ListNoticeServlet.do";
	}
	
	@RequestMapping("/AddNotice.do")
	public String AddNotice(Notice notice){
		
		noticeService.addNotice(notice);
		return "/admin/notices/ListNoticeServlet.do";
	}
}

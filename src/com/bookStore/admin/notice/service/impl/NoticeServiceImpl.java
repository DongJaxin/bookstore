/**
 * 
 */
package com.bookStore.admin.notice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookStore.admin.notice.dao.INoticeDao;
import com.bookStore.admin.notice.service.INoticeService;
import com.bookStore.commons.beans.Notice;

@Service
public class NoticeServiceImpl implements INoticeService {

	@Autowired
	private INoticeDao noticeDao;
	
	@Override
	public List<Notice> ListNoticeServlet() {
		// TODO Auto-generated method stub
		return noticeDao.selectNotice();
	}

	@Override
	public Notice findByIdNoticeServlet(String id) {
		// TODO Auto-generated method stub
		return noticeDao.selectNoticeById(id);
	}

	@Override
	public void editNoticeServlet(Notice notice) {
		// TODO Auto-generated method stub
		noticeDao.updateNotice(notice);
	}

	@Override
	public void deleteNotice(String id) {
		// TODO Auto-generated method stub
		noticeDao.deleteNoticeById(id);
	}

	@Override
	public void addNotice(Notice notice) {
		// TODO Auto-generated method stub
		noticeDao.insertNotice(notice);
	}

	@Override
	public int findNoticeByCount(Notice n) {
		// TODO Auto-generated method stub
		return noticeDao.selectNoticeByCount(n);
	}



	

}

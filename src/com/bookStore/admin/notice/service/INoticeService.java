/**
 * 
 */
package com.bookStore.admin.notice.service;

import java.util.List;

import com.bookStore.commons.beans.Notice;

public interface INoticeService {

	List<Notice> ListNoticeServlet();

	Notice findByIdNoticeServlet(String id);

	void editNoticeServlet(Notice notice);

	void deleteNotice(String id);

	void addNotice(Notice notice);

	int findNoticeByCount(Notice n);

}

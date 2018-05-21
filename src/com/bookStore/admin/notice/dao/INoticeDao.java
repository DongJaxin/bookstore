/**
 * 
 */
package com.bookStore.admin.notice.dao;

import java.util.List;

import com.bookStore.commons.beans.Notice;


public interface INoticeDao {

	List<Notice> selectNotice();

	Notice selectNoticeById(String id);

	void updateNotice(Notice notice);

	void deleteNoticeById(String id);

	void insertNotice(Notice notice);

	int selectNoticeByCount(Notice n);

}

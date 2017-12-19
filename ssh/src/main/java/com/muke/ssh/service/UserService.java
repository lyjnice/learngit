package com.muke.ssh.service;

import org.springframework.transaction.annotation.Transactional;

import com.muke.ssh.dao.UserDao;
import com.muke.ssh.domin.User;

@Transactional
public class UserService {
	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void registEmail(User user) {
		 userDao.registEmail(user);
		
	}

}

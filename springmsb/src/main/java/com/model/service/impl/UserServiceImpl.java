package com.model.service.impl;

import com.model.dao.UserDao;
import com.model.service.UserService;

public class UserServiceImpl implements UserService{
	public UserDao userdao;
	
	public void setUserdao(UserDao userdao) {
		this.userdao = userdao;
	}

	@Override
	public void save() {
          userdao.save();	
	}

}

package com.muke.ssh.dao;

import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import com.muke.ssh.domin.User;

public class UserDao extends HibernateDaoSupport {

	public void registEmail(User user) {
      this.getHibernateTemplate().save(user);		
	}

}

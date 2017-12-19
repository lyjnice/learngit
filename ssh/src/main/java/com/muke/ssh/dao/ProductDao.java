package com.muke.ssh.dao;


import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import com.muke.ssh.domin.Product;
public class ProductDao extends HibernateDaoSupport{

	public void save(Product product) {
     System.out.println("dao中保存商品。。。。。。。。。。。"+product);
     this.getHibernateTemplate().save(product);
     int i = 4/0;
     product.setPname("999999999");
     this.getHibernateTemplate().update(product);
	}

}

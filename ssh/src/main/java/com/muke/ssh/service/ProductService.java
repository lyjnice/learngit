package com.muke.ssh.service;

import org.springframework.transaction.annotation.Transactional;

import com.muke.ssh.dao.ProductDao;
import com.muke.ssh.domin.Product;
@Transactional
public class ProductService {
	private ProductDao productDao;

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void save(Product product) {
        productDao.save(product);		
	}

}

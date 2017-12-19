package com.muke.ssh.action;


import com.muke.ssh.domin.Product;
import com.muke.ssh.service.ProductService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class ProductAction extends ActionSupport implements ModelDriven<Product> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ProductService productService;
	
	private Product product;
	
    public ProductAction() {
    	 
	}
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		System.out.println("set product:"+product);
		this.product = product;
	}
	public Product getModel() {
		return product;
	}
    
	public void save(){
		System.out.println(product.getPname());
		productService.save(product);
	}
}

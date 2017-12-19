package com.example.demo.domin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

@Entity
public class Gril {
	@Id
	@GeneratedValue
	private Integer id;
	@Min(value = 18,message ="不满18周岁")
	private Integer age;
    
	 
	public Gril() {
		 System.out.println("默认构造函数。。。。。。。。。。。。。。。。。。。。");
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}

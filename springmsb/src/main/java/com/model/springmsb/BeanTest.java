package com.model.springmsb;

import com.model.service.UserService;

public class BeanTest {
  public static void main(String[] args) throws Exception {
	 ClassPathXmlApplicationContext classObject = new ClassPathXmlApplicationContext();
	 UserService userservice = (UserService) classObject.getBean("userservice");
	 userservice.save();
	 
}
}

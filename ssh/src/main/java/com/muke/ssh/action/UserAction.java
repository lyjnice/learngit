package com.muke.ssh.action;

import javax.servlet.Servlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.muke.ssh.domin.User;
import com.muke.ssh.service.UserService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class UserAction extends ActionSupport implements ModelDriven<User> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService;
	private User user = new User();
	private String gotoUrl;

	public User getModel() {
		return user;
	}
   
   
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public void registEmail(User user){
		user.setState(0);
	    user.setUid(0);
	    userService.registEmail(user);
	}
	
	public void doLogin(User user){
		if("xiaoming".equals(user.getUsername()) && "123".equals(user.getPassword())){
			Cookie cookie = new Cookie("ssocookie", "cookie");
			cookie.setPath("/");
			HttpServletResponse  response  = ServletActionContext.getResponse();
			response.addCookie(cookie);
		}
	}
}

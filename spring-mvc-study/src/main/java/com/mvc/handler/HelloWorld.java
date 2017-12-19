package com.mvc.handler;

import javax.servlet.http.HttpServletRequest;

import org.omg.CORBA.Request;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("test")
public class HelloWorld {
	@RequestMapping(value = "/hello/{id}",params={"age!=10"},method=RequestMethod.GET)
	public String hello(@PathVariable("id") Integer myid) {
		System.out.println("get : "+myid);
		return "success";
	}
	@RequestMapping(value = "/hello2/{id}",params={"age!=10"},method=RequestMethod.PUT)
	public String hello2(@PathVariable("id") Integer myid) {
		System.out.println("put : "+myid);
		return "success";
	}
	@RequestMapping(value = "/hello3/{id}",params={"age!=10"})
	public String hello3(@PathVariable("id") Integer myid) {
		System.out.println("method------------------"+);
		System.out.println("moren : "+myid);
		return "success";
	}
}

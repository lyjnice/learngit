package com.example.demo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class HttpAspect {
	
	public HttpAspect() {
		System.out.println("Aspect*******************************");
	}
	
	@Before("execution (public * com.example.demo.controller.CrilController2.getGrilList(..) )")
    public void login(){
    	System.out.println("竟然拦截了000000====================================");
    }
}

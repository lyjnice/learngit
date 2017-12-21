package com.test.toolsInvocation.proxy;

import java.lang.reflect.Method;

public class Test implements com.test.toolsInvocation.proxy.Moveable {
	com.test.toolsInvocation.proxy.InvocationHandler h;

	public Test(InvocationHandler h) {
		this.h = h;
	}

	@Override
	public void move() {
		try {
			Method md = com.test.toolsInvocation.proxy.Moveable.class.getMethod("move");
			System.out.println("md====" + md);
			System.out.println("md2====" + "public abstract void com.test.toolsInvocation.proxy.Moveable.move()");
			h.invoke(this, md);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		Method md = com.test.toolsInvocation.proxy.Moveable.class.getMethod("move");
		System.out.println(md);
	}
}
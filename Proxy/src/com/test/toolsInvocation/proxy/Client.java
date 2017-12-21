package com.test.toolsInvocation.proxy;

public class Client {
	public static void main(String[] args) throws Exception {
		Tank t =  new Tank();
		InvocationHandler h = new TimeHandler(t);
		Moveable m = (Moveable) CompileProxy.newProxyInstance(Moveable.class,h);
		m.move();
	}
}

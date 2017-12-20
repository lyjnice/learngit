package com.test.tools.proxy;

public class Client {
	public static void main(String[] args) throws Exception {
		Moveable m = (Moveable) CompileProxy.newProxyInstance(Moveable.class);
		m.stop();
	}
}

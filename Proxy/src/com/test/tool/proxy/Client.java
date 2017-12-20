package com.test.tool.proxy;

public class Client {
	public static void main(String[] args) throws Exception {
		Moveable m = (Moveable) CompileProxy.newProxyInstance();
		m.move();
	}
}

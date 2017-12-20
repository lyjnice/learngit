package com.test.tools.proxy;

import java.lang.reflect.Method;

public class Test {
   public static void main(String[] args) {
	Method[] methods = Moveable.class.getMethods();
	for (Method method : methods) {
		System.out.println(method.getName());
	}
	System.out.println(Moveable.class.getName());
}
}

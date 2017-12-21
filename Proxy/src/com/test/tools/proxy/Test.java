package com.test.tools.proxy;

import java.lang.reflect.Method;

public class Test {
   public static void main(String[] args) throws NoSuchMethodException, SecurityException {
	Method[] methods = Moveable.class.getMethods();
	Method md = Moveable.class.getMethod("move");
	System.out.println(md);
	for (Method method : methods) {
		//System.out.println(method.getName());
	}
	//System.out.println(Moveable.class.getName());
}
}

package com.test.toolsInvocation.proxy;

import java.lang.reflect.Method;

public class TimeHandler implements InvocationHandler {
	private Object target;

	public TimeHandler(Object target) {
		this.target = target;
	}

	@Override
	public void invoke(Object o, Method m) {// o������������ m������
		Long start = System.currentTimeMillis();
		try {
			System.out.println("m:" + m);
			System.out.println("o:" + o);
			m.invoke(target);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Long end = System.currentTimeMillis();
		System.out.println("��ʱ�� " + (end - start));
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

}

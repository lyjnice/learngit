package com.test.toolsInvocation.proxy;

import java.lang.reflect.Method;

public class TimeHandler implements InvocationHandler {
	private Object target;

	public TimeHandler(Object target) {
		this.target = target;
	}

	@Override
	public void invoke(Object o, Method m) {// o代表处理器对象 m代表方法
		Long start = System.currentTimeMillis();
		try {
			System.out.println("m:" + m);
			System.out.println("o:" + o);
			m.invoke(target);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Long end = System.currentTimeMillis();
		System.out.println("耗时： " + (end - start));
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

}

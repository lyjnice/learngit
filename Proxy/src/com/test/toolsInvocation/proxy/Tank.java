package com.test.toolsInvocation.proxy;
import java.util.Random;

public class Tank implements Moveable {

	@Override
	public void move() {
		System.out.println("tank ������������������������");
		try {
			Thread.sleep(new Random().nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
 
}

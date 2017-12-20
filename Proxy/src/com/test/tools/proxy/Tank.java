package com.test.tools.proxy;
import java.util.Random;

public class Tank implements Moveable {

	@Override
	public void move() {
		try {
			Thread.sleep(new Random().nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		System.out.println("stop.................");
	}

}

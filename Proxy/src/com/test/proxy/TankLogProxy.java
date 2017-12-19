package com.test.proxy;

public class TankLogProxy implements Moveable {
	Moveable t;

	public TankLogProxy(Moveable t) {
		this.t = t;
	}

	@Override
	public void move() {
		System.out.println("Tank start................");
		t.move();
		System.out.println("Tank end .................");
	}

}

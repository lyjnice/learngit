package com.test.tool.proxy;

public class TankTimeProxy implements Moveable {
	Moveable t;

	public TankTimeProxy(Moveable t) {
		this.t = t;
	}

	@Override
	public void move() {
		System.out.println("Tank moving................");
	    Long start = System.currentTimeMillis();
		t.move();
		Long end = System.currentTimeMillis();
		System.out.println("ºÄÊ±£º "+(end - start));
	}

}

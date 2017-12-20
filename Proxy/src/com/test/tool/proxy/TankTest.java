package com.test.tool.proxy;

public class TankTest {
    public static void main(String[] args) {
    	Tank tank = new Tank();
    	TankTimeProxy ttp = new TankTimeProxy(tank);
    	Moveable t = ttp;
		t.move();
	}
}

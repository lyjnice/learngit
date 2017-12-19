package com.test.proxy;

public class TankTest {
    public static void main(String[] args) {
    	Tank tank = new Tank();
		/*TankTimeProxy ttp = new TankTimeProxy(tank);
		TankLogProxy tlp = new TankLogProxy(ttp);
		Moveable t = tlp;*/
    	
    	TankLogProxy tlp = new TankLogProxy(tank);
    	TankTimeProxy ttp = new TankTimeProxy(tlp);
    	Moveable t = ttp;
		t.move();
	}
}

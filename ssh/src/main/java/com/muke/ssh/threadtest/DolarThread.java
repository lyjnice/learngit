package com.muke.ssh.threadtest;

public class DolarThread extends Thread{

	@Override
	public void run() {
		 for (int i = 0; i < 1000; i++) {
				System.out.println(i+"$$$");
			}
	}

}

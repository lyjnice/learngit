package com.muke.ssh.threadtest;

public class ThreadTest {
      public static void main(String[] args) {
		/* SharpThread sharpThread = new SharpThread();
		 DolarThread dolarThread = new DolarThread();
		 sharpThread.start();
		 dolarThread.start();
		 System.out.println("主线程结束，主线程没有代码执行。。。");*/
    	  
    	  new Thread(new SharpRunable()).start();
    	  
    	  Runnable dolarRunable = new DolarRunnable();
    	  Thread thread = new Thread(dolarRunable);
    	  thread.run();
    	  System.out.println("主线程结束，主线程没有代码执行。。。");
	}
}

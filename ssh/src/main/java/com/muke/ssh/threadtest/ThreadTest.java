package com.muke.ssh.threadtest;

public class ThreadTest {
      public static void main(String[] args) {
		/* SharpThread sharpThread = new SharpThread();
		 DolarThread dolarThread = new DolarThread();
		 sharpThread.start();
		 dolarThread.start();
		 System.out.println("���߳̽��������߳�û�д���ִ�С�����");*/
    	  
    	  new Thread(new SharpRunable()).start();
    	  
    	  Runnable dolarRunable = new DolarRunnable();
    	  Thread thread = new Thread(dolarRunable);
    	  thread.run();
    	  System.out.println("���߳̽��������߳�û�д���ִ�С�����");
	}
}

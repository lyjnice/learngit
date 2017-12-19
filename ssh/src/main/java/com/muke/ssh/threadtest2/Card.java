package com.muke.ssh.threadtest2;

public class Card {
	private double balance;
	private Object lockA = new Object();
	private Object lockB = new Object();
	
	public Card(double balance) {
		super();
		this.balance = balance;
	}
	/**
	 * 存钱
	 * @param money 存入的钱数
	 * @throws InterruptedException 
	 */
	public void deposit(double count) throws InterruptedException {
		System.out.println("存钱线程，存入的金额=" + count);
		synchronized (lockA) {
			double now = balance + count;
			Thread.sleep(100); //操作用时0.1s
			synchronized (lockB) {
				this.balance = now;
			}
		}
		
		System.out.println("存钱线程，当前金额=" + this.balance);
	}
	/**
	 * 取钱
	 * @param money 取出的钱数
	 * @throws InterruptedException 
	 */
	public void withdraw(double count) throws InterruptedException {
		System.out.println("取钱线程，取出的金额=" + count);
		synchronized (lockA) {
			double now = balance - count;
			Thread.sleep(200); //操作耗时0.2秒
			synchronized (lockB) {     
				this.balance = now;
			}
		}
		
		System.out.println("取钱线程，当前金额=" + this.balance);
	}
	
	public double getBalance() {
		return balance;
	}
	
}

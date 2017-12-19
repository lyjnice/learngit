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
	 * ��Ǯ
	 * @param money �����Ǯ��
	 * @throws InterruptedException 
	 */
	public void deposit(double count) throws InterruptedException {
		System.out.println("��Ǯ�̣߳�����Ľ��=" + count);
		synchronized (lockA) {
			double now = balance + count;
			Thread.sleep(100); //������ʱ0.1s
			synchronized (lockB) {
				this.balance = now;
			}
		}
		
		System.out.println("��Ǯ�̣߳���ǰ���=" + this.balance);
	}
	/**
	 * ȡǮ
	 * @param money ȡ����Ǯ��
	 * @throws InterruptedException 
	 */
	public void withdraw(double count) throws InterruptedException {
		System.out.println("ȡǮ�̣߳�ȡ���Ľ��=" + count);
		synchronized (lockA) {
			double now = balance - count;
			Thread.sleep(200); //������ʱ0.2��
			synchronized (lockB) {     
				this.balance = now;
			}
		}
		
		System.out.println("ȡǮ�̣߳���ǰ���=" + this.balance);
	}
	
	public double getBalance() {
		return balance;
	}
	
}

package com.muke.ssh.threadtest2;

public class ThreadTest {

	public static void main(String[] args) throws InterruptedException {
		Card card = new Card(100);
		Thread deposit = new DepositThread(card);
		Thread withdraw = new WithdrawThread(card);
		deposit.start();
		withdraw.start();
		
		Thread.sleep(2000);
		System.out.println("cardµÄ×îÖÕÓà¶î£º" + card.getBalance());
	}

}

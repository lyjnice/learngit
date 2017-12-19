package com.muke.ssh.threadtest2;

public class DepositThread extends Thread {
	private Card card;
	private Thread withdraw;
	public DepositThread(Card card) {
		this.card = card;
	}

	public void setWithdraw(Thread withdraw) {
		this.withdraw = withdraw;
	}

	@Override
	public void run() {
		try {
			card.deposit(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

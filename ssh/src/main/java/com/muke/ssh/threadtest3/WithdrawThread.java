package com.muke.ssh.threadtest3;

public class WithdrawThread extends Thread {
	private Card card;
	private Thread depositThread;
	public WithdrawThread(Card card) {
		this.card = card;
	}
	
	public void setDepositThread(Thread depositThread) {
		this.depositThread = depositThread;
	}

	@Override
	public void run() {
		try {
			card.withdraw(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

package com.suanfa.test;

public class qsorts {
	int[] array = new int[] { 3, 2, 4, 5, 7, 8, 9, 1, 6, 0 };

	public static void main(String[] args) {
		qsorts q = new qsorts();
		q.qArray();
	}

	public void qArray() {
		for (int i = 0; i < array.length; i++) {
			int len = array.length;
			if (array[i] > array[len]) {
				int temp = array[len];
				array[len] = array[i];
				array[i] = temp;
			} else if (array[i] < array[len]) {

			}
		}
	}
}

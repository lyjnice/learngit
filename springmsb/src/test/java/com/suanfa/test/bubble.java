package com.suanfa.test;

public class bubble {
	int[] array = new int[] { 3, 2, 4, 5, 7, 8, 9, 1, 6, 0 };

	public static void main(String[] args) {
        bubble b = new bubble();
        b.bubbleArray();
	}

	public int[] bubbleArray() {
		int m = array.length;
		int n = m - 1;
		for (int j = 0; j < m-1; j++) {
			for (int i = 0; i < n; i++) {
				if (array[i] <= array[i + 1]) {
                    
				} else {
					int temp = array[i + 1];
					array[i + 1] = array[i];
					array[i] = temp;
				}
			}
			n = n - 1;
		}
		System.out.println(array);
		return array;

	}
}

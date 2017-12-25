package com.suanfa.test;

public class mySearch {
	static int[] array = new int[] { 7, 12, 18, 22, 40, 56, 66, 79, 84, 88, 94 };

	public static void main(String[] args) {
          int m = find(22);
          System.out.println(m);
	}

	public static int find(int m) {
		int len = array.length;
		int low = 0;
		int mid = (len - 1) / 2;
		int high = len - 1;
		while (low <= high) {
			mid = (low + high)/2;
			if (array[mid] < m) {
				low = mid + 1;
			} else if (array[mid] == m) {
				return mid;
			} else {
				high = mid - 1;
			}
		}
		return 0;
	}
}

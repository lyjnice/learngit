package com.suanfa.test;

public class insert {
	int[] array = new int[] { 3, 2, 4, 5, 7, 8, 9, 1, 6, 0 };

	public static void main(String[] args) {
         insert test = new insert();
         test.insertArray();
	}

	public void insertArray() {
		int len = array.length ;
		for (int i = 1; i < len ; i++) {
			 int temp = array[i];//要交换元素
			 int j  = 0;
			 for (j = i; j > 0 && temp < array[j-1]; j--) {
				 array[j] = array[j-1];
			}
			 array[j] = temp; 
		}
		System.out.println(array);
	}
}

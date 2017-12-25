package com.suanfa.test;

public class selects {
	int[] array = new int[] { 3, 2, 4, 5, 7, 8, 9, 1, 6, 0 };

	public static void main(String[] args) {
      selects select = new selects();
      select.selectsArray();
	}

	public void selectsArray() {
		int len = array.length - 1;
		for (int i = 0; i < array.length; i++) {
			int changeIndex = len;
			int temp = array[len];
			for (int j = len; j > i; j--) {
				if (temp > array[j - 1]) {
					temp = array[j - 1];//移动的数   最小数
					changeIndex = j-1;//移动的位置 
				}
			} 
			array[changeIndex] = array[i];
			array[i] = temp;
		}
		System.out.println(array);
	}
}

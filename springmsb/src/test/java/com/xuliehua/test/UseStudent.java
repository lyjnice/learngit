package com.xuliehua.test;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UseStudent {

	public static void main(String[] args) {
		Student st = new Student("Tom",'M',20,3.6);  
		  File file = new File("E:\\student.txt");  
		  try  
		  {  
		   file.createNewFile();  
		  }  
		  catch(IOException e)  
		  {  
		   e.printStackTrace();  
		  }  
		  try  
		  {  
		   //Student�������л����  
		   FileOutputStream fos = new FileOutputStream(file);  
		   ObjectOutputStream oos = new ObjectOutputStream(fos); 
		   oos.writeObject(st);  
		   oos.flush();  
		   oos.close();  
		   fos.close();  
		  
		   //Student�������л����  
		   FileInputStream fis = new FileInputStream(file);  
		   ObjectInputStream ois = new ObjectInputStream(fis);  
		   Student st1 = (Student) ois.readObject();  
		   System.out.println("name = " + st1.getName());  
		   System.out.println("sex = " + st1.getSex());  
		   System.out.println("year = " + st1.getYear());  
		   System.out.println("gpa = " + st1.getGpa());  
		   ois.close();  
		   fis.close();  
		  }  
		  catch(ClassNotFoundException e)  
		  {  
		   e.printStackTrace();  
		  }  
		  catch (IOException e)  
		  {  
		   e.printStackTrace();  
		  }               
		 }  

}

package com.xuliehua.test;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class B {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		/*try {
			Class c = Class.forName("A");
			Method m = c.getDeclaredMethod("say", null);// �õ�A������Ϊsay�ķ���
			m.invoke(c.newInstance());
			m = c.getDeclaredMethod("speek", String.class);// �õ�A������Ϊspeek�ķ���
			m.invoke(c.newInstance(), "Hi~");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		ClassLoader cl = B.class.getClassLoader();;  // ��λ��ClassLoader�ο�1  
		  
		/* Step 2. Load the class */  
		Class cls = cl.loadClass("A"); // ʹ�õ�һ���õ���ClassLoader������B  
		     
		/* Step 3. new instance */  
		Method m = cls.getDeclaredMethod("say", null);// �õ�A������Ϊsay�ķ���
		m.invoke(cls.newInstance());
	 
	}
}

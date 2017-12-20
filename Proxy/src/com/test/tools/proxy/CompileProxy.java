package com.test.tools.proxy;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class CompileProxy{
	public static Object newProxyInstance(Class Interf) throws Exception{
		   String rt = "\r\n";
		   Method[] methods = Interf.getMethods();
		   String methodsStr = "";
		   for (Method method : methods) {
			   methodsStr += "@Override" + rt +
				"public void "+method.getName()+ "() {" + rt +
				"  System.out.println(\"Tank moving................\");" + rt +
				"  Long start = System.currentTimeMillis();" + rt +
				"  t."+method.getName()+"();" + rt +
				"  Long end = System.currentTimeMillis();" + rt +
				"  System.out.println(\"��ʱ�� \"+(end - start));" + rt +
				"}";
		}
		   String str = "package com.test.tools.proxy;" + rt +

		   "public class CompileTimeProxy implements "+Interf.getName()+"{" + rt +
		   "Moveable t;" + rt +

			"public CompileTimeProxy("+Interf.getName()+" t) {" + rt +
			"  this.t = t;" + rt +
			"}" + rt +
           methodsStr +
		"}";
		   String fileName = System.getProperty("user.dir")+"/src/com/test/tools/proxy/CompileTimeProxy.java";
		   File file = new File(fileName);
		   FileWriter fw =  new FileWriter(file);
		   fw.write(str);
		   fw.flush();
		   fw.close();
		   
		   // ʹ��JavaCompiler ����java�ļ�  
	        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();  
	        StandardJavaFileManager fileManager = jc.getStandardFileManager(null, null, null);  
	        Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(fileName);  
	        CompilationTask cTask = jc.getTask(null, fileManager, null, null, null, fileObjects);  
	        cTask.call();  
	        fileManager.close();  
	        // ʹ��URLClassLoader����class���ڴ�    �����classLoaderҪ��֤class��classpath��
	        URL[] urls = new URL[] { new URL("file:/" + System.getProperty("user.dir") + "/src/") };  
	        URLClassLoader cLoader = new URLClassLoader(urls);  
	        Class<?> c = cLoader.loadClass("com.test.tools.proxy.CompileTimeProxy");  
	        cLoader.close();  
	        // ����class����ʵ��������ִ�з���  ֻ��load���캯���Ĳ���Ϊ�յ�class�ļ�
		       /* Object obj = c.newInstance();  
		        Method method = c.getMethod("disply");  
		        method.invoke(obj);  */
	        Constructor<?> ctr = c.getConstructor(Moveable.class);
	        return ctr.newInstance(new Tank()); //�������뱻��������
	}
 
}
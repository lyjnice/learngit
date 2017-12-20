package com.test.tool.proxy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class CompileProxy{
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		   String rt = "\r\n";
		   String str = "package com.test.tool.proxy;" + rt +

		   "public class CompileTimeProxy implements Moveable {" + rt +
		   "Moveable t;" + rt +

			"public CompileTimeProxy(Moveable t) {" + rt +
			"  this.t = t;" + rt +
			"}" + rt +

			"@Override" + rt +
			"public void move() {" + rt +
			"  System.out.println(\"Tank moving................\");" + rt +
			"  Long start = System.currentTimeMillis();" + rt +
			"  t.move();" + rt +
			"  Long end = System.currentTimeMillis();" + rt +
			"  System.out.println(\"耗时： \"+(end - start));" + rt +
			"}" + rt +
		"}";
		   String fileName = System.getProperty("user.dir")+"/src/com/test/tool/proxy/CompileTimeProxy.java";
		   File file = new File(fileName);
		   FileWriter fw =  new FileWriter(file);
		   fw.write(str);
		   fw.flush();
		   fw.close();
		   
		   // 使用JavaCompiler 编译java文件  
	        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();  
	        StandardJavaFileManager fileManager = jc.getStandardFileManager(null, null, null);  
	        Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(fileName);  
	        CompilationTask cTask = jc.getTask(null, fileManager, null, null, null, fileObjects);  
	        cTask.call();  
	        fileManager.close();  
	        // 使用URLClassLoader加载class到内存    如果用classLoader要保证class在classpath下
	        URL[] urls = new URL[] { new URL("file:/" + System.getProperty("user.dir") + "/src/") };  
	        URLClassLoader cLoader = new URLClassLoader(urls);  
	        Class<?> c = cLoader.loadClass("com.test.tool.proxy.CompileTimeProxy");  
	        cLoader.close();  
	        // 利用class创建实例，反射执行方法  只会load构造函数的参数为空的class文件
		       /* Object obj = c.newInstance();  
		        Method method = c.getMethod("disply");  
		        method.invoke(obj);  */
	        Constructor<?> ctr = c.getConstructor(Moveable.class);
	        Moveable m = (Moveable)ctr.newInstance(new Tank()); //参数传入被代理对象
		    m.move();
	}
  
   
   
   
}

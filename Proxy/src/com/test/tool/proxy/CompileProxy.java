package com.test.tool.proxy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CompileProxy{
	public static void main(String[] args) throws IOException {
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
			"  System.out.println(\"ºÄÊ±£º \"+(end - start));" + rt +
			"}" + rt +
		"}";
		   String fileName = System.getProperty("user.dir")+"/src/com/test/tool/proxy/CompileTimeProxy.java";
		   File file = new File(fileName);
		   FileWriter fw =  new FileWriter(file);
		   fw.write(str);
		   fw.flush();
		   fw.close();
	}
  
   
   
   
}

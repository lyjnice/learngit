import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import oracle.jrockit.jfr.events.DynamicValueDescriptor;
import sun.org.mozilla.javascript.internal.ast.ThrowStatement;

public class Test1 {
	public static int num = 10;

	public static Integer m() throws Exception {
		if (num <= 10) {
			 n();
		}
		 
		return num;
	}
	public static  Integer n(){
		return 1;
	}

	public static void main(String[] args) throws Exception {
		  /*InetAddress  addRess = InetAddress.getLocalHost();
		  System.out.println(addRess.getHostName());
		  System.out.println(addRess.getHostAddress());
		  m();*/
		Map map =new HashMap<>();
		map.put(0, 111);
		System.out.println(map.get(0));
}
}
package filtertest;

public class MainTest {
   public static void main(String[] args) {
	String msg = "cwq 阿凡达你分开件萨比敏感字眼的";
	Request request = new Request();
	request.setRequest(msg);
	Response response = new Response();
	response.setResponse("");
	FilterChain fc = new FilterChain();
	fc.addFilter(new HTMLFilter()).addFilter(new SesitiveFilter());
	fc.doFilter(request, response,fc);
	System.out.println(request.getRequest());
	System.out.println(response.getResponse());
}
}

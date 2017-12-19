package filtertest;

public class HTMLFilter implements Filter{

	@Override
	public void doFilter(Request request, Response response,FilterChain chain) {
          request.setRequest(request.getRequest().replace("cwq", "cwq替换")+"-------html处理 ");
          chain.doFilter(request, response, chain);
          response.setResponse(response.getResponse() + "-----------html处理response   ");
	}
    
}

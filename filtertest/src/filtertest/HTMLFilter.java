package filtertest;

public class HTMLFilter implements Filter{

	@Override
	public void doFilter(Request request, Response response,FilterChain chain) {
          request.setRequest(request.getRequest().replace("cwq", "cwq�滻")+"-------html���� ");
          chain.doFilter(request, response, chain);
          response.setResponse(response.getResponse() + "-----------html����response   ");
	}
    
}

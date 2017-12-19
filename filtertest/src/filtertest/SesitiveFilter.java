package filtertest;

public class SesitiveFilter implements Filter {

	@Override
	public void doFilter(Request request, Response response,FilterChain chain) {
		request.setRequest(request.getRequest().replace("敏感", "mingan替换")+"-------sesitive处理  ");
		chain.doFilter(request, response, chain);
		response.setResponse(response.getResponse() + "---------sesitive处理response ");
	}

}

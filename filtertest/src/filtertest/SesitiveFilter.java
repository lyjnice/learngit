package filtertest;

public class SesitiveFilter implements Filter {

	@Override
	public void doFilter(Request request, Response response,FilterChain chain) {
		request.setRequest(request.getRequest().replace("����", "mingan�滻")+"-------sesitive����  ");
		chain.doFilter(request, response, chain);
		response.setResponse(response.getResponse() + "---------sesitive����response ");
	}

}

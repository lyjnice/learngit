package filtertest;

import java.util.ArrayList;
import java.util.List;

public class FilterChain implements Filter {
	List<Filter> filterlist = new ArrayList<>();
	int index = 0;

	public FilterChain addFilter(Filter f) {
		this.filterlist.add(f);
		return this;
	}

	@Override
	public void doFilter(Request request, Response response, FilterChain chain) {
		if (index == filterlist.size())
			return;
		Filter filter = filterlist.get(index);
		index++;
		filter.doFilter(request, response, chain);
		/*
		 * for (Filter filter : filterlist) { filter.doFilter(request,
		 * response,chain); }
		 */
	}

}

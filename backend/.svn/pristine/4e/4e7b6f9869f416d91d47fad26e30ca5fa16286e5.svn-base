package com.yanxiu.util.interceptor;

/**
 * Created by mashiwei on 2016/5/10.
 */
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class HttpInterceptor implements HandlerInterceptor {

    private static Logger TIME_LOG = LoggerFactory.getLogger("TIME_LOG");

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object obj, Exception e)
            throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object obj, ModelAndView mv) throws Exception {
        long timeBegin = Long.valueOf(request.getAttribute("timeBegin").toString());
        long timeEnd = System.nanoTime();
        String uri = request.getRequestURI();
        @SuppressWarnings("unchecked")
        Enumeration<String> paramEnumeration = request.getParameterNames();
        StringBuffer paramSB = new StringBuffer();
        while (paramEnumeration.hasMoreElements()) {
            String pName = paramEnumeration.nextElement();
            paramSB.append(pName + "=" + request.getParameter(pName) + ",");
        }
        TIME_LOG.info("URI:" + uri + ";timeCost:" + ((timeEnd - timeBegin) / 1000000) + "ms;" + "params:" + paramSB.toString());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object obj) throws Exception {
        long timeBegin = System.nanoTime();
        request.setAttribute("timeBegin", timeBegin);
        return true;
    }
}


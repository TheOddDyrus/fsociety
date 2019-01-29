package com.thomax.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomFilter extends ZuulFilter {

    /**
     * filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
     *      pre：路由之前
     *      routing：路由之时
     *      post： 路由之后
     *      error：发送错误调用
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * filterOrder：过滤的顺序
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * shouldFilter：这里可以写逻辑判断，是否要过滤
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * run：过滤器的具体逻辑
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        String t = ctx.getRequest().getParameter("t");
        if(!StringUtils.isEmpty(t) && t.length() <= 2) {
            ctx.setSendZuulResponse(false);
            ctx.getResponse().setStatus(401);

            try {
                ctx.getResponse().getWriter().write("t's length must greater than 2");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}

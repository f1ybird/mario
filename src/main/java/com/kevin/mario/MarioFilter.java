package com.kevin.mario;

import com.kevin.mario.route.Route;
import com.kevin.mario.route.RouteMatcher;
import com.kevin.mario.route.Routers;
import com.kevin.mario.util.PathUtil;
import com.kevin.mario.util.ReflectUtil;
import com.kevin.mario.wrapper.Request;
import com.kevin.mario.wrapper.Response;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * describe  : MVC核心处理器
 * creat_user: kevin
 * creat_time: 2018/11/4 13:01
 **/
public class MarioFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(MarioFilter.class.getName());
    private RouteMatcher routeMatcher = new RouteMatcher(new ArrayList<>());
    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("MarioFilter init");
        Mario mario = Mario.me();
        if (!mario.isInit()) {
            String className = filterConfig.getInitParameter("bootstrap");
            Bootstrap bootstrap = this.getBootstrap(className);
            bootstrap.init(mario);
            Routers routers = mario.getRouters();
            if (null != routers) {
                routeMatcher.setRoutes(routers.getRoutes());
            }
            servletContext = filterConfig.getServletContext();
            mario.setInit(true);
        }
    }

    private Bootstrap getBootstrap(String className) {
        if (null != className) {
            try {
                Class<?> clazz = Class.forName(className);
                return (Bootstrap) clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("init bootstrap class error!");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOGGER.info("MarioFilter doFilter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //设置编码
        request.setCharacterEncoding(Const.DEFAULT_CHAR_SET);
        response.setCharacterEncoding(Const.DEFAULT_CHAR_SET);

        //获取uri
        String uri = PathUtil.getRelativePath(request);
        LOGGER.info("Request URI:" + uri);

        //查找路由
        Route route = routeMatcher.findRoute(uri);
        if (null != route) {
            // 实际执行方法
            handle(request, response, route);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Route route) {
        //初始化上下文
        Request request = new Request(httpServletRequest);
        Response response = new Response(httpServletResponse);
        MarioContext.initContext(servletContext, request, response);

        Object controller = route.getController();
        Method actionMethod = route.getAction();
        // 执行route方法
        executeMethod(controller, actionMethod, request, response);
    }

    /**
     * 执行路由方法
     *
     * @param controller 路由所在的控制器
     * @param method     方法名称
     * @param request
     * @param response
     * @return 执行路由方法后的结果
     */
    private Object executeMethod(Object controller, Method method, Request request, Response response) {
        int len = method.getParameterTypes().length;
        method.setAccessible(true);
        if (len > 0) {
            Object[] args = getArgs(request, response, method.getParameterTypes());
            return ReflectUtil.invokeMehod(controller, method, args);
        } else {
            return ReflectUtil.invokeMehod(controller, method);
        }
    }

    /**
     * 获取方法内的参数
     *
     * @param request
     * @param response
     * @param params
     * @return 参数数组
     */
    private Object[] getArgs(Request request, Response response, Class<?>[] params) {
        int length = params.length;
        Object[] args = new Object[length];
        for (int i = 0; i < args.length; i++) {
            Class<?> paramClazz = params[i];
            if (paramClazz.getName().equals(request.getClass().getName())) {
                args[i] = request;
            }
            if (paramClazz.getName().equals(response.getClass().getName())) {
                args[i] = response;
            }
        }
        return args;
    }

    @Override
    public void destroy() {
        LOGGER.info("MarioFilter destroy");
    }
}

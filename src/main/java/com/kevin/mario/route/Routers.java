package com.kevin.mario.route;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * describe  : 路由管理器管理所有路由
 * creat_user: kevin
 * creat_time: 2018/10/30 23:48
 **/
public class Routers {

    private static final Logger LOGGER = Logger.getLogger(Routers.class.getName());

    private List<Route> routes = new ArrayList<>();

    public Routers() {
    }

    public void addRoute(List<Route> routes){
        routes.addAll(routes);
    }

    public void addRoute(Route route){
        routes.add(route);
    }

    public void addRoute(String path, Method action, Object controller){
        Route route = new Route();
        route.setPath(path);
        route.setAction(action);
        route.setController(controller);
        routes.add(route);
        LOGGER.info("add route [" + path + "]");
    }

    public void removeRoute(Route route){
        routes.remove(route);
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
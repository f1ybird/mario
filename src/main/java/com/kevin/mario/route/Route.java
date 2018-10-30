package com.kevin.mario.route;

import java.lang.reflect.Method;

/**
 * describe  : 路由
 * creat_user: kevin
 * creat_time: 2018/10/30 23:25
 **/
public class Route {

    /**
     * 路由path
     */
    private String path;

    /**
     * 执行路由方法
     */
    private Method action;

    /**
     * 路由所在的控制器
     */
    private Object controller;

    public Route() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Method getAction() {
        return action;
    }

    public void setAction(Method action) {
        this.action = action;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }
}

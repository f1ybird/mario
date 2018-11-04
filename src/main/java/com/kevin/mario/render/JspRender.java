package com.kevin.mario.render;

import com.kevin.mario.Const;
import com.kevin.mario.Mario;
import com.kevin.mario.MarioContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * JSP渲染实现
 *
 * @author biezhi
 */
public class JspRender implements Render {

    @Override
    public void render(String view, Writer writer) {

        String viewPath = this.getViewPath(view);

        HttpServletRequest servletRequest = MarioContext.me().getRequest().getRaw();
        HttpServletResponse servletResponse = MarioContext.me().getResponse().getRaw();
        try {
            servletRequest.getRequestDispatcher(viewPath).forward(servletRequest, servletResponse);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getViewPath(String view) {
        Mario mario = Mario.me();
        String viewPrfix = mario.getConf(Const.VIEW_PREFIX_FIELD);
        String viewSuffix = mario.getConf(Const.VIEW_SUFFIX_FIELD);

        if (null == viewSuffix || viewSuffix.equals("")) {
            viewSuffix = Const.VIEW_SUFFIX;
        }
        if (null == viewPrfix || viewPrfix.equals("")) {
            viewPrfix = Const.VIEW_PREFIX;
        }
        String viewPath = viewPrfix + "/" + view;
        if (!view.endsWith(viewSuffix)) {
            viewPath += viewSuffix;
        }
        return viewPath.replaceAll("[/]+", "/");
    }


}

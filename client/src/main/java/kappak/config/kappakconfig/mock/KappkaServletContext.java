package kappak.config.kappakconfig.mock;

import org.springframework.mock.web.MockServletContext;

import javax.servlet.*;

/**
 * @author ：youngsapling
 * @date ：Created in 2020/3/9 16:29
 * @modifyTime :
 * @description :
 */
public class KappkaServletContext extends MockServletContext {

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter){
        return null;
    }
}
package lk.GamerShop.Filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class CustomerAccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req =(HttpServletRequest) servletRequest;
        HttpServletResponse res =(HttpServletResponse) servletResponse;

        HttpSession session = req.getSession(false);
        if(session!=null && session.getAttribute("user")!=null){
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            res.sendError(403);
        }

    }
}

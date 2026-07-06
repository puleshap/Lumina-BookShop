package lk.GamerShop.Filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AdminAccessFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp =(HttpServletResponse) servletResponse;


        HttpSession httpSession = req.getSession(false);
        if (httpSession != null && httpSession.getAttribute("admin") != null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            resp.sendError(403);
        }
    }
}

package lk.GamerShop.Filters;

import jakarta.annotation.Priority;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lk.GamerShop.Annotations.IsAdmin;
import lk.GamerShop.Annotations.IsCustomer;

import java.io.IOException;


@Provider
@Priority(Priorities.AUTHENTICATION)
@IsCustomer
public class CustomerAuthFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest req;


    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        HttpSession session = req.getSession(false);
        System.out.println(req.getContextPath());

        if (session == null || session.getAttribute("user") == null) {
            containerRequestContext.abortWith(Response.status(403).build());
        }

    }
}

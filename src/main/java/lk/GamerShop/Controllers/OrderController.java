package lk.GamerShop.Controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.Annotations.IsCustomer;
import lk.GamerShop.service.OrderService;


@IsCustomer
@Path("/Orders")
public class OrderController {

    @Path("/ordersummary")
    @GET
    public Response Summary(@QueryParam("id") int id) {
        String response = new OrderService().ordersummary(id);
        return Response.ok().entity(response).build();
    }

    @Path("/OrderById")
    @GET
    public Response getByID(@QueryParam("id") int id) {
        String response = new OrderService().getOrderById(id);
        return Response.ok().entity(response).build();
    }

    @Path("/OrderList")
    @GET
    public Response getList(@Context HttpServletRequest request) {
        String response = new OrderService().getOrderList(request);
        return Response.ok().entity(response).build();
    }

}

package lk.GamerShop.Controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.service.CartService;

@Path("/cart")
public class CartController {

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCart(String json, @Context HttpServletRequest request) {


        String response = new CartService().AddCart(json, request);

        return  Response.ok().entity(response).build();
    }



    @Path("")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCart(@Context HttpServletRequest request) {

        String response = new CartService().getCart(request);
        return Response.ok().entity(response).build();
    }



    @Path("/remove")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeCart(String json ,@Context HttpServletRequest request) {
        String response = new CartService().removeFromCart(json,request);

        return   Response.ok().entity(response).build();
    }


    @Path("/clear")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearCart(@Context HttpServletRequest request) {
        String response = new CartService().clearCart(request);

        return  Response.ok().entity(response).build();

    }

}

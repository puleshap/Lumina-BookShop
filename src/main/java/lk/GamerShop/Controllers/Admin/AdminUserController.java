package lk.GamerShop.Controllers.Admin;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.Annotations.IsAdmin;
import lk.GamerShop.service.Admin.AdminAuthorService;
import lk.GamerShop.service.Admin.AdminService;
import lk.GamerShop.service.Admin.AdminUserService;


@IsAdmin
@Path("/admin")
public class AdminUserController {


    @Path("/getUsers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@QueryParam("page") int page) {
        String response = new AdminUserService().getUsers(page);
        return Response.ok().entity(response).build();

    }

    @Path("/searchusers")
    @GET
    public Response searchusers(@QueryParam("search") String search) {
        String response = new AdminUserService().searchUsers(search);
        return Response.ok().entity(response).build();

    }


    @Path("/changeStatus")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeStatus(@QueryParam("id") int id) {
        String response = new AdminUserService().changeStatus(id);
        return Response.ok().entity(response).build();

    }
}

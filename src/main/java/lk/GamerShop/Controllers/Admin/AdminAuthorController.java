package lk.GamerShop.Controllers.Admin;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.Annotations.IsAdmin;
import lk.GamerShop.service.Admin.AdminAuthorService;
import lk.GamerShop.service.Admin.AdminService;


@IsAdmin
@Path("/admin/authors")
public class AdminAuthorController {




    @Path("/load")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@QueryParam("page") int page) {
        String response = new AdminAuthorService().getAuthors(page);
        return Response.ok().entity(response).build();

    }

    @Path("/searchAuthors")
    @GET
    public Response searchAuthors(@QueryParam("search") String search) {
        String response = new AdminAuthorService().searchAuthors(search);
        return Response.ok().entity(response).build();

    }
    @Path("/addAuthor")
    @POST
    public Response AddAuthor(String json) {
        String response = new AdminAuthorService().addAuthor(json);
        return Response.ok().entity(response).build();

    }


    @Path("/changeStatus")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeStatus(@QueryParam("id") int id) {
        String response = new AdminAuthorService().changeStatus(id);
        return Response.ok().entity(response).build();

    }
}

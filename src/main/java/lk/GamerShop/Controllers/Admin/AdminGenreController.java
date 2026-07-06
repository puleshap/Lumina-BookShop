package lk.GamerShop.Controllers.Admin;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.Annotations.IsAdmin;
import lk.GamerShop.service.Admin.AdminAuthorService;
import lk.GamerShop.service.Admin.AdminGenreService;

@IsAdmin
@Path("/admin/genre")
public class AdminGenreController {



    @Path("/load")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenres(@QueryParam("page") int page) {
        String response = new AdminGenreService().getGenres(page);
        return Response.ok().entity(response).build();

    }

    @Path("/searchgenre")
    @GET
    public Response searchAuthors(@QueryParam("search") String search) {
        String response = new AdminAuthorService().searchAuthors(search);
        return Response.ok().entity(response).build();

    }
    @Path("/addGenre")
    @POST
    public Response AddGenre(String json) {
        String response = new AdminGenreService().AddGenre(json);
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

package lk.GamerShop.Controllers;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.service.BasicService;

@Path("/basic")
public class BasicController {


    @Path("/categories")
    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public Response getcategories(){
        String categories = new BasicService().getcategories();
return Response.ok().entity(categories).build();
    }


    @Path("/genres")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getgenres(){
        String genres = new BasicService().getgenres();

        return Response.ok().entity(genres).build();
    }

    @Path("/authors")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getauthors(){
        String genres = new BasicService().getauthors();

        return Response.ok().entity(genres).build();
    }

    @Path("/statuses")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getstatuses(){
        String statuses = new BasicService().getstatus();

        return Response.ok().entity(statuses).build();
    }

    @Path("/admin/statuses")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response adminbookstatus(){
        String statuses = new BasicService().adminbookstatus();

        return Response.ok().entity(statuses).build();
    }
    @Path("/admin/statuss")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response adminorderstatus(){
        String statuses = new BasicService().adminorderstatus();

        return Response.ok().entity(statuses).build();
    }

}

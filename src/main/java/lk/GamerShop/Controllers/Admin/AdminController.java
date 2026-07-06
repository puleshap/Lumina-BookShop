package lk.GamerShop.Controllers.Admin;


import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.DTO.AdminDTO;
import lk.GamerShop.service.Admin.AdminService;



@Path("/admin")
public class AdminController {

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String json, @Context HttpServletRequest request) {
        String response = new AdminService().AdminLogin(json, request);

        return Response.ok().entity(response).build();

    }


}

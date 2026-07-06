package lk.GamerShop.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.Annotations.IsCustomer;
import lk.GamerShop.DTO.UserDTO;
import lk.GamerShop.Entities.User;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.service.UserService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.InputStream;


@Path("/users")
public class UserController {

    @Path("/register")
@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(String data) {
        UserDTO dto = AppUtil.GSON.fromJson(data,UserDTO.class);
        String results = new UserService().registerUser(dto);

        return Response.ok().entity(results).build();
    }

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(String data, @Context HttpServletRequest request) {
        UserDTO dto = AppUtil.GSON.fromJson(data,UserDTO.class);
        String service = new UserService().loginUser(dto,request);
        return Response.ok().entity(service).build();

    }

    @Path("/forgot")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response forgotpassword(String data) {
        UserDTO dto = AppUtil.GSON.fromJson(data,UserDTO.class);

        String service = new UserService().ForgotPassword(dto);
        return Response.ok().entity(service).build();

    }

    @Path("/changepassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changepassword(String data) {
        UserDTO dto = AppUtil.GSON.fromJson(data,UserDTO.class);

        String service = new UserService().changepassword(dto);
        return Response.ok().entity(service).build();

    }

    @IsCustomer
    @Path("")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@Context HttpServletRequest request) {
         String results = new UserService().getUser(request);

        return Response.ok().entity(results).build();
    }

    @IsCustomer
    @Path("/update")
    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateuser(@Context HttpServletRequest request, @FormDataParam("firstName") String firstName,
                               @FormDataParam("lastName") String lastName,
                               @FormDataParam("email") String email,
                               @FormDataParam("mobile") String mobile,
                               @FormDataParam("sinceAt") String sinceAt,
                               @FormDataParam("image") InputStream imageInputStream,
                               @FormDataParam("image") FormDataContentDisposition imageDetails) {

        UserDTO dto = new UserDTO();
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setEmail(email);
        dto.setMobile(mobile);
        dto.setSinceAt(sinceAt);
        String results = new UserService().updateUser(request, dto,imageInputStream, imageDetails);

        return Response.ok().entity(results).build();
    }

    @IsCustomer
    @Path("/logout")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@Context HttpServletRequest request) {

        String results = new UserService().Userlogout(request);

        return Response.ok().entity(results).build();
    }


}

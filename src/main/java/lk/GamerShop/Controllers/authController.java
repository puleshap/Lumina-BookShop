package lk.GamerShop.Controllers;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.DTO.UserDTO;
import lk.GamerShop.Entities.User;
import lk.GamerShop.Utils.AppUtil;

@Path("/auth")
public class authController {

    @GET
    @Path("/session")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkSession(@Context HttpServletRequest request) {
        JsonObject responseObject = new JsonObject();
        boolean isLoggedIn = false;

        try {
            HttpSession session = request.getSession(false);

            if (session != null && session.getAttribute("user") != null) {
                isLoggedIn = true;
                 User user = (User) session.getAttribute("user");
                 UserDTO userDTO = new UserDTO();
                 userDTO.setFirstName(user.getFirstName());
                 userDTO.setLastName(user.getLastName());
                 responseObject.addProperty("name", userDTO.getFirstName() + " " + userDTO.getLastName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseObject.addProperty("isLoggedIn", isLoggedIn);

        // Serialize the JsonObject to a String using GSON so Jersey can write it effortlessly
        return Response.ok().entity(AppUtil.GSON.toJson(responseObject)).build();
    }
}

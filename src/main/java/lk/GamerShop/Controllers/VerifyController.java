package lk.GamerShop.Controllers;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.DTO.UserDTO;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.service.UserService;


@Path("/verify-accounts")
public class VerifyController {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyAccounts(String data) {
        UserDTO dto = AppUtil.GSON.fromJson(data,UserDTO.class);
        String responseJson =new UserService().verifyaccount(dto);
        return Response.ok().entity(responseJson).build();

    }
}

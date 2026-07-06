package lk.GamerShop.Controllers;


import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.DTO.CheckoutDTO;
import lk.GamerShop.Entities.Order;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.Utils.PayHereUtil;
import lk.GamerShop.service.CheckoutService;

@Path("/checkout")
public class CheckOutController {


    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkoutprocess(@Context HttpServletRequest request){
        String checkoutService = new CheckoutService().fulfilprocess(request);
        return Response.ok().entity(checkoutService).build();
    }

    @POST
    @Path("/notify")
    public Response notify(@Context HttpServletRequest request){
       System.out.println("notify is happening");
        System.out.println(request.toString());
        return Response.ok().build();
    }

//    @Path("/checkout")
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response checkout(String json) {
//
//        CheckoutDTO dto = AppUtil.GSON.fromJson(json, CheckoutDTO.class);
//
//        Order order = orderService.createPendingOrder(dto);
//
//        String hash = PayHereUtil.generateHash(
//                String.valueOf(order.getId()),
//                order.getTotal()
//        );
//
//        JsonObject payment = new JsonObject();
//        payment.addProperty("merchant_id", PayHereUtil.getMerchantId());
//        payment.addProperty("order_id", order.getId());
//        payment.addProperty("amount", order.getTotal());
//        payment.addProperty("currency", "LKR");
//        payment.addProperty("hash", hash);
//        payment.addProperty("notify_url", publicUrl + "/api/payment/notify");
//
//        return Response.ok(payment.toString()).build();
//    }

}

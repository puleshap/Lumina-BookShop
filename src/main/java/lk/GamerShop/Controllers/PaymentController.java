package lk.GamerShop.Controllers;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.Annotations.IsCustomer;
import lk.GamerShop.Utils.Env;
import lk.GamerShop.Utils.PayHereUtil;
import lk.GamerShop.service.CheckoutService;
import lk.GamerShop.service.OrderService;

import java.net.URI;

@IsCustomer
@Path("/payments")
public class PaymentController {
    @Path("/return")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response paymentSuccess(@QueryParam("orderId") String orderId) {
        System.out.println("Order ID: " + orderId);
        return Response.seeOther(URI.create(Env.get("app.url") + "/invoice.html?orderId=" + orderId)).build();
    }

    @Path("/cancel")
    @GET
    public Response paymentCancel() {
        System.out.println("Payment canceled");
        return Response.ok().build();
    }

    @Path("/notify")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response paymentNotify(MultivaluedMap<String, String> form) {
        String orderId = form.getFirst("order_id");
        String statusCode = form.getFirst("status_code");

        if (!PayHereUtil.validateNotify(form)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("INVALID SIGNATURE").build();
        }


        if (Integer.parseInt(statusCode) == PayHereUtil.PAYMENT_SUCCESS) {
            CheckoutService service = new CheckoutService();
            service.handleNotify(form);

            return Response.ok().build();
        }
        return null;
    }
}
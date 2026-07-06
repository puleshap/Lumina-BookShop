package lk.GamerShop.Controllers.Admin;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.Annotations.IsAdmin;
import lk.GamerShop.service.Admin.AdminDashboardService;
import lk.GamerShop.service.Admin.AdminService;


@IsAdmin
@Path("/admin/dashboard")
public class AdminDashboardController {

    @GET
    @Path("/summary")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDashboardSummary(@QueryParam("filter") @DefaultValue("total") String filter,
                                        @Context HttpServletRequest request) {

        // Ensure standard fallback parameters are assigned safely
        if (filter == null || filter.trim().isEmpty()) {
            filter = "total";
        }

        String responseJson = new AdminDashboardService().fetchSummaryMetrics(filter.toLowerCase().trim());
        return Response.ok(responseJson).build();
    }


}

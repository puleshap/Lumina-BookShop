package lk.GamerShop.Controllers.Admin;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.Annotations.IsAdmin;
import lk.GamerShop.DTO.BookDTO;
import lk.GamerShop.service.Admin.AdminAuthorService;
import lk.GamerShop.service.Admin.AdminBookService;
import lk.GamerShop.service.Admin.AdminOrderService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.InputStream;



@IsAdmin
@Path("/admin/orders")
public class AdminOrderController {



    @Path("/load")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@QueryParam("page") int page) {
        String response = new AdminOrderService().getOrders(page);
        return Response.ok().entity(response).build();

    }

    @Path("/searchOrders")
    @GET
    public Response searchAuthors(@QueryParam("search") String search) {
        String response = new AdminOrderService().searchOrders(search);
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


    @Path("/add")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBook(@Context HttpServletRequest request,
                            @FormDataParam("title") String title,
                            @FormDataParam("authorId") int authorId,
                            @FormDataParam("genreId") int genreId,
                            @FormDataParam("price") Double price,
                            @FormDataParam("discountPrice") Double discountPrice,
                            @FormDataParam("description") String description,
                            @FormDataParam("coverImage") InputStream coverInputStream,
                            @FormDataParam("coverImage") FormDataContentDisposition coverDetails,
                            @FormDataParam("ebookFile") InputStream ebookInputStream,
                            @FormDataParam("ebookFile") FormDataContentDisposition ebookDetails) {

        BookDTO dto = new BookDTO();
        dto.setTitle(title);
        dto.setAuthorId(authorId);
        dto.setGenreId(genreId);
        dto.setPrice(price);
        dto.setDiscountPrice(discountPrice);
        dto.setDescription(description);

        String result = new AdminBookService().saveBook(request, dto,
                coverInputStream, coverDetails,
                ebookInputStream, ebookDetails);

        return Response.ok().entity(result).build();
    }


    @Path("/update")
    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@Context HttpServletRequest request,
                               @FormDataParam("id") int id,
                               @FormDataParam("title") String title,
                               @FormDataParam("authorId") int authorId,
                               @FormDataParam("genreId") int genreId,
                               @FormDataParam("statusId") int statusId,

                               @FormDataParam("price") Double price,
                               @FormDataParam("discountPrice") Double discountPrice,
                               @FormDataParam("description") String description,
                               @FormDataParam("coverImage") InputStream coverInputStream,
                               @FormDataParam("coverImage") FormDataContentDisposition coverDetails,
                               @FormDataParam("ebookFile") InputStream ebookInputStream,
                               @FormDataParam("ebookFile") FormDataContentDisposition ebookDetails) {

        System.out.println(statusId);
        BookDTO dto = new BookDTO();
        dto.setId(id); // Added ID field to your existing DTO pattern
        dto.setTitle(title);
        dto.setAuthorId(authorId);
        dto.setGenreId(genreId);
        dto.setPrice(price);
        dto.setStatusId(statusId);
        if(discountPrice==null){
            dto.setDiscountPrice(0.00);
        }else{
            dto.setDiscountPrice(discountPrice);
        }

        dto.setDescription(description);

        String result = new AdminBookService().updateBookDetails(request, dto,
                coverInputStream, coverDetails,
                ebookInputStream, ebookDetails);

        return Response.ok().entity(result).build();
    }


    @Path("/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchAdminBooks(
            @QueryParam("text") String text,
            @QueryParam("genreId") Integer genreId,
            @QueryParam("author") Integer author,
            @QueryParam("page") @DefaultValue("0") int page,
            @Context HttpServletRequest request
    ) {
        String responseJson = new AdminBookService()
                .searchAdminBooks(text, genreId, author, page);

        return Response.ok(responseJson).build();
    }
}

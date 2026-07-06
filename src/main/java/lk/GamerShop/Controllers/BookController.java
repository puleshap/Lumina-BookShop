package lk.GamerShop.Controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.Annotations.IsCustomer;
import lk.GamerShop.DTO.FeedBackDTO;
import lk.GamerShop.service.BookService;

@Path("/books")
public class BookController {


  @Path("")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks(@Context HttpServletRequest request) {
String response = new BookService().loadLatestBooks(request);
      return Response.ok().entity(response).build();
  }


  @Path("/search")
    @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response searchBooks(
          @QueryParam("title") String title,
          @QueryParam("genreId") Integer genreId,
          @QueryParam("author") String author,
          @QueryParam("minPrice") Double minPrice,
          @QueryParam("maxPrice") Double maxPrice,
          @QueryParam("page")  int page,
          @QueryParam("size")  int size,
          @Context HttpServletRequest request
  ) {
      String response = new BookService()
              .searchBooks(title, genreId, author, minPrice, maxPrice, page, size, request);

      return Response.ok(response).build();
  }



  @Path("/bookdetails")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response bookdetails( @QueryParam("id") int id, @Context HttpServletRequest request){
 String response = new BookService().detailedBook(id, request);
      return Response.ok().entity(response).build();

  }



    @IsCustomer
    @Path("/feedback")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response bookFeedback(String data, @Context  HttpServletRequest request) {
        String response = new BookService().bookFeedback(data,request);
        return Response.ok().entity(response).build();

    }

    @Path("/detailedFeedback")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response detailedFeedback( @QueryParam("id") int id){
        String response = new BookService().detailedFeedback(id);
        return Response.ok().entity(response).build();

    }
}

package lk.GamerShop.service;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;
import lk.GamerShop.DTO.BookDTO;
import lk.GamerShop.DTO.FeedBackDTO;
import lk.GamerShop.Entities.*;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class BookService {

public String loadLatestBooks(@Context HttpServletRequest request){
    JsonObject jsonObject = new JsonObject();
    boolean status = false;
    String message = "";

    Session session = HibernateUtil.getSessionFactory().openSession();

    HttpSession usession = request.getSession(false);


    try{
        User user = null;
        if (usession != null && usession.getAttribute("user") != null) {
            user = (User) usession.getAttribute("user");
        }



    Status activestatus = session.createNamedQuery("Status.findByValue", Status.class).
            setParameter("value", String.valueOf(Status.Type.ACTIVE)).getSingleResultOrNull();


    List<Book> books = session.createQuery("from Book b WHERE b.status=:status ORDER BY b.createdAt DESC ",Book.class).
            setParameter("status",activestatus).setMaxResults(6).getResultList();

        List<Integer> purchasedBookIds = session.createQuery("SELECT l.ebook.id FROM Library l WHERE l.user = :user", Integer.class)
                .setParameter("user", user)
                .getResultList();


System.out.println(purchasedBookIds);


    List<BookDTO> bookDTOS = new ArrayList<>();
    for(Book book : books){
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setCoverImagePath(book.getCoverimagepath());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor().getName());
        bookDTO.setPrice(book.getPrice());
        bookDTO.setDiscountPrice(book.getDiscountPrice());

        if (purchasedBookIds.contains(book.getId())) {

            bookDTO.setBought(true);
            bookDTO.setFilePath(book.getFilepath());
        }

        bookDTOS.add(bookDTO);

        status = true;
        jsonObject.addProperty("status",status);
    }
        session.close();
    jsonObject.add("books",AppUtil.GSON.toJsonTree(bookDTOS));




    }catch (HibernateException e){
    e.printStackTrace();
}
    return AppUtil.GSON.toJson(jsonObject);
}






public String searchBooks(String title, Integer genreId, String author, Double minPrice, Double maxPrice, int page, int size, @Context HttpServletRequest request) {

        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";

        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        if (minPrice != null && minPrice < 0) {
            response.addProperty("status", status);
            response.addProperty("message", "Minimum price cannot be negative");
            return AppUtil.GSON.toJson(response);
        }

        if (maxPrice != null && maxPrice < 0) {
            response.addProperty("status", status);
            response.addProperty("message", "Maximum price cannot be negative");
            return AppUtil.GSON.toJson(response);
        }

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            response.addProperty("status", status);
            response.addProperty("message", "Minimum price cannot be greater than maximum price");
            return AppUtil.GSON.toJson(response);
        }

        Session session = HibernateUtil.getSessionFactory().openSession();

    HttpSession usession = request.getSession(false);





        try {
            User user = null;
            if (usession != null && usession.getAttribute("user") != null) {
                user = (User) usession.getAttribute("user");
            }

            Status activeStatus = session.createNamedQuery("Status.findByValue", Status.class)
                    .setParameter("value", String.valueOf(Status.Type.ACTIVE))
                    .getSingleResultOrNull();

            StringBuilder mainHql = new StringBuilder(
                    " FROM Book b WHERE b.status = :status "
            );

            if (title != null && !title.trim().isEmpty()) {
                mainHql.append(" AND b.title LIKE :title ");
            }
            if (author != null && !author.trim().isEmpty()) {
                mainHql.append(" AND b.author.name LIKE :author ");
            }
            if (genreId != null && genreId > 0) {
                mainHql.append(" AND b.genre.id = :genreId ");
            }
            if (minPrice != null) {
                mainHql.append(" AND b.price >= :minPrice ");
            }
            if (maxPrice != null) {
                mainHql.append(" AND b.price <= :maxPrice ");
            }

            Query<Book> dataQuery = session.createQuery("SELECT b " + mainHql + " ORDER BY b.createdAt DESC", Book.class);

            Query<Long> countQuery = session.createQuery("SELECT COUNT(b.id)" + mainHql, Long.class);

            // -------------------- Common parameters --------------------
            dataQuery.setParameter("status", activeStatus);
            countQuery.setParameter("status", activeStatus);

            if (title != null && !title.trim().isEmpty()) {
                String titleParam = "%" + title + "%";
                dataQuery.setParameter("title", titleParam);
                countQuery.setParameter("title", titleParam);
            }

            if (author != null && !author.trim().isEmpty()) {
                String authorParam = "%" + author+ "%";
                dataQuery.setParameter("author", authorParam);
                countQuery.setParameter("author", authorParam);
            }

            if (genreId != null && genreId > 0) {
                dataQuery.setParameter("genreId", genreId);
                countQuery.setParameter("genreId", genreId);
            }

            if (minPrice != null) {
                dataQuery.setParameter("minPrice", minPrice);
                countQuery.setParameter("minPrice", minPrice);
            }

            if (maxPrice != null) {
                dataQuery.setParameter("maxPrice", maxPrice);
                countQuery.setParameter("maxPrice", maxPrice);
            }

            // -------------------- Pagination --------------------
            dataQuery.setFirstResult(page * size);
            dataQuery.setMaxResults(size);

            List<Book> books = dataQuery.getResultList();

            List<Integer> purchasedBookIds = session.createQuery("SELECT l.ebook.id FROM Library l WHERE l.user = :user", Integer.class)
                    .setParameter("user", user)
                    .getResultList();


            System.out.println(purchasedBookIds);

            // -------------------- DTO mapping --------------------
            List<BookDTO> bookDTOS = new ArrayList<>();
            for (Book book : books) {
                BookDTO dto = new BookDTO();
                dto.setId(book.getId());
                dto.setTitle(book.getTitle());
                dto.setAuthor(book.getAuthor().getName());
                dto.setPrice(book.getPrice());
                dto.setDiscountPrice(book.getDiscountPrice());
                dto.setCoverImagePath(book.getCoverimagepath());
                if (purchasedBookIds.contains(book.getId())) {
                    dto.setBought(true);
                    dto.setFilePath(book.getFilepath());
                }
                bookDTOS.add(dto);
            }

            // -------------------- Count & pages --------------------
            long totalCount = countQuery.getSingleResult();
            int totalPages = (int) Math.ceil((double) totalCount / size);

            // -------------------- Response --------------------
            response.add("books", AppUtil.GSON.toJsonTree(bookDTOS));
            response.addProperty("page", page);
            response.addProperty("size", size);
            response.addProperty("totalCount", totalCount);
            response.addProperty("totalPages", totalPages);

            status = true;
            message = "success";

        } finally {
            session.close();
        }

        response.addProperty("status", status);
        response.addProperty("message", message);
        return AppUtil.GSON.toJson(response);
    }







    public String detailedBook(int id ,@Context HttpServletRequest request) {

        JsonObject response = new JsonObject();
        boolean status = false;

        Session session = HibernateUtil.getSessionFactory().openSession();
        HttpSession usession = request.getSession(false);




        User user = null;
        if (usession != null && usession.getAttribute("user") != null) {
            user = (User) usession.getAttribute("user");
        }

        try {
            Status activeStatus = session.createNamedQuery(
                            "Status.findByValue", Status.class)
                    .setParameter("value", String.valueOf(Status.Type.ACTIVE))
                    .getSingleResultOrNull();

            Book book = session.createQuery(
                            "FROM Book b WHERE b.status = :status AND b.id = :id",
                            Book.class
                    )
                    .setParameter("status", activeStatus)
                    .setParameter("id", id)
                    .getSingleResultOrNull();

            List<Integer> purchasedBookIds = session.createQuery("SELECT l.ebook.id FROM Library l WHERE l.user = :user", Integer.class)
                    .setParameter("user", user)
                    .getResultList();


            System.out.println(purchasedBookIds);




            if (book == null) {
                response.addProperty("status", status);
                return  AppUtil.GSON.toJson(response);

            }

                BookDTO dto = new BookDTO();
                dto.setId(book.getId());
                dto.setTitle(book.getTitle());
                dto.setGenreId(book.getGenre().getId());
                dto.setDescription(book.getDescription());
                dto.setFilePath(book.getFilepath());
                dto.setAuthor(book.getAuthor().getName());
                dto.setPrice(book.getPrice());
                dto.setDiscountPrice(book.getDiscountPrice());
                dto.setCoverImagePath(book.getCoverimagepath());
                if (purchasedBookIds.contains(book.getId())) {
                    dto.setBought(true);
                    dto.setFilePath(book.getFilepath());
                }


            Genre genre = session.createQuery("FROM Genre g where g.id=:id",Genre.class).
                    setParameter("id",dto.getGenreId()).getSingleResultOrNull();

            List<Book> relatedbooks = session.createQuery("FROM Book b WHERE genre =:genre AND b.status=:status AND b.id!=:id ",Book.class).
                    setParameter("genre",genre).setMaxResults(4).
                    setParameter("id",dto.getId()).
                    setParameter("status",activeStatus).getResultList();

            ArrayList<BookDTO> relatedbookDTOS = new ArrayList<>();

            for(Book relatedBook: relatedbooks) {
                BookDTO books = new BookDTO();
                books.setId(relatedBook.getId());
                books.setTitle(relatedBook.getTitle());
                books.setAuthor(relatedBook.getAuthor().getName());
                books.setPrice(relatedBook.getPrice());
                books.setCoverImagePath(relatedBook.getCoverimagepath());
                relatedbookDTOS.add(books);
            }

            List<Book> authoredbooks =session.createQuery("FROM Book b WHERE b.author=:authoer AND b.status=:status AND b.id!=:id",Book.class).
                    setParameter("authoer",book.getAuthor()).
                    setParameter("id",dto.getId()).
                    setParameter("status",activeStatus).
                    setMaxResults(4).getResultList();


            ArrayList<BookDTO> AuthorBookDTO = new ArrayList<>();

            for(Book AuthorBook : authoredbooks) {
                BookDTO books = new BookDTO();
                books.setId(AuthorBook.getId());
                books.setTitle(AuthorBook.getTitle());
                books.setAuthor(AuthorBook.getAuthor().getName());
                books.setPrice(AuthorBook.getPrice());
                books.setCoverImagePath(AuthorBook.getCoverimagepath());
                AuthorBookDTO.add(books);
            }



            response.add("authoredbooks",AppUtil.GSON.toJsonTree(AuthorBookDTO));
            response.add("related",AppUtil.GSON.toJsonTree(relatedbookDTOS));
                response.add("book", AppUtil.GSON.toJsonTree(dto));
                status = true;



        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        response.addProperty("status", status);
        return AppUtil.GSON.toJson(response);
    }


    public String bookFeedback(String data ,@Context HttpServletRequest req) {

        JsonObject response = new JsonObject();
        String message;
        boolean status = false;

        Session session = HibernateUtil.getSessionFactory().openSession();
        HttpSession usession = req.getSession();
        Transaction tr = session.beginTransaction();

        JsonObject obj = AppUtil.GSON.fromJson(data, JsonObject.class);
        FeedBackDTO dto = AppUtil.GSON.fromJson(obj, FeedBackDTO.class);

        System.out.println(dto);

       try{
           Book book = session.get(Book.class, dto.getBookId());
           User user = null;
           if (usession != null && usession.getAttribute("user") != null) {
               user = (User) usession.getAttribute("user");
           }


           Feedback feedback = new Feedback();
           feedback.setFeedback(dto.getText());
           feedback.setUser(user);
           feedback.setBook(book);
           feedback.setRate(dto.getRate());

           session.persist(feedback);
           tr.commit();
           status = true;
           message="Feedback Recorded Successfully";

           response.addProperty("message", message);


       } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        response.addProperty("status", status);
        return AppUtil.GSON.toJson(response);
    }


    public String detailedFeedback(int id ) {

        JsonObject response = new JsonObject();
        boolean status = false;

        Session session = HibernateUtil.getSessionFactory().openSession();






        try {
           List<Feedback> feedbacks = session.createQuery("from Feedback f where f.book.id=:id ORDER BY f.createdAt DESC ",Feedback.class)
                   .setParameter("id",id).getResultList();



            ArrayList<FeedBackDTO> feedBackDTOS = new ArrayList<>();

            for(Feedback feedback: feedbacks) {
                FeedBackDTO feedBackDTO = new FeedBackDTO();
                feedBackDTO.setId(feedback.getId());
                feedBackDTO.setRate(feedback.getRate());
                feedBackDTO.setText(feedback.getFeedback());
                feedBackDTO.setUsertName(feedback.getUser().getFirstName() + " " + feedback.getUser().getLastName());
                feedBackDTO.setCreatedDate(feedback.getCreatedAt().toString());

                feedBackDTOS.add(feedBackDTO);
            }



            response.add("feedbacks",AppUtil.GSON.toJsonTree(feedBackDTOS));
            status = true;



        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        response.addProperty("status", status);
        return AppUtil.GSON.toJson(response);
    }

}







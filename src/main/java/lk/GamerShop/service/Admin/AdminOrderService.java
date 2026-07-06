package lk.GamerShop.service.Admin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import lk.GamerShop.DTO.AuthorDTO;
import lk.GamerShop.DTO.BookDTO;
import lk.GamerShop.DTO.OrderDTO;
import lk.GamerShop.Entities.*;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.Utils.HibernateUtil;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class AdminOrderService {





    public String getOrders(int page){
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";

        int size = 10;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Order> query = session.createQuery("from Order b order by b.id desc ", Order.class);
            Long total = session.createQuery("select count(b) from Order b", Long.class).uniqueResult();

            query.setFirstResult(page * size);
            query.setMaxResults(size);



            List<Order> orders = query.getResultList();

            List<OrderDTO> orderDTOS = new ArrayList<>();
            for (Order order : orders) {
                List<OrderItem> items= session.createQuery("from OrderItem OI where OI.order=:order",OrderItem.class)
                        .setParameter("order",order).getResultList();

                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setOrderNumber(order.getId());
                orderDTO.setOrderStatus(order.getStatus().getValue());
                orderDTO.setTotalAmount(order.getTotal());
                orderDTO.setItemCount(items.size());
                orderDTO.setCustomerName(order.getUser().getFirstName() +  " " + order.getUser().getLastName());
                orderDTO.setCustomerEmail(order.getUser().getEmail());
                orderDTO.setOrderDate(order.getCreatedAt().toLocalDate().toString());





                orderDTOS.add(orderDTO);
            }
            status = true;
            response.add("orders",AppUtil.GSON.toJsonTree(orderDTOS));
            System.out.println(AppUtil.GSON.toJsonTree(orderDTOS));
            response.addProperty("totalOrders", total);
            response.addProperty("currentPage", page);
            response.addProperty("pageSize", size);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Failed to load Orders";
        }
        response.addProperty("message", message);
        response.addProperty("status", status);
        return AppUtil.GSON.toJson(response);

        }


    public String searchOrders(String search){
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";
        System.out.println(search);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {


            List<Order> orders = session.createQuery("FROM Order a WHERE a.user.email LIKE :search  ORDER BY a.id DESC ", Order.class)
                    .setParameter("search", "%"+search+"%").getResultList();

            int total = orders.size();

            List<OrderDTO> orderDTOS = new ArrayList<>();
            for (Order order : orders) {
                List<OrderItem> items= session.createQuery("from OrderItem OI where OI.order=:order",OrderItem.class)
                        .setParameter("order",order).getResultList();
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setOrderNumber(order.getId());
                orderDTO.setOrderStatus(order.getStatus().getValue());
                orderDTO.setTotalAmount(order.getTotal());
                orderDTO.setItemCount(items.size());
                orderDTO.setCustomerName(order.getUser().getFirstName() +  " " + order.getUser().getLastName());
                orderDTO.setCustomerEmail(order.getUser().getEmail());
                orderDTO.setOrderDate(order.getCreatedAt().toLocalDate().toString());



                orderDTOS.add(orderDTO);
            }
            status = true;
            response.add("orders",AppUtil.GSON.toJsonTree(orderDTOS));
            response.addProperty("totalOrders", total);

        } catch (Exception e) {
            e.printStackTrace();
            message = "Failed to load users";
        }
        response.addProperty("message", message);
        response.addProperty("status", status);
        return AppUtil.GSON.toJson(response);

    }

    public String changeStatus(int id){
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try{
            User user = session.get(User.class, id);
            if(user.getStatus().getValue().equals(String.valueOf(Status.Type.VERIFIED))){
                Status userstatus = session.createQuery("from Status s where s.value=:value",Status.class)
                                .setParameter("value", String.valueOf(Status.Type.BLOCKED)).uniqueResult();
                user.setStatus(userstatus);
            }else if(user.getStatus().getValue().equals(String.valueOf(Status.Type.BLOCKED))){
                Status userstatus = session.createQuery("from Status s where s.value=:value",Status.class)
                        .setParameter("value", String.valueOf(Status.Type.VERIFIED)).uniqueResult();
                user.setStatus(userstatus);
            }
            tx.commit();
            session.merge(user);


            status = true;
            response.addProperty("message", message);
            response.addProperty("status", status);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  AppUtil.GSON.toJson(response);

    }



    public String saveBook(HttpServletRequest request, BookDTO dto,
                           InputStream coverStream, FormDataContentDisposition coverDetails,
                           InputStream ebookStream, FormDataContentDisposition ebookDetails) {

        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        // Basic server-side validation
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            message = "Book title is required.";
        } else if (dto.getPrice() ==0.00 || dto.getPrice() < 0) {
            message = "Valid book price is required.";
        } else if (coverDetails == null || ebookDetails == null) {
            message = "Both the cover image and the E-book file are required.";
        } else {

            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = null;

            try {
                transaction = hibernateSession.beginTransaction();

                // 1. Fetch related Author and Genre entities from the DB
                Author author = hibernateSession.createQuery("from Author a where a.id=:id",Author.class)
                        .setParameter("id", dto.getAuthorId()).uniqueResult();
                Genre genre = hibernateSession.get(Genre.class, dto.getGenreId());

                Status status1 = hibernateSession.createNamedQuery("Status.findByValue", Status.class).
                        setParameter("value", String.valueOf(Status.Type.ACTIVE)).getSingleResultOrNull();


                if (author == null) {
                    message = "Selected author not found.";
                } else if (genre == null) {
                    message = "Selected genre not found.";
                } else {

                    // 2. Instantiate and map the Book entity
                    System.out.println("Received Author ID: " + dto.getAuthorId());

                    Book book = new Book();
                    book.setTitle(dto.getTitle());
                    book.setPrice(dto.getPrice());
                    book.setDiscountPrice(dto.getDiscountPrice());
                    book.setDescription(dto.getDescription());
                    book.setAuthor(author);
                    book.setGenre(genre);
                    book.setStatus(status1);

                    // 3. Create a clean system folder name from the book title
                    // Replaces spaces/special characters with underscores to keep URLs clean
                    String sanitizedBookName = dto.getTitle().trim()
                            .replaceAll("[^a-zA-Z0-9-_\\s]", "")
                            .replaceAll("\\s+", "_")
                            .toLowerCase();

                    // Relative path where web context files are hosted
                    String relativeFolderPath = "assets/books/" + sanitizedBookName + "/";
                    String absoluteUploadPath = request.getServletContext().getRealPath("/") + relativeFolderPath;

                    File folder = new File(absoluteUploadPath);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }

                    // 4. Process and write the Cover Image File
                    String coverExtension = coverDetails.getFileName().substring(coverDetails.getFileName().lastIndexOf("."));
                    String coverFileName = "cover" + coverExtension;
                    Files.copy(coverStream, Paths.get(absoluteUploadPath + coverFileName), StandardCopyOption.REPLACE_EXISTING);
                    book.setCoverimagepath(relativeFolderPath + coverFileName);

                    // 5. Process and write the Ebook File (PDF/EPUB)
                    String ebookExtension = ebookDetails.getFileName().substring(ebookDetails.getFileName().lastIndexOf("."));
                    String ebookFileName = "book" + ebookExtension;
                    Files.copy(ebookStream, Paths.get(absoluteUploadPath + ebookFileName), StandardCopyOption.REPLACE_EXISTING);
                    book.setFilepath(relativeFolderPath + ebookFileName);

                    // 6. Persist everything to your Database
                    hibernateSession.persist(book);
                    transaction.commit();

                    status = true;
                    message = "Book added successfully!";
                }

            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                e.printStackTrace();
                message = "An internal server error occurred: " + e.getMessage();
            } finally {
                hibernateSession.close();
            }
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }


    public String updateBookDetails(HttpServletRequest request, BookDTO dto,
                                    InputStream coverStream, FormDataContentDisposition coverDetails,
                                    InputStream ebookStream, FormDataContentDisposition ebookDetails) {

        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        if (String.valueOf(dto.getId()).isBlank()) {
            message = "Target Book ID is missing.";
        } else if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            message = "Book title cannot be empty.";
        } else if (String.valueOf(dto.getPrice()).isBlank()|| dto.getPrice() < 0) {
            message = "Please provide a valid price calculation.";
        } else {

            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = null;

            try {
                transaction = hibernateSession.beginTransaction();

                // Load managed persistent record from DB context
                Book singleBook = hibernateSession.get(Book.class, dto.getId());
                Status status1 = hibernateSession.createQuery("from Status s where s.id=:id",Status.class)
                        .setParameter("id", dto.getStatusId()).uniqueResult();

                if (singleBook == null) {
                    message = "Invalid Book to Update";
                } else {
                    Author author = hibernateSession.get(Author.class, dto.getAuthorId());
                    Genre genre = hibernateSession.get(Genre.class, dto.getGenreId());

                    singleBook.setTitle(dto.getTitle());
                    singleBook.setPrice(dto.getPrice());
                    singleBook.setDiscountPrice(dto.getDiscountPrice());
                    singleBook.setDescription(dto.getDescription());
                    singleBook.setAuthor(author);
                    singleBook.setGenre(genre);
                    singleBook.setStatus(status1);

                    // Setup clean asset directory paths matching name properties
                    String sanitizedBookName = dto.getTitle().trim()
                            .replaceAll("[^a-zA-Z0-9-_\\s]", "")
                            .replaceAll("\\s+", "_")
                            .toLowerCase();

                    String relativeFolderPath = "assets/books/" + sanitizedBookName + "/";
                    String absoluteUploadPath = request.getServletContext().getRealPath("/") + relativeFolderPath;

                    File folder = new File(absoluteUploadPath);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }

                    // Process Cover Image Update if present
                    if (coverDetails != null && coverDetails.getFileName() != null && !coverDetails.getFileName().isBlank()) {
                        String coverExt = coverDetails.getFileName().substring(coverDetails.getFileName().lastIndexOf("."));
                        String coverFileName = "cover" + coverExt;
                        Files.copy(coverStream, Paths.get(absoluteUploadPath + coverFileName), StandardCopyOption.REPLACE_EXISTING);
                        singleBook.setCoverimagepath(relativeFolderPath + coverFileName);
                    }

                    // Process E-book File Document binary stream update if present
                    if (ebookDetails != null && ebookDetails.getFileName() != null && !ebookDetails.getFileName().isBlank()) {
                        String ebookExt = ebookDetails.getFileName().substring(ebookDetails.getFileName().lastIndexOf("."));
                        String ebookFileName = "book" + ebookExt;
                        Files.copy(ebookStream, Paths.get(absoluteUploadPath + ebookFileName), StandardCopyOption.REPLACE_EXISTING);
                        singleBook.setFilepath( relativeFolderPath + ebookFileName);
                    }

                    hibernateSession.merge(singleBook);
                    transaction.commit();

                    status = true;
                    message = "Book Updated successfully!";
                }

            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                e.printStackTrace();
                message = "Error Updating Book";
            } finally {
                hibernateSession.close();
            }
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }

    public String searchAdminBooks(String text, Integer genreId, Integer author, int page) {
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";

        if (page < 0) page = 0;
        int size = 10;

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            StringBuilder mainHql = new StringBuilder(" FROM Book b WHERE 1=1 ");

            if (text != null && !text.trim().isEmpty()) {
                mainHql.append(" AND b.title LIKE :text ");
            }
            if (author != null && author >0) {
                mainHql.append(" AND b.author.id LIKE :author ");
            }
            if (genreId != null && genreId > 0) {
                mainHql.append(" AND b.genre.id = :genreId ");
            }

            // Build Queries
            Query<Book> dataQuery = session.createQuery("SELECT b " + mainHql + " ORDER BY b.id DESC", Book.class);
            Query<Long> countQuery = session.createQuery("SELECT COUNT(b.id)" + mainHql, Long.class);

            // Assign Dynamic Parameters
            if (text != null && !text.trim().isEmpty()) {
                String textParam = "%" + text.trim() + "%";
                dataQuery.setParameter("text", textParam);
                countQuery.setParameter("text", textParam);
            }
            if (author != null && author>0) {
                dataQuery.setParameter("author", author);
                countQuery.setParameter("author", author);
            }
            if (genreId != null && genreId > 0) {
                dataQuery.setParameter("genreId", genreId);
                countQuery.setParameter("genreId", genreId);
            }

            // Apply Pagination Range Windows
            dataQuery.setFirstResult(page * size);
            dataQuery.setMaxResults(size);

            List<Book> books = dataQuery.getResultList();

            // Direct Plain Object Mapping (No transaction ownership validation checked)
            List<BookDTO> bookDTOs = new ArrayList<>();
            for (Book book : books) {
                BookDTO bookDTO = new BookDTO();
                bookDTO.setId(book.getId());
                bookDTO.setTitle(book.getTitle());
                bookDTO.setDescription(book.getDescription());
                bookDTO.setCreatedAt(book.getCreatedAt().toLocalDate().toString());
                bookDTO.setDiscountPrice(book.getDiscountPrice());
                bookDTO.setPrice(book.getPrice());
                bookDTO.setFilePath(book.getFilepath());
                bookDTO.setCoverImagePath(book.getCoverimagepath());
                bookDTO.setGenrevalue(book.getGenre().getName());
                bookDTO.setAuthor(book.getAuthor().getName());
                bookDTO.setAuthorId(book.getAuthor().getId());
                bookDTO.setGenreId(book.getGenre().getId());
                bookDTO.setStatus(book.getStatus().getValue());
                bookDTO.setStatusId(book.getStatus().getId());



                bookDTOs.add(bookDTO);
            }

            long totalCount = countQuery.getSingleResult();
            int totalPages = (int) Math.ceil((double) totalCount / size);

            response.add("books", AppUtil.GSON.toJsonTree(bookDTOs));
            response.addProperty("page", page);
            response.addProperty("size", size);
            response.addProperty("totalBooks", totalCount);
            response.addProperty("totalPages", totalPages);

            status = true;
            message = "Book Laoding Successful";

        } catch (Exception e) {
            e.printStackTrace();
            message = "Error querying admin dataset: " + e.getMessage();
        } finally {
            session.close();
        }

        response.addProperty("status", status);
        response.addProperty("message", message);
        return AppUtil.GSON.toJson(response);
    }
    }


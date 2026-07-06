package lk.GamerShop.service;


import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;
import lk.GamerShop.DTO.BookDTO;
import lk.GamerShop.DTO.OrderItemDTO;
import lk.GamerShop.Entities.Book;
import lk.GamerShop.Entities.Order;
import lk.GamerShop.Entities.OrderItem;
import lk.GamerShop.Entities.User;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.Utils.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class LibraryService {

public String getLibrary(@Context HttpServletRequest request) {
    JsonObject jsonObject = new JsonObject();
    boolean status = false;
    String message = "";

    Session session = HibernateUtil.getSessionFactory().openSession();

    try {
        User user;
        if (request.getSession().getAttribute("user") == null) {
            message = "No user logged in";
            jsonObject.addProperty("message", message);
            jsonObject.addProperty("status", status);
            return AppUtil.GSON.toJson(jsonObject);
        }
        HttpSession httpsession = request.getSession(true);
        user = (User) httpsession.getAttribute("user");
        System.out.println(user);
        List<Book> books = session.createQuery("select l.ebook from Library l where l.user=:user", Book.class)
                .setParameter("user",user).getResultList();

        List<BookDTO> bookDTOS = new ArrayList<BookDTO>();
        for (Book book : books) {
            BookDTO bookDTO = new BookDTO();
            bookDTO.setId(book.getId());
            bookDTO.setTitle(book.getTitle());
            bookDTO.setAuthor(book.getAuthor().getName());
            bookDTO.setCoverImagePath(book.getCoverimagepath());
            bookDTO.setFilePath(book.getFilepath());
            bookDTOS.add(bookDTO);

        }
        int count = bookDTOS.size();
        status = true;
        jsonObject.addProperty("count", count);
        jsonObject.addProperty("status", status);
        jsonObject.add("books", AppUtil.GSON.toJsonTree(bookDTOS));
    } catch (Exception e) {
        e.printStackTrace();
        status = false;
        jsonObject.addProperty("message", e.getMessage());
        jsonObject.addProperty("status", status);
    }

    return AppUtil.GSON.toJson(jsonObject);
}
}

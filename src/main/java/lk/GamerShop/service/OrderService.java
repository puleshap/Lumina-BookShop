package lk.GamerShop.service;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;
import lk.GamerShop.DTO.BookDTO;
import lk.GamerShop.DTO.OrderDTO;
import lk.GamerShop.DTO.UserDTO;
import lk.GamerShop.Entities.*;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.Utils.HibernateUtil;
import lk.GamerShop.mail.OrderSummaryMail;
import lk.GamerShop.provider.MailServiceProvider;
import org.hibernate.Session;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderService {

    public String getOrderById(int orderId){
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "here";
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            Order order =session.get(Order.class, orderId);
           if(order==null || !order.getStatus().getValue().equals(String.valueOf(Status.Type.COMPLETED))){
               message = "Order does not exist or is invalid";
               response.addProperty("status",status);
               response.addProperty("message",message);
               return  AppUtil.GSON.toJson(response);

           }

            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderNumber(orderId);


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            orderDTO.setOrderDate(formatter.format(order.getCreatedAt()));
            orderDTO.setTotalAmount(order.getTotal());


            User user = session.get(User.class, order.getUser().getId());
            List<OrderItem> items= session.createQuery("from OrderItem OI where OI.order=:order",OrderItem.class)
                    .setParameter("order",order).getResultList();


            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(user.getEmail());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());

            List<BookDTO> books = new ArrayList<BookDTO>();

            for(OrderItem item:items){
                Book book = session.get(Book.class,item.getEbook().getId());
                BookDTO  bookDTO = new BookDTO();
                bookDTO.setId(book.getId());
                bookDTO.setCoverImagePath(book.getCoverimagepath());
                bookDTO.setTitle(book.getTitle());
                bookDTO.setPrice(item.getPriceAtPurchase());
                bookDTO.setGenrevalue(book.getGenre().getName());

                books.add(bookDTO);
            }
            status = true;
            response.addProperty("size",books.size());
            response.addProperty("status",status);
            response.add("user",AppUtil.GSON.toJsonTree(userDTO));
            response.add("book",AppUtil.GSON.toJsonTree(books));
            response.add("order", AppUtil.GSON.toJsonTree(orderDTO));





        }catch(Exception e){
            e.printStackTrace();
        }
        return  AppUtil.GSON.toJson(response);


    }


    public String ordersummary(int orderId) {
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "here";

        try  {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Order order = session.get(Order.class, orderId);
            if (order == null || !order.getStatus().getValue().equals(String.valueOf(Status.Type.COMPLETED))) {
                response.addProperty("status", status);
                response.addProperty("message", message);
                return AppUtil.GSON.toJson(response);
            }


            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderNumber(orderId);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            orderDTO.setOrderDate(formatter.format(order.getCreatedAt()));
            orderDTO.setTotalAmount(order.getTotal());


            User user = order.getUser();
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(user.getEmail());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            try {
                OrderSummaryMail orderSummaryMail = new OrderSummaryMail(user.getEmail(), orderDTO);
                MailServiceProvider.getInstance().sendMail(orderSummaryMail);
                System.out.println("Order summary email sent successfully.");
            } catch (Exception mailEx) {
                System.err.println("Email failed to send, but processing continues: " + mailEx.getMessage());
            }

            status = true;
            response.addProperty("status", status);
            response.add("user", AppUtil.GSON.toJsonTree(userDTO));
            response.add("order", AppUtil.GSON.toJsonTree(orderDTO));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return AppUtil.GSON.toJson(response);
    }

    public String getOrderList(@Context HttpServletRequest request) {
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "Sign in to see User Orders";
        try{
            HttpSession httpSession = request.getSession(false);
            if(httpSession==null||httpSession.getAttribute("user") == null){
                response.addProperty("status", status);
                response.addProperty("message", message);
                return AppUtil.GSON.toJson(response);
            }
            User user = (User) httpSession.getAttribute("user");
            System.out.println(user.getId());
            Session session = HibernateUtil.getSessionFactory().openSession();

            User db=session.get(User.class, user.getId());

            List<Order> orders = session.createQuery("from Order o where o.user=:user and o.status.value='COMPLETED' ORDER BY o.createdAt DESC ",Order.class)
                    .setParameter("user",db).getResultList();


            List<OrderDTO> orderDTOs = new ArrayList<>();
            for(Order order:orders) {

                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setOrderNumber(order.getId());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
                orderDTO.setOrderDate(formatter.format(order.getCreatedAt()));
                orderDTO.setTotalAmount(order.getTotal());

                orderDTO.setOrderStatus(order.getStatus().getValue());
                List<OrderItem> orderItems = session.createQuery("from OrderItem i where i.order=:order", OrderItem.class)
                        .setParameter("order",order).getResultList();
                orderDTO.setItemCount(orderItems.size());
                orderDTOs.add(orderDTO);

            }



            status = true;
            response.addProperty("status",status);
            response.add("order", AppUtil.GSON.toJsonTree(orderDTOs));





        }catch(Exception e){
            e.printStackTrace();
        }
        return  AppUtil.GSON.toJson(response);


    }

}

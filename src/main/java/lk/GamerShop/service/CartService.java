package lk.GamerShop.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;
import lk.GamerShop.DTO.BookDTO;
import lk.GamerShop.DTO.CartItemDTO;
import lk.GamerShop.Entities.*;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

public class CartService {

    public String AddCart(String json, @Context HttpServletRequest request) {
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";

        JsonObject req = AppUtil.GSON.fromJson(json, JsonObject.class);

        int bookid = req.get("bookid").getAsInt();

        HttpSession httpsession = request.getSession(true);
        Object user = httpsession.getAttribute("user");

        if(user ==null){

            Map<Integer, BookDTO> cartitems = (Map<Integer, BookDTO>) httpsession.
                    getAttribute("session_cart");

            if(cartitems ==null || cartitems.isEmpty()){
                cartitems = new HashMap<Integer, BookDTO>();
            }

            Session session = HibernateUtil.getSessionFactory().openSession();


            try{

                Status activestatus = session.createNamedQuery("Status.findByValue",Status.class).
                        setParameter("value",String.valueOf(Status.Type.ACTIVE)).getSingleResultOrNull();

                Book book = session.createQuery("FROM Book b WHERE b.id =:id AND status=:status",Book.class).
                        setParameter("id",bookid).
                        setParameter("status",activestatus).getSingleResultOrNull();

                if(book == null){
                    message = "Book Not Found";
                    response.addProperty("status", status);
                    response.addProperty("message", message);
                    return AppUtil.GSON.toJson(response);

                }
                double price;
                if(book.getDiscountPrice()==0){
                    price = book.getPrice();

                }else{
                    price = book.getDiscountPrice();
                }
                if(!cartitems.containsKey(bookid)){
                    BookDTO bookdto = new BookDTO();
                    bookdto.setId(bookid);
                    bookdto.setTitle(book.getTitle());
                    bookdto.setAuthor(book.getAuthor().getName());
                    bookdto.setPrice(price);
                    bookdto.setDiscountPrice(book.getDiscountPrice());
                    bookdto.setCoverImagePath(book.getCoverimagepath());

                    cartitems.put(bookid,bookdto);

                }

                httpsession.setAttribute("session_cart",cartitems);

                status = true;
                message="Cart Item Added Successfully";
            }catch (HibernateException e ){
                e.printStackTrace();
            }


            response.addProperty("status", status);
            response.addProperty("message", message);
            response.addProperty("cartSize", cartitems.size());

            session.close();



        }else {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            Cart cart = session.createQuery("FROM Cart c WHERE c.user=:user",Cart.class).
                    setParameter("user",user).getSingleResultOrNull();

            if(cart == null){
                cart = new Cart();
                cart.setUser((User) user);
                cart.setItems(new HashSet<>());
                session.persist(cart);
            }
            Book ebook = session.get(Book.class, bookid);

            CartItems existing = session.createQuery("FROM CartItems c WHERE c.cart=:cart AND c.book=:book", CartItems.class).
                    setParameter("cart",cart).setParameter("book",ebook).getSingleResultOrNull();

            if(existing != null){
                message = "Book Already Exists in Cart";
                response.addProperty("status", status);
                response.addProperty("message", message);
                return AppUtil.GSON.toJson(response);

            }
            double price;
            if(ebook.getDiscountPrice()==0){
                price = ebook.getPrice();

            }else{
                price = ebook.getDiscountPrice();
            }

                CartItems cartItems = new CartItems();
                cartItems.setBook(ebook);
                cartItems.setCart(cart);
                cartItems.setPriceAtTime(price);

                cart.getItems().add(cartItems);


            tx.commit();
            status = true;
            message="Cart Item Added Successfully";

            response.addProperty("status", status);
            response.addProperty("message", message);

            session.close();
        }











        return AppUtil.GSON.toJson(response);


    }




    public String getCart(HttpServletRequest request) {

        JsonObject response = new JsonObject();
        boolean status = false;
        boolean canreload = true;
        HttpSession session = request.getSession(true);
try{
    User user = (User) session.getAttribute("user");

    if (user == null) {

        Map<Integer, BookDTO> cart = (Map<Integer, BookDTO>) session.getAttribute("session_cart");

        if (cart == null) {
            response.addProperty("status", status);
            return AppUtil.GSON.toJson(response);
        }
        if (cart.size() == 0) {
            response.addProperty("reload", canreload);
            return AppUtil.GSON.toJson(response);


        }

        double total = 0;
        for (BookDTO item : cart.values()) {
            total += item.getPrice();

        }
        status = true;

        response.addProperty("status", status);
        response.add("items", AppUtil.GSON.toJsonTree(cart.values()));
        response.addProperty("total", total);


    }else{

        Session sess =  HibernateUtil.getSessionFactory().openSession();

        Cart cart = sess.createQuery("FROM Cart c WHERE c.user=:user",Cart.class).
                setParameter("user",user).getSingleResultOrNull();
        if(cart == null){
            response.addProperty("status", status);
            return  AppUtil.GSON.toJson(response);
        }
        List<CartItems> dbcartitems= sess.createQuery("FROM CartItems ci WHERE ci.cart=:cart",CartItems.class).
                setParameter("cart",cart).getResultList();

        if(dbcartitems==null){
            response.addProperty("status", status);
            return  AppUtil.GSON.toJson(response);

        }
        ArrayList<BookDTO> dbcartitem_array = new ArrayList<>();

        double total=0;
        for(CartItems item:dbcartitems){
            Book book = sess.get(Book.class, item.getBook().getId());
            Status bookStatus = sess.get(Status.class, book.getStatus().getId());

            if(bookStatus.getValue().equals(String.valueOf(Status.Type.ACTIVE))){
                BookDTO itemdto = new BookDTO();
                itemdto.setId(book.getId());
                itemdto.setTitle(book.getTitle());
                itemdto.setAuthor(book.getAuthor().getName());
                itemdto.setPrice(item.getPriceAtTime());
                total += item.getPriceAtTime();

                itemdto.setCoverImagePath(book.getCoverimagepath());
                dbcartitem_array.add(itemdto);

            }


        }
        status = true;
        response.add("items", AppUtil.GSON.toJsonTree(dbcartitem_array));
        response.addProperty("status", status);
        response.addProperty("total", total);



    }}catch(Exception e){
    e.printStackTrace();
}


        return AppUtil.GSON.toJson(response);
    }


    public String removeFromCart(String json, @Context HttpServletRequest request) {

        JsonObject response = new JsonObject();
        boolean status = false;
        HttpSession session = request.getSession(false);

       JsonObject req = AppUtil.GSON.fromJson(json, JsonObject.class);
       int bookId = req.get("id").getAsInt();

       User user = (User) session.getAttribute("user");
       if (user == null) {

           if(session ==null){
               response.addProperty("status", status);
               return AppUtil.GSON.toJson(response);
           }



           Map<Integer, BookDTO> cart = (Map<Integer, BookDTO>) session.getAttribute("session_cart");

           if (cart != null) {
               cart.remove(bookId);
               session.setAttribute("session_cart", cart);
               status = true;

           }
           if(cart.size()==0){
               response.addProperty("reloadpage", true);
               return AppUtil.GSON.toJson(response);


           }
           response.addProperty("status", true);

       }else{
           Session sess =  HibernateUtil.getSessionFactory().openSession();
           Transaction tx = sess.beginTransaction();

           Book book = sess.get(Book.class, bookId);

           Cart cart = sess.createQuery("FROM Cart c WHERE c.user=:user",Cart.class).
                   setParameter("user",user).getSingleResultOrNull();

           CartItems cartitem = sess.createQuery("FROM CartItems ci WHERE ci.book=:book AND ci.cart=:cart",CartItems.class).
                   setParameter("book",book).setParameter("cart",cart).getSingleResultOrNull();

           if(cart == null){
               response.addProperty("status", status);
               return AppUtil.GSON.toJson(response);
           }
           sess.remove(cartitem);

           tx.commit();
           status = true;
           response.addProperty("status", status);

       }

        return AppUtil.GSON.toJson(response);
    }



    public String clearCart(HttpServletRequest request) {
        JsonObject response = new JsonObject();
        boolean status = false;
        HttpSession session = request.getSession(false);

        User user = (User) session.getAttribute("user");
        if (user == null) {
            if (session != null) {
                session.removeAttribute("session_cart");
            }

            status=true;
            response.addProperty("status", status);

        }else{
            Session sess =  HibernateUtil.getSessionFactory().openSession();
            Transaction tx = sess.beginTransaction();

            sess.createQuery("DELETE FROM Cart c WHERE c.user=:user").
                    setParameter("user",user).executeUpdate();

            tx.commit();

            status = true;
            response.addProperty("status", status);
        }

        return AppUtil.GSON.toJson(response);
    }



}

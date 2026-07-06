package lk.GamerShop.service;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import lk.GamerShop.DTO.*;
import lk.GamerShop.Entities.*;
import lk.GamerShop.Main;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.Utils.Env;
import lk.GamerShop.Utils.HibernateUtil;
import lk.GamerShop.Utils.PayHereUtil;
import lk.GamerShop.Validations.Validator;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CheckoutService {

    public String fulfilprocess(@Context HttpServletRequest request) {

        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        HttpSession session = request.getSession(false);

        try {



            if (session.getAttribute("user") == null) {
                message = "Login to Process Checkout";
                response.addProperty("status", status);
                response.addProperty("message", message);
                return AppUtil.GSON.toJson(response);
            }
            User user = (User) session.getAttribute("user");
            UserDTO userDTO = new UserDTO();
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());

            Cart cart = hibernateSession
                    .createQuery("FROM Cart c WHERE c.user=:user", Cart.class)
                    .setParameter("user", user)
                    .getSingleResultOrNull();

            if (cart == null) {
                message = "Cart is Empty";
                response.addProperty("status", status);
                response.addProperty("message", message);
                return AppUtil.GSON.toJson(response);
            }

            List<CartItems> cartItems = hibernateSession
                    .createQuery("FROM CartItems c WHERE c.cart=:cart", CartItems.class)
                    .setParameter("cart", cart)
                    .getResultList();

            if (cartItems.isEmpty()) {
                message = "Cart is Empty";
                response.addProperty("status", status);
                response.addProperty("message", message);
                return AppUtil.GSON.toJson(response);
            }

            double total = 0.0;

            transaction = hibernateSession.beginTransaction();

            Status pendingStatus = hibernateSession
                    .createQuery("FROM Status s WHERE s.value=:value", Status.class)
                    .setParameter("value", "COMPLETED")
                    .getSingleResult();

            Order order = new Order();
            order.setUser(user);
            order.setStatus(pendingStatus);
            order.setQuantity(cartItems.size());
            for (CartItems cartItem : cartItems) {
                total += cartItem.getPriceAtTime();
            }

            order.setTotal(total);

            hibernateSession.persist(order);

            for (CartItems cartItem : cartItems) {

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setEbook(cartItem.getBook());

                orderItem.setPriceAtPurchase(cartItem.getPriceAtTime());

                Library lb = new Library();
                lb.setEbook(cartItem.getBook());
                lb.setUser(cartItem.getCart().getUser());

                hibernateSession.persist(orderItem);
                hibernateSession.persist(lb);
            }
            hibernateSession.remove(cart);
            transaction.commit();

            String hash = PayHereUtil.generateHash(
                    String.valueOf(order.getId()),
                    total
            );

            JsonObject paymentDetails = new JsonObject();
            paymentDetails.addProperty("merchant_id", PayHereUtil.getMerchantId());

            paymentDetails.addProperty("return_url", "http://localhost:8080/luminabooks/payment-success.html?OrderId=");
            paymentDetails.addProperty("success_url", "http://localhost:8080/luminabooks/payment-success.html?OrderId=");

            paymentDetails.addProperty("cancel_url", "http://localhost:8080/luminabooks/payment-failed.html");
            paymentDetails.addProperty("notify_url", Env.get("app.public.url") + "/api/payments/notify");
            paymentDetails.addProperty("order_id", order.getId());
            paymentDetails.addProperty("items", "E-Book Order #" + order.getId());
            paymentDetails.addProperty("currency", "LKR");
            paymentDetails.addProperty("amount", total);
            paymentDetails.addProperty("hash", hash);

            status = true;
            response.addProperty("status", status);
            response.addProperty("user",AppUtil.GSON.toJson(userDTO));
            response.add("paymentDetails", paymentDetails);

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            hibernateSession.close();
        }

        return AppUtil.GSON.toJson(response);
    }

    public void handleNotify(MultivaluedMap<String, String> form) {

        String orderId = form.getFirst("order_id");
        String statusCode = form.getFirst("status_code");

        if (Integer.parseInt(statusCode) == PayHereUtil.PAYMENT_SUCCESS) {
            completeOrder(orderId);
        } else {
            failedOrder(orderId);
        }
    }

    private void completeOrder(String orderId) {

        int oId = Integer.parseInt(orderId.replaceAll("[^0-9]", ""));

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();

            try {
                Order order = session.find(Order.class, oId);

                if (order == null) {
                    throw new RuntimeException("Order not found: " + oId);
                }

                // Prevent double completion
                if (order.getStatus().getValue().equals(String.valueOf(Status.Type.COMPLETED))) {
                    transaction.commit();
                    return;
                }



                // 🔥 Set order status to COMPLETED
                Status completedStatus = session
                        .createNamedQuery("Status.findByValue", Status.class)
                        .setParameter("value", String.valueOf(Status.Type.COMPLETED))
                        .getSingleResult();

                order.setStatus(completedStatus);
                session.merge(order);

                transaction.commit();

            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Failed to complete order: " + e.getMessage(), e);
            }
        }
    }


    private void failedOrder(String orderId) {

        int oId = Integer.parseInt(orderId.replaceAll("[^0-9]", ""));

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();

            try {

                Order order = session.find(Order.class, oId);

                if (order == null) {
                    throw new RuntimeException("Order not found: " + oId);
                }

                Status rejectedStatus = session
                        .createNamedQuery("Status.findByValue", Status.class)
                        .setParameter("value", "REJECTED")
                        .getSingleResult();

                order.setStatus(rejectedStatus);
                session.merge(order);

                transaction.commit();

            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Failed to reject order: " + e.getMessage(), e);
            }
        }
    }



}

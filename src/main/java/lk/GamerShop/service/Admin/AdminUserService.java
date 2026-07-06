package lk.GamerShop.service.Admin;

import com.google.gson.JsonObject;
import lk.GamerShop.DTO.UserDTO;
import lk.GamerShop.Entities.Status;
import lk.GamerShop.Entities.User;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class AdminUserService {

    public String getUsers(int page){
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";

        int size = 10;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<User> query = session.createQuery("from User u order by u.id ASC ", User.class);
            Long total = session.createQuery("select count(u) from User u", Long.class).uniqueResult();

            query.setFirstResult(page * size);
            query.setMaxResults(size);



            List<User> users = query.getResultList();

            List<UserDTO> userDTOS = new ArrayList<>();
            for (User user : users) {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setFirstName(user.getFirstName());
                userDTO.setLastName(user.getLastName());
                userDTO.setEmail(user.getEmail());
                userDTO.setProfileImagePath(user.getProfileImagePath());
                userDTO.setStatusValue(user.getStatus().getValue());
                userDTO.setStatusId(user.getStatus().getId());
                userDTO.setSinceAt(user.getCreatedAt().toLocalDate().toString());
                Double spent = session.createQuery("select sum(o.total) from Order o where o.user.id = :userId", Double.class)
                        .setParameter("userId", user.getId()).uniqueResult();

                if (spent == null) {
                    spent = 0.0;
                }

                userDTO.setTotalSpend(spent);
                userDTOS.add(userDTO);
            }
            status = true;
            response.add("users", AppUtil.GSON.toJsonTree(userDTOS));
            response.addProperty("totalUsers", total);
            response.addProperty("currentPage", page);
            response.addProperty("pageSize", size);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Failed to load users";
        }
        response.addProperty("message", message);
        response.addProperty("status", status);
        return AppUtil.GSON.toJson(response);

    }


    public String searchUsers(String search){
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<User> query = session.createQuery(
                    "FROM User u WHERE u.firstName LIKE :search OR u.lastName LIKE :search OR u.email LIKE :search " +
                            "ORDER BY u.id ASC ", User.class);

            query.setParameter("search", "%" + search + "%");


            List<User> users = query.getResultList();

            int total = users.size();

            List<UserDTO> userDTOS = new ArrayList<>();
            for (User user : users) {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setFirstName(user.getFirstName());
                userDTO.setLastName(user.getLastName());
                userDTO.setEmail(user.getEmail());
                userDTO.setProfileImagePath(user.getProfileImagePath());
                userDTO.setStatusValue(user.getStatus().getValue());
                userDTO.setSinceAt(user.getCreatedAt().toLocalDate().toString());
                Double spent = session.createQuery("select sum(o.total) from Order o where o.user.id = :userId", Double.class)
                        .setParameter("userId", user.getId()).uniqueResult();

                if (spent == null) {
                    spent = 0.0;
                }

                userDTO.setTotalSpend(spent);
                userDTOS.add(userDTO);
            }
            status = true;
            response.add("users",AppUtil.GSON.toJsonTree(userDTOS));
            response.addProperty("totalUsers", total);

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
}

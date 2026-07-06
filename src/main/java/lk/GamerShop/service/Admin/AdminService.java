package lk.GamerShop.service.Admin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.protobuf.StringValue;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;
import lk.GamerShop.DTO.AdminDTO;
import lk.GamerShop.DTO.UserDTO;
import lk.GamerShop.Entities.Admin;
import lk.GamerShop.Entities.Order;
import lk.GamerShop.Entities.Status;
import lk.GamerShop.Entities.User;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.Utils.HibernateUtil;
import lk.GamerShop.Validations.Validator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class AdminService {


    public String AdminLogin(String json, @Context HttpServletRequest request){
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";

        AdminDTO adminDTO = AppUtil.GSON.fromJson(json, AdminDTO.class);

        Session session = HibernateUtil.getSessionFactory().openSession();

        try{
            if (adminDTO.getUsername() == null || adminDTO.getUsername().isBlank()) {
                message = "Username is required";
            } else if (adminDTO.getPassword() == null||adminDTO.getPassword().isBlank()) {
                message = "Password is required";
            } else if (!adminDTO.getPassword().matches(Validator.PASSWORD_VALIDATION)) {
                message = "Please provide a valid password: \n" +
                        "The password must contain at least 8 characters long and include one capital letter, " +
                        "one simple letter, one digit and one special character";
            } else {
                Admin admin = session.createQuery("from Admin a where a.username=:u and a.password=:p", Admin.class)
                        .setParameter("u", adminDTO.getUsername()).setParameter("p", adminDTO.getPassword()).getSingleResultOrNull();

                if (admin == null) {
                    message = "Admin does not exist";
                } else {

                    HttpSession httpSession = request.getSession();
                    httpSession.setAttribute("admin", admin);
                    status = true;
                    response.add("admin", AppUtil.GSON.toJsonTree(adminDTO));
                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        response.addProperty("message", message);
        response.addProperty("status", status);
        return AppUtil.GSON.toJson(response);

    }




    }


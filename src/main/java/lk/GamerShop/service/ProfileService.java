package lk.GamerShop.service;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;
import lk.GamerShop.DTO.UserDTO;
import lk.GamerShop.Entities.Address;
import lk.GamerShop.Entities.City;
import lk.GamerShop.Entities.User;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.Utils.HibernateUtil;
import lk.GamerShop.Validations.Validator;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProfileService {

    public String getUserData(@Context HttpServletRequest request) {
        JsonObject responseobject = new JsonObject();
        boolean status = false;
        String message = "";

        HttpSession session = request.getSession(false);
    User user =(User)session.getAttribute("user");

        UserDTO userdto = new UserDTO();
    userdto.setId(user.getId());
        userdto.setEmail(user.getEmail());
    userdto.setPassword(user.getPassword());
    userdto.setFirstName(user.getFirstName());
    userdto.setLastName(user.getLastName());
    userdto.setMobile(user.getMobile());

    Session ses= HibernateUtil.getSessionFactory().openSession();
       try{
           List<Address> address = ses.createQuery("from Address a where user=:user ", Address.class).
                   setParameter("user",user).getResultList();
           Address primaryAddress = null;
           for (Address address1 : address) {

               primaryAddress = address1;
               break;

           }
           if (primaryAddress != null) {
               userdto.setLineOne(primaryAddress.getLineOne());
               userdto.setLineTwo(primaryAddress.getLineTwo());
               userdto.setPostalCode(primaryAddress.getPostalCode());


               userdto.setCityId(primaryAddress.getCity().getId());
               userdto.setCityName(primaryAddress.getCity().getName());
           }
           LocalDateTime createdAt = user.getCreatedAt();
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
           String sinceAt = createdAt.format(formatter);
           userdto.setSinceAt(sinceAt);

           responseobject.add("user", AppUtil.GSON.toJsonTree(userdto));
           ses.close();
           responseobject.addProperty("status", status);
           responseobject.addProperty("message", message);
           return AppUtil.GSON.toJson(responseobject);
       }catch (HibernateException h){
           h.printStackTrace();
           return null;
       }
    }


    public String getCityData(){
        JsonObject responseobject = new JsonObject();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try{
List<City> cities = session.createQuery("FROM City c ", City.class).getResultList();
responseobject.add("cities",AppUtil.GSON.toJsonTree(cities));
session.close();

return  AppUtil.GSON.toJson(responseobject);

        }catch(HibernateException h){
            h.printStackTrace();
            return null;
        }

    }

    public String updateprofile(UserDTO userDTO, @Context HttpServletRequest request) {
JsonObject responseobject = new JsonObject();
boolean status = false;
String message = "";

        if (userDTO.getFirstName() == null) {
            message = "First name is required!";
        } else if (userDTO.getFirstName().isBlank()) {
            message = "First name can not be empty!";
        } else if (userDTO.getLastName() == null) {
            message = "Last name is required!";
        } else if (userDTO.getLastName().isBlank()) {
            message = "Last name can not be empty!";
        } else if (userDTO.getLineOne() == null) {
            message = "Address line one is required!";
        } else if (userDTO.getLineOne().isBlank()) {
            message = "Address line one can not be empty!";
        } else if (userDTO.getPostalCode() != null &&
                !userDTO.getPostalCode().isBlank() &&
                !userDTO.getPostalCode().matches(Validator.POSTAL_CODE_VALIDATION)) {
            message = "Enter a valid postal code!";
        } else if (userDTO.getCityId() == 0) {
            message = "Please select a city!";
        } else if (!userDTO.getMobile().matches(Validator.MOBILE_VALIDATION)) {
            message = "Enter valid mobile number!";
        } else if (userDTO.getPassword() == null) {
            message = "Password is required!";
        } else if (userDTO.getPassword().isBlank()) {
            message = "Password can not be empty!";
        } else if (!userDTO.getPassword().matches(Validator.PASSWORD_VALIDATION)) {
            message = "Please provide valid password. \n " +
                    "The password must be at least 8 characters long and include at least one uppercase letter, " +
                    "one lowercase letter, one digit, and one special character";
        } else if (userDTO.getNewPassword() != null &&
                !userDTO.getNewPassword().isBlank() &&
                !userDTO.getNewPassword().matches(Validator.PASSWORD_VALIDATION)) {
            message = "New password is not valid. \n " +
                    "The password must be at least 8 characters long and include at least one uppercase letter, " +
                    "one lowercase letter, one digit, and one special character";
        } else if (userDTO.getConfirmPassword() != null &&
                !userDTO.getConfirmPassword().isBlank() &&
                !userDTO.getConfirmPassword().matches(Validator.PASSWORD_VALIDATION)) {
            message = "Confirm password not valid. \n " +
                    "The password must be at least 8 characters long and include at least one uppercase letter, " +
                    "one lowercase letter, one digit, and one special character";
        } else if (userDTO.getNewPassword() != null && userDTO.getConfirmPassword() != null && !userDTO.getConfirmPassword().equals(userDTO.getNewPassword())) {
            message = "New password and confirm password did not match";
        } else {
            HttpSession httpSession = request.getSession(false);
            if (httpSession == null) {
                message = "Please login first";
            } else if (httpSession.getAttribute("user") == null) {
                message = "Please login first";
            } else {
                User sessionUser = (User) httpSession.getAttribute("user");

                Session session = HibernateUtil.getSessionFactory().openSession();

                User dbuser = session.createNamedQuery("User.getbyemail", User.class).setParameter("email", sessionUser.getEmail()).uniqueResult();

                dbuser.setFirstName(userDTO.getFirstName());
                dbuser.setLastName(userDTO.getLastName());
                dbuser.setMobile(userDTO.getMobile());

                List<Address> addressList = session.createQuery("FROM Address a WHERE a.user=:user", Address.class)
                        .setParameter("user", dbuser)
                        .getResultList();

                Address currentAddress = null;
                for (Address address : addressList) {
                    if (address.getLineOne().equals(userDTO.getLineOne()) &&
                            address.getLineTwo().equals(userDTO.getLineTwo() != null ? userDTO.getLineTwo() : "") &&
                            address.getPostalCode().equals(userDTO.getPostalCode() != null ? userDTO.getPostalCode() : "") &&
                            address.getCity().getId() == userDTO.getCityId()) {
                        currentAddress = address;
                        break;
                    }
                }

                if (currentAddress == null) {
                    currentAddress = new Address();
                }

                currentAddress.setLineOne(userDTO.getLineOne());
                currentAddress.setLineTwo(userDTO.getLineTwo());
                currentAddress.setPostalCode(userDTO.getPostalCode());
                currentAddress.setUser(dbuser);

                City city = session.find(City.class, userDTO.getCityId());

                currentAddress.setCity(city);

                Transaction transaction = session.beginTransaction();
                try {
                    session.merge(dbuser);
                    session.merge(currentAddress);
                    transaction.commit();
                    httpSession.setAttribute("user", dbuser); /// update session user
                    status = true;
                    message = "Profile details update successful...";
                } catch (HibernateException e) {
                    transaction.rollback();
                    message = "Profile details update failed!";
                }

                session.close();
            }
        }
        /// profile-update-end

        responseobject.addProperty("status", status);
        responseobject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseobject);
            }
        }


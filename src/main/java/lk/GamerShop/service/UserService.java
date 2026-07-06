package lk.GamerShop.service;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.DTO.BookDTO;
import lk.GamerShop.DTO.UserDTO;
import lk.GamerShop.Entities.*;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.Utils.HibernateUtil;
import lk.GamerShop.Validations.Validator;
import lk.GamerShop.mail.ForgotPasswordMail;
import lk.GamerShop.mail.VerificationMail;
import lk.GamerShop.provider.MailServiceProvider;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class UserService {

    public String registerUser(UserDTO userDTO) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";
        System.out.println(userDTO.getPassword());
        System.out.println(userDTO.getConfirm());


        if (userDTO.getFirstName() == null || userDTO.getFirstName().isBlank()) {
            message = "First name is required";
        } else if (userDTO.getLastName() == null || userDTO.getLastName().isBlank()) {
            message = "Last name is required";
        } else if (userDTO.getEmail() == null || userDTO.getEmail().isBlank()) {
            message = "Email is required";
        } else if (!userDTO.getEmail().matches(Validator.EMAIL_VALIDATION)) {
            message = "Please provide a valid email address!";
        } else if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
            message = "Password is required";
        } else if (userDTO.getConfirm() == null || userDTO.getConfirm().isBlank()) {
            message = "Confirm Password is required";
        } else if (!userDTO.getPassword().equals(userDTO.getConfirm())) {
            message = "Password must match the Confirm Password!";
        } else if (!userDTO.getPassword().matches(Validator.PASSWORD_VALIDATION)) {
            message = "Please provide a valid password: \n" +
                    "The password must contain at least 8 characters long and include one capital letter, " +
                    "one simple letter, one digit and one special character";
        } else {
            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
            User singleuser = hibernateSession.createNamedQuery("User.getbyemail", User.class).
                    setParameter("email", userDTO.getEmail()).getSingleResultOrNull();

            if (singleuser != null) {

                message = "Email already exists as a User!";
                responseObject.addProperty("message", message);

                return responseObject.toString();
            }
            User u = new User();
            u.setFirstName(userDTO.getFirstName());
            u.setLastName(userDTO.getLastName());
            u.setEmail(userDTO.getEmail());
            u.setPassword(userDTO.getPassword());
            u.setProfileImagePath("assets/images/new_user.svg");


            String verifycode = AppUtil.generateCode();
            u.setVerificationCode(verifycode);

            Status pendingstatus = hibernateSession.createNamedQuery("Status.findByValue", Status.class).
                    setParameter("value", String.valueOf(Status.Type.PENDING)).getSingleResultOrNull();
            u.setStatus(pendingstatus);

            Transaction tr = hibernateSession.beginTransaction();

            try {
                hibernateSession.persist(u);
                tr.commit();
                VerificationMail verificationMail = new VerificationMail(u.getEmail(), verifycode);
                MailServiceProvider.getInstance().sendMail(verificationMail);
                responseObject.addProperty("verificationCode", verifycode);
                status = true;
                message = "Account creation successfully. Verification code has sent to your email";


            } catch (HibernateException e) {
                tr.rollback();
                message = "Account creation failed. Please try again!";
                e.printStackTrace();
            }


        }
        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }

    public String verifyaccount(UserDTO userDTO) {

        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";
        if (userDTO.getEmail() == null) {
            message = "Email is required";
        } else if (userDTO.getEmail().isBlank()) {
            message = "Email can not be empty or blank";
        } else if (!userDTO.getEmail().matches(Validator.EMAIL_VALIDATION)) {
            message = "Please provide a valid email address!";
        } else if (userDTO.getVerificationCode() == null) {
            message = "Verification code is required";
        } else if (userDTO.getVerificationCode().isBlank()) {
            message = "Verification code can not be empty";
        } else if (!userDTO.getVerificationCode().matches(Validator.VERIFICATION_CODE_VALIDATION)) {
            message = "Please provide a valid verification code! Verification code must have 6 digits";
        } else {
            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

            User user = hibernateSession.createQuery("from User u WHERE u.email=:email AND u.verificationCode=:code", User.class).
                    setParameter("email", userDTO.getEmail()).
                    setParameter("code", userDTO.getVerificationCode()).getSingleResultOrNull();

            if (user == null) {
                message = "Account not found. Please register first";

            } else {
                Status verify = hibernateSession.createNamedQuery("Status.findByValue", Status.class).
                        setParameter("value", String.valueOf(Status.Type.VERIFIED)).getSingleResultOrNull();

                Status blocked = hibernateSession.createNamedQuery("Status.findByValue", Status.class).
                        setParameter("value", String.valueOf(Status.Type.BLOCKED)).getSingleResultOrNull();

                if (user.getStatus().equals(verify)) {
                    message = "Account already verified!";
                } else if (user.getStatus().equals(blocked)) {
                    message = "Your Account has been blocked by Admins!";
                } else {
                    user.setStatus(verify);
                    Transaction tr = hibernateSession.beginTransaction();
                    try {
                        hibernateSession.merge(user);
                        tr.commit();
                        status = true;
                        message = "Account verified successfully!";
                    } catch (HibernateException e) {
                        tr.rollback();
                        message = "Something went wrong. Verification process failed!";
                        e.printStackTrace();
                    }
                    hibernateSession.close();
                }

            }


        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }

    public String loginUser(UserDTO userDTO, @Context HttpServletRequest request) {
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";
        boolean pending = false;


        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = hibernateSession.beginTransaction();

        try {
            if (userDTO.getEmail() == null || userDTO.getEmail().isBlank()) {
                message = "Email is required";
            } else if (!userDTO.getEmail().matches(Validator.EMAIL_VALIDATION)) {
                message = "Please provide a valid email address!";
            } else if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
                message = "Password is required";
            } else if (!userDTO.getPassword().matches(Validator.PASSWORD_VALIDATION)) {
                message = "Please provide a valid password: \n" +
                        "The password must contain at least 8 characters long and include one capital letter, " +
                        "one simple letter, one digit and one special character";
            } else {

                User singleuser = hibernateSession.createNamedQuery("User.getbyemail", User.class).
                        setParameter("email", userDTO.getEmail()).getSingleResultOrNull();
                if (singleuser == null) {
                    message = "Account not found";

                } else {
                    if (userDTO.getPassword().equals(singleuser.getPassword())) {
                        Status singlestatus = hibernateSession.createNamedQuery("Status.findByValue", Status.class).
                                setParameter("value", String.valueOf(Status.Type.VERIFIED)).getSingleResult();
                        Status pendingstatus = hibernateSession.createNamedQuery("Status.findByValue", Status.class).
                                setParameter("value", String.valueOf(Status.Type.PENDING)).getSingleResult();

                        if (singlestatus.equals(singleuser.getStatus())) {
                            HttpSession httpSession = request.getSession();
                            Map<Integer, BookDTO> session_cart = (Map<Integer, BookDTO>) httpSession.getAttribute("session_cart");

                            if (session_cart != null) {
                                Cart cart = hibernateSession.createQuery("FROM Cart c WHERE c.user=:user", Cart.class).
                                        setParameter("user", singleuser).getSingleResultOrNull();

                                if (cart == null) {
                                    cart = new Cart();
                                    cart.setUser(singleuser);
                                    cart.setItems(new HashSet<>());
                                    hibernateSession.persist(cart);


                                }


                                Map<Integer, BookDTO> cartitems = (Map<Integer, BookDTO>) httpSession.getAttribute("session_cart");

                                if (cartitems != null) {
                                    for (BookDTO book : cartitems.values()) {

                                        Book dbbook = hibernateSession.get(Book.class, book.getId());


                                        CartItems existingitems = hibernateSession.createQuery("FROM CartItems c WHERE c.cart=:cart AND c.book=:book", CartItems.class).
                                                setParameter("cart", cart).
                                                setParameter("book", dbbook).getSingleResultOrNull();

                                        if (existingitems == null) {
                                            CartItems items = new CartItems();
                                            items.setCart(cart);
                                            items.setBook(dbbook);
                                            items.setPriceAtTime(book.getPrice());
                                            cart.getItems().add(items);

                                        }
                                    }

                                    tr.commit();
                                }


                            }

                            httpSession.setAttribute("user", singleuser);
                            status = true;
                            message = "Logged in successfully!";
                        } else if (pendingstatus.equals(singleuser.getStatus())) {
                            message = "your account is not verified, Please verify first";
                            pending = true;
                            String verifycode = AppUtil.generateCode();
                            singleuser.setVerificationCode(verifycode);
                            hibernateSession.merge(singleuser);
                            tr.commit();
                            VerificationMail verificationMail = new VerificationMail(singleuser.getEmail(), verifycode);
                            MailServiceProvider.getInstance().sendMail(verificationMail);
                            response.addProperty("verificationCode", verifycode);

                            response.addProperty("pending",pending);
                        }else{
                            message = "your account has been Blocked, Please Contact an Admin.";

                        }

                    } else {
                        message = "Invalid Login Credentials.";
                    }

                }


            }
            response.addProperty("status", status);
            response.addProperty("message", message);
            return AppUtil.GSON.toJson(response);
        } catch (HibernateException e) {
            e.printStackTrace();
            tr.rollback();
            return e.getMessage();
        }
    }


    public String ForgotPassword(UserDTO dto) {
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";


        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = hibernateSession.beginTransaction();


        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            message = "Email is required";
        } else {

            User user = hibernateSession.createNamedQuery("User.getbyemail", User.class)
                    .setParameter("email", dto.getEmail()).getSingleResultOrNull();

            if (user == null) {
                message = "No user found from this email";
            } else {
                String verifycode = AppUtil.generateCode();
                user.setVerificationCode(verifycode);
                hibernateSession.merge(user);
                tr.commit();

                ForgotPasswordMail forgotPasswordMail = new ForgotPasswordMail(user.getEmail(), verifycode);
                MailServiceProvider.getInstance().sendMail(forgotPasswordMail);
                System.out.println("email sent");
                response.addProperty("verificationCode", verifycode);
                status = true;
                message = "Verification code has been sent to your email";
            }


        }
        hibernateSession.close();
        response.addProperty("status", status);
        response.addProperty("message", message);
        return AppUtil.GSON.toJson(response);


    }


    public String searchBook(String title, Integer genreId, String author, Double minPrice, Double maxPrice,
                             int page, int size) {


        JsonObject jsonObject = new JsonObject();
        boolean status = false;
        String message = "";
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Status activestatus = session.createNamedQuery("Status.findByValue", Status.class).
                    setParameter("value", String.valueOf(Status.Type.ACTIVE)).getSingleResultOrNull();

            StringBuilder hql = new StringBuilder("from Book b WHERE b.status=:status ");

//       if(minPrice != null){
//           if(minPrice > 0 || minPrice < maxPrice){
//
//           }else{
//               message="Invalid Minimum Price";
//               jsonObject.addProperty("message",message);
//               return AppUtil.GSON.toJson(jsonObject);
//           }
//
//       }
//       if(maxPrice != null) {
//           if (maxPrice > 0 || minPrice < maxPrice) {
//
//           } else {
//               message = "Invalid Maximum Price";
//               jsonObject.addProperty("message", message);
//               return AppUtil.GSON.toJson(jsonObject);
//           }
//       }

            if (title != null && !title.trim().isEmpty()) {
                hql.append(" AND LOWER(b.title) LIKE :title");
            }
            if (author != null && !author.trim().isEmpty()) {
                hql.append(" AND LOWER(b.author) LIKE :author");
            }
            if (genreId != null) {
                hql.append(" AND b.genre.id = :genreId");
            }
            if (minPrice != null) {
                hql.append(" AND b.price >= :minPrice");
            }
            if (maxPrice != null) {
                hql.append(" AND b.price <= :maxPrice");
            }

            hql.append(" ORDER BY b.createdAt DESC");


            Query<Book> query = session.createQuery(hql.toString(), Book.class);
            query.setParameter("status", activestatus);

            if (title != null && !title.trim().isEmpty()) {
                query.setParameter("title", "%" + title.toLowerCase() + "%");
            }
            if (author != null && !author.trim().isEmpty()) {
                query.setParameter("author", "%" + author.toLowerCase() + "%");
            }
            if (genreId != null) {
                query.setParameter("genreId", genreId);
            }
            if (minPrice != null) {
                query.setParameter("minPrice", minPrice);
            }
            if (maxPrice != null) {
                query.setParameter("maxPrice", maxPrice);
            }

            query.setFirstResult(page * size);
            query.setMaxResults(size);

            List<Book> books = query.getResultList();

            List<BookDTO> bookDTOS = new ArrayList<>();
            for (Book book : books) {
                BookDTO dto = new BookDTO();
                dto.setId(book.getId());
                dto.setTitle(book.getTitle());
                dto.setAuthor(book.getAuthor().getName());
                dto.setPrice(book.getPrice());
                dto.setCoverImagePath(book.getCoverimagepath());
                bookDTOS.add(dto);
            }

            jsonObject.add("books", AppUtil.GSON.toJsonTree(bookDTOS));
            jsonObject.addProperty("page", page);
            jsonObject.addProperty("size", size);

            session.close();

        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return AppUtil.GSON.toJson(jsonObject);
    }


    public String changepassword(UserDTO userDTO) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";
        System.out.println(userDTO.getPassword());
        System.out.println(userDTO.getConfirm());
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = hibernateSession.beginTransaction();
        try {


            if (userDTO.getVerificationCode() == null || userDTO.getVerificationCode().isBlank()) {
                message = "Verification Code is required";
            } else if (userDTO.getEmail() == null || userDTO.getEmail().isBlank()) {
                message = "Email is required";
            } else if (!userDTO.getEmail().matches(Validator.EMAIL_VALIDATION)) {
                message = "Please provide a valid email address!";
            } else if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
                message = "New Password is required";
            } else if (userDTO.getConfirm() == null || userDTO.getConfirm().isBlank()) {
                message = "Confirm Password is required";
            } else if (!userDTO.getPassword().equals(userDTO.getConfirm())) {
                message = "Password must match the Confirm Password!";
            } else if (!userDTO.getPassword().matches(Validator.PASSWORD_VALIDATION)) {
                message = "Please provide a valid password: \n" +
                        "The password must contain at least 8 characters long and include one capital letter, " +
                        "one simple letter, one digit and one special character";
            } else {

                User singleuser = hibernateSession.createNamedQuery("User.getbyemail", User.class).
                        setParameter("email", userDTO.getEmail()).getSingleResultOrNull();

                if (singleuser == null) {
                    message = "Email does not exist";
                } else {
                    if (singleuser.getVerificationCode().equals(userDTO.getVerificationCode())) {
                        singleuser.setPassword(userDTO.getPassword());
                        hibernateSession.merge(singleuser);
                        transaction.commit();
                        status = true;
                        message = "Password has been changed successfully";


                    } else {
                        message = "Verification Code do not match";
                    }
                }



            }
        } catch (HibernateException e) {
            transaction.rollback();
            message = "Account creation failed. Please try again!";
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }



    public String getUser(@Context HttpServletRequest request) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        HttpSession session = request.getSession(false);
        Session HibernateSession = HibernateUtil.getSessionFactory().openSession();

     try{
         if (session == null || session.getAttribute("user") == null) {
             message= "Please Login First to view Profile";
         }else{
             User user = (User) session.getAttribute("user");
             User SingleUser = HibernateSession.createNamedQuery("User.getbyemail",User.class )
                     .setParameter("email", user.getEmail()).getSingleResultOrNull();

             UserDTO dto = new UserDTO();
             dto.setId(SingleUser.getId());
             dto.setFirstName(SingleUser.getFirstName());
             dto.setLastName(SingleUser.getLastName());
             dto.setEmail(SingleUser.getEmail());
             dto.setProfileImagePath(SingleUser.getProfileImagePath());
             dto.setMobile(SingleUser.getMobile());
             dto.setSinceAt(SingleUser.getCreatedAt().toLocalDate().toString());

             responseObject.add("user",AppUtil.GSON.toJsonTree(dto));
             status = true;
             message = "User loaded Successfully";
         }

     }catch(Exception e){
         e.printStackTrace();
     }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }


    public String updateUser(@Context HttpServletRequest request, UserDTO dto, InputStream imageInputStream, FormDataContentDisposition imageDetails) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        HttpSession session = request.getSession(false);
        Session HibernateSession = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction= HibernateSession.beginTransaction();

        try{
            if (session == null || session.getAttribute("user") == null) {
                message= "Please Login First to view Profile";
            }else{

                if (dto.getFirstName() == null || dto.getFirstName().isBlank()) {
                    message = "First name is required";
                } else if (dto.getLastName() == null || dto.getLastName().isBlank()) {
                    message = "Last name is required";
                } else if (dto.getMobile() == null || dto.getMobile().isBlank()) {
                    message = "Mobile is required";
                }else if( !dto.getMobile().matches(Validator.MOBILE_VALIDATION)){
                    message = "Enter Proper Mobile Number";


                }else{
                    
                    
                    
                    User user = (User) session.getAttribute("user");
                    User SingleUser = HibernateSession.createNamedQuery("User.getbyemail",User.class )
                            .setParameter("email", user.getEmail()).getSingleResultOrNull();

                    SingleUser.setFirstName(dto.getFirstName());
                    SingleUser.setLastName(dto.getLastName());
                    SingleUser.setMobile(dto.getMobile());
                    if (imageDetails != null) {

                        String extension = imageDetails.getFileName().substring(imageDetails.getFileName().lastIndexOf("."));

                        String fileName = "user-" + SingleUser.getId() + "-" + System.currentTimeMillis() + extension;

                        String uploadPath = request.getServletContext().getRealPath("/") + "assets/images/users/";

                        File folder = new File(uploadPath);

                        if (!folder.exists()) {
                            folder.mkdirs();
                        }

                        Files.copy(imageInputStream, Paths.get(uploadPath + fileName), StandardCopyOption.REPLACE_EXISTING
                        );

                        SingleUser.setProfileImagePath("assets/images/users/" + fileName);
                    }
                    HibernateSession.merge(SingleUser);
                    transaction.commit();

                    status = true;
                    message = "User Updated Successfully";
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }



    public String Userlogout(@Context HttpServletRequest request) {

        JsonObject response = new JsonObject();

        boolean status = false;
        String message = "";

        try {

            HttpSession session = request.getSession(false);

            if (session != null) {

                session.invalidate();

                status = true;
                message = "Logged out successfully";

            } else {

                message = "No active Users found";
            }

        } catch (Exception e) {

            e.printStackTrace();

            message = "Logout failed";
        }

        response.addProperty("status", status);
        response.addProperty("message", message);

        return AppUtil.GSON.toJson(response);
    }
}
















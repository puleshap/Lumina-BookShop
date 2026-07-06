package lk.GamerShop.service.Admin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.GamerShop.DTO.AuthorDTO;
import lk.GamerShop.Entities.Author;
import lk.GamerShop.Entities.Book;
import lk.GamerShop.Entities.Status;
import lk.GamerShop.Entities.User;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class AdminAuthorService {





    public String getAuthors(int page){
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";

        int size = 10;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Author> query = session.createQuery("from Author a order by a.id ASC ", Author.class);
            Long total = session.createQuery("select count(a) from Author a", Long.class).uniqueResult();

            query.setFirstResult(page * size);
            query.setMaxResults(size);



            List<Author> authors = query.getResultList();

            List<AuthorDTO> authorDTOS = new ArrayList<>();
            for (Author author : authors) {
                AuthorDTO authorDTO = new AuthorDTO();
                authorDTO.setId(author.getId());
                authorDTO.setName(author.getName());
                authorDTO.setAddedDate(author.getCreatedAt().toLocalDate().toString());

                List<Book> books = session.createQuery("from Book b where b.author=:author",Book.class)
                        .setParameter("author",author).getResultList();
                authorDTO.setBookCount(books.size());


                authorDTOS.add(authorDTO);
            }
            status = true;
            response.add("authors",AppUtil.GSON.toJsonTree(authorDTOS));
            System.out.println(AppUtil.GSON.toJsonTree(authorDTOS));
            response.addProperty("totalAuthors", total);
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

    public String addAuthor(String json){
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";


        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx =session.beginTransaction();
        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            Author author = new  Author();

            if(jsonObject.get("name") == null){
                message="Please enter Author's Name";

            } else {
                Author au = session.createQuery("from Author a where a.name=:name", Author.class)
                        .setParameter("name", jsonObject.get("name").getAsString()).getSingleResultOrNull();
                if (au == null) {
                    author.setName(jsonObject.get("name").getAsString());
                    session.persist(author);
                    tx.commit();
                    status = true;
                }else{
                    message="Author Already Exists!";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            message ="Failed to add Author";
        }
        response.addProperty("message", message);
        response.addProperty("status", status);
        return AppUtil.GSON.toJson(response);

    }


    public String searchAuthors(String search){
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {

            List<Author> authors = session.createQuery("FROM Author a WHERE a.name LIKE :search ORDER BY a.id ASC ", Author.class)
                    .setParameter("search", "%" + search + "%").getResultList();

            int total = authors.size();

            List<AuthorDTO> authorDTOS = new ArrayList<>();
            for (Author author : authors) {
                List<Book> books = author.getBooks();
                System.out.println(books);
                System.out.println(books.size());
                AuthorDTO authorDTO = new AuthorDTO();
                authorDTO.setId(author.getId());
                authorDTO.setName(author.getName());
                authorDTO.setAddedDate(author.getCreatedAt().toLocalDate().toString());


                authorDTO.setBookCount(books.size());
                authorDTOS.add(authorDTO);
            }
            status = true;
            response.add("authors",AppUtil.GSON.toJsonTree(authorDTOS));
            response.addProperty("totalAuthors", total);

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


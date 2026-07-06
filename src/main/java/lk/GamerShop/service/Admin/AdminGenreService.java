package lk.GamerShop.service.Admin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lk.GamerShop.DTO.AuthorDTO;
import lk.GamerShop.DTO.GenreDTO;
import lk.GamerShop.Entities.*;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class AdminGenreService {





    public String getGenres(int page){
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";

        int size = 5;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Genre> query = session.createQuery("from Genre a order by a.id ASC ", Genre.class);
            Long total = session.createQuery("select count(a) from Genre a", Long.class).uniqueResult();

            query.setFirstResult(page * size);
            query.setMaxResults(size);



            List<Genre> genres = query.getResultList();

            List<GenreDTO> genreDTOS = new ArrayList<>();
            for (Genre genre : genres) {
                GenreDTO genreDTO = new GenreDTO();
                genreDTO.setId(genre.getId());
                genreDTO.setName(genre.getName());
                genreDTO.setCreatedAt(genre.getCreatedAt().toLocalDate().toString());

                List<Book> books = session.createQuery("from Book b where b.genre=:genre",Book.class)
                        .setParameter("genre",genre).getResultList();
                genreDTO.setBookCount(books.size());


                genreDTOS.add(genreDTO);
            }
            status = true;
            response.add("genres",AppUtil.GSON.toJsonTree(genreDTOS));
            System.out.println(AppUtil.GSON.toJsonTree(genreDTOS));
            response.addProperty("totalGenres", total);
            response.addProperty("currentPage", page);
            response.addProperty("pageSize", size);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Failed to load Genres";
        }
        response.addProperty("message", message);
        response.addProperty("status", status);
        return AppUtil.GSON.toJson(response);

        }

    public String AddGenre(String json){
        JsonObject response = new JsonObject();
        boolean status = false;
        String message = "";


        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx =session.beginTransaction();
        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            Genre genre = new  Genre();
            String name =  jsonObject.get("name").getAsString().trim();
            if(name==null || name.isBlank()){
                message="Please enter Genre Name";

            } else {
                Genre ge = session.createQuery("from Genre a where a.name=:name", Genre.class)
                        .setParameter("name", name).getSingleResultOrNull();
                if (ge == null) {
                    genre.setName(jsonObject.get("name").getAsString());
                    session.persist(genre);
                    tx.commit();
                    status = true;
                }else{
                    message="Genre Already Exists!";
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


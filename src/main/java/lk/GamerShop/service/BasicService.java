package lk.GamerShop.service;

import com.google.gson.JsonObject;
import lk.GamerShop.DTO.AuthorDTO;
import lk.GamerShop.DTO.GenreDTO;
import lk.GamerShop.Entities.Author;
import lk.GamerShop.Entities.Genre;
import lk.GamerShop.Entities.Status;
import lk.GamerShop.Utils.AppUtil;
import lk.GamerShop.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class BasicService {

    public String getcategories(){
        JsonObject response = new JsonObject();
        boolean status = true;
        String message = "";

        Session session = HibernateUtil.getSessionFactory().openSession();
return message;
    }



    public String getgenres(){
        JsonObject response = new JsonObject();


        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Genre> genres =session.createQuery("FROM Genre g", Genre.class).getResultList();
            List<GenreDTO> genreDTOS =new ArrayList<GenreDTO>();
            for(Genre genre:genres){
                GenreDTO genreDTO = new GenreDTO();
                genreDTO.setId(genre.getId());
                genreDTO.setName(genre.getName());
                genreDTOS.add(genreDTO);

            }


            session.close();

            response.add("genres",AppUtil.GSON.toJsonTree(genreDTOS));

        }catch(HibernateException e){
            e.printStackTrace();
            return "Error";
        }



        return AppUtil.GSON.toJson(response);
    }

    public String getauthors(){
        JsonObject response = new JsonObject();


        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Author> authors =session.createQuery("FROM Author a", Author.class).getResultList();
            List<AuthorDTO> authorDTOS =new ArrayList<AuthorDTO>();
            for(Author author :authors){
                AuthorDTO authorDTO = new AuthorDTO();
                authorDTO.setId(author.getId());
                authorDTO.setName(author.getName());
                authorDTOS.add(authorDTO);

            }


            session.close();

            response.add("authors",AppUtil.GSON.toJsonTree(authorDTOS));

        }catch(HibernateException e){
            e.printStackTrace();
            return "Error";
        }



        return AppUtil.GSON.toJson(response);
    }




    public String getstatus(){
        JsonObject response = new JsonObject();


        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Status> statuses =session.createQuery("FROM Status s", Status.class).getResultList();

            session.close();

            response.add("statuses",AppUtil.GSON.toJsonTree(statuses));

        }catch(HibernateException e){
            e.printStackTrace();
            return "Error";
        }



        return AppUtil.GSON.toJson(response);
    }

    public String adminbookstatus(){
        JsonObject response = new JsonObject();


        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            Status status1 = session.createNamedQuery("Status.findByValue", Status.class).
                    setParameter("value", String.valueOf(Status.Type.ACTIVE)).getSingleResultOrNull();
            Status status2 = session.createNamedQuery("Status.findByValue", Status.class).
                    setParameter("value", String.valueOf(Status.Type.BLOCKED)).getSingleResultOrNull();

            List<Status> statuses =new ArrayList<Status>();
            statuses.add(status1);
            statuses.add(status2);

            session.close();

            response.add("statuses",AppUtil.GSON.toJsonTree(statuses));

        }catch(HibernateException e){
            e.printStackTrace();
            return "Error";
        }



        return AppUtil.GSON.toJson(response);
    }

    public String adminorderstatus(){
        JsonObject response = new JsonObject();


        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            Status status1 = session.createNamedQuery("Status.findByValue", Status.class).
                    setParameter("value", String.valueOf(Status.Type.COMPLETED)).getSingleResultOrNull();
            Status status2 = session.createNamedQuery("Status.findByValue", Status.class).
                    setParameter("value", String.valueOf(Status.Type.PENDING)).getSingleResultOrNull();

            List<Status> statuses =new ArrayList<Status>();
            statuses.add(status1);
            statuses.add(status2);

            session.close();

            response.add("statuses",AppUtil.GSON.toJsonTree(statuses));

        }catch(HibernateException e){
            e.printStackTrace();
            return "Error";
        }



        return AppUtil.GSON.toJson(response);
    }

}


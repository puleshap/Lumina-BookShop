package lk.GamerShop.Entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "library")
public class Library extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book ebook;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getEbook() {
        return ebook;
    }

    public void setEbook(Book ebook) {
        this.ebook = ebook;
    }
}

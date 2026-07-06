package lk.GamerShop.Entities;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "gender")
@NamedQuery(name = "findAll",query = "SELECT g")
public class Gender implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "gender_name", nullable = false)
    private String genderName;

    public Gender() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }
}


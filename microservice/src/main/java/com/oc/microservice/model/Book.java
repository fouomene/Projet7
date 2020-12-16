package com.oc.microservice.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Length(min = 3,max = 100,message = "Le titre doit être compris entre 3 et 100 caractères.")
    private String title;
    @Length(min = 3,max = 45,message = "L'auteur doit être compris entre 3 et 100 caractères.")
    private String autor;
    private int nbTotal;
    private int nbRemaining;
    @Length(max = 500,message = "Le lien est trop grand (max : 500 caractères).")
    private String image;

    // to solve issue: foreignkey mal formated while importing db
    @OneToMany(mappedBy = "book")
    private Set<Loan> loans;

    public Book() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getNbTotal() {
        return nbTotal;
    }
    public void setNbTotal(int nbTotal) {
        this.nbTotal = nbTotal;
    }

    public int getNbRemaining() {
        return nbRemaining;
    }
    public void setNbRemaining(int nbRemaining) {
        this.nbRemaining = nbRemaining;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", autor='" + autor + '\'' +
                ", nbTotal=" + nbTotal +
                ", nbRemaining=" + nbRemaining +
                '}';
    }
}

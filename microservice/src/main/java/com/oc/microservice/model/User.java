package com.oc.microservice.model;
import com.oc.microservice.enums.Role;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Length(min = 3,max = 100,message = "Le nom doit être compris entre 3 et 100 caractères.")
    private String name;
    @Length(min = 3,max = 45,message = "Le prénom doit être compris entre 3 et 100 caractères.")
    private String firstname;
    @Length(min = 3,max = 100,message = "Le mail doit être compris entre 3 et 100 caractères.")
    private String mail;
    @Length(min = 3,max = 45,message = "Le pseudo doit être compris entre 3 et 100 caractères.")
    private String username;
    @Length(min = 3,max = 45,message = "Le mot de passe doit être compris entre 3 et 100 caractères.")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(length = 13)
    private Role role;

    // to solve issue: 'foreignkey mal formated' while importing db
    @OneToMany(mappedBy = "user")
    private Set<Loan> loans;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firstname='" + firstname + '\'' +
                ", mail='" + mail + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}

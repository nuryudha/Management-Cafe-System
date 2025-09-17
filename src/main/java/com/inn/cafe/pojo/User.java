package com.inn.cafe.pojo;

import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "User.findByEmailId",query = "SELECT u FROM User u WHERE u.email=:email")

@NamedQuery(name = "User.getAllUser",query = "SELECT new com.inn.cafe.wrapper.UserWrapper(u.id, u.name, u.email, u.contactNumber, u.status) " +
        "FROM User u WHERE u.role = 'user'")

@NamedQuery(name = "User.updateStatus",query = "UPDATE User u SET u.status=:status WHERE u.id=:id")

@NamedQuery(name = "User.getAllAdmin",query = "SELECT u.email " +
        "FROM User u WHERE u.role = 'admin'")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L; // mksdny apa ? bedanya sama kalau yang biasanya

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;

}

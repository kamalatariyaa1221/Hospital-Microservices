package com.hospitalDepartment.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class InvalidatedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Date expirationDate;


}
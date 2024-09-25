package com.patient.payload;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
public class Doctor {

    private Long doctorId;


    private String name;


    private int yearOfexperience;

    private String email;
   private String speciality;


    private long departmentId;


}

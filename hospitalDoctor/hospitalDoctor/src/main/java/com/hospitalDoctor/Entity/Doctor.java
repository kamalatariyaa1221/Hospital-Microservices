package com.hospitalDoctor.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @NotEmpty(message = "Doctor Name must not be empty")
    private String doctorName;

    @NotEmpty(message = "doctor Specialization is required")
    private String specialization;

    @NotEmpty(message = "Email id is required")
    private String emailId;

    private Long departmentId;


}

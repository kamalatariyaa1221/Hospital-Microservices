package com.patient.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {


    private long patientId;

    private String patientName;


    private String phoneNumber;

    private Date dob;

    private String address;

    private long departmentId;

    private long doctorId;
}



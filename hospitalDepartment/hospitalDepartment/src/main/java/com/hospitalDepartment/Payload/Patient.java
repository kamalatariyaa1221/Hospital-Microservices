package com.hospitalDepartment.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Patient {

    private Long patientId;
    private String patientName;
    private String phoneNumber;
    private Date dob;
    private String address;
    private String status;
}

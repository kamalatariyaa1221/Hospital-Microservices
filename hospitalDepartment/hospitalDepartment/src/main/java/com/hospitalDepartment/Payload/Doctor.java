package com.hospitalDepartment.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    private Long doctorId;
    private String doctorName;
    private String specialization;
    private String emailId;
    private Long departmentId;
}

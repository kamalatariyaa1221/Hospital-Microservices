package com.hospitalDoctor.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DoctorDto {
    private Long doctorId;
    private String doctorName;
    private String specialization;
    private String emailId;
    private Long departmentId;
}

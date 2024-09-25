package com.hospitalDepartment.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DepartmentWithPatientDto {
    private Long departmentId;
    private String departmentName;
    List<Patient> patientList = new ArrayList<>();

}

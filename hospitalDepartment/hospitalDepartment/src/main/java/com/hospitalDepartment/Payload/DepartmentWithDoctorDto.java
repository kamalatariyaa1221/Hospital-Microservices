package com.hospitalDepartment.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentWithDoctorDto {
    private Long departmentId;
    private String departmentName;
    List<Doctor> doctorList = new ArrayList<>();


}

package com.hospitalDepartment.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentWithStaffDto {

    private Long departmentId;
    private String departmentName;
    List<Staff> staffList = new ArrayList<>();
}

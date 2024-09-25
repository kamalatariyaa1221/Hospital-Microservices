package com.hospitalDepartment.Payload;

import jakarta.persistence.Entity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DepartmentDto {
    private Long departmentId;

    private String departmentName;

}

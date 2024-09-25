package com.hospitalDepartment.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Staff {
    private Long staffId;
    private String staffName;
    private  String position;
    private String gender;
    private String status;


}

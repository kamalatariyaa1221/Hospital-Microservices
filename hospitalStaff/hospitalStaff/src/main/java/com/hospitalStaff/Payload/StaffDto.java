package com.hospitalStaff.Payload;

import com.hospitalStaff.Entity.checkStaff;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class StaffDto {
    private Long staffId;

    private String staffName;

    private  String position;

    private String gender;

    private checkStaff status;

    private Long departmentId;
}

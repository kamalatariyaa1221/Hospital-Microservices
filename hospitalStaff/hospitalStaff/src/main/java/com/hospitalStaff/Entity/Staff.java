package com.hospitalStaff.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;

    @Size(min = 5, max = 20, message = "Name is too long")
    @NotEmpty(message = "staff name is required")
    private String staffName;

    @Size(min = 5, max = 20, message = "Positon is too long")
    @NotEmpty(message = "Staff position is required")
    private  String position;

    @Size(min = 5, max = 20, message = "gender is too long")
    @NotEmpty
    private String gender;

    @Enumerated(EnumType.STRING)
    private checkStaff status;

    private Long departmentId;
    private String departmentName;
}

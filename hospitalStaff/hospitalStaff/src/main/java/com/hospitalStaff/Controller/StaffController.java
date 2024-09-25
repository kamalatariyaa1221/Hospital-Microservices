package com.hospitalStaff.Controller;

import com.hospitalStaff.Payload.StaffDto;
import com.hospitalStaff.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    //  add a staff-->

    @PostMapping("/addStaffByDepartmentId/Department/{departmentId}")
    // http://localhost:8083/api/v1/staff/addStaffByDepartmentId/Department/{departmentId}
    public ResponseEntity<StaffDto> addStaff(@RequestBody StaffDto staffDto, @PathVariable Long departmentId, @RequestHeader("Authorization") String authHeader){
        String jwtToken = authHeader.substring(7);
        StaffDto staffDto1 = staffService.addStaff(staffDto, departmentId,jwtToken);
        return new ResponseEntity<>(staffDto1,HttpStatus.CREATED);
    }

    // get all staff records by their department id without using restTemplate-->

    @GetMapping("/getAllStaffByDepartment/department/{departmentId}")
    //   http://localhost:8083/api/v1/staff/getAllStaffByDepartment/department/{departmentId}
    public ResponseEntity<List<StaffDto>> getAllStaffByDepartment(@PathVariable Long departmentId){
        List<StaffDto> savedStaff = staffService.getAllStaffByDepartment(departmentId);
        return new ResponseEntity<>(savedStaff,HttpStatus.OK);
    }

    //update staff from busy to free-->

    @PatchMapping("/getReleaseStaff/staff/{staffId}")
    //  http://localhost:8083/api/v1/staff/getReleaseStaff/staff/{staffId}
    public ResponseEntity<StaffDto>releaseStaff(@PathVariable long staffId){
       StaffDto resultDto = staffService.releaseStaff(staffId);
        return new ResponseEntity<>(resultDto,HttpStatus.OK);
    }

    //update staff from free to busy-->

    @PatchMapping("/getfreeToBusyStaff/staff/{staffId}")
    // http://localhost:8083/api/v1/staff/getfreeToBusyStaff/staff/staffId
    public ResponseEntity<StaffDto>freeToBusyStaff(@PathVariable long staffId){
        StaffDto resultDto = staffService.freetoBusyStaff(staffId);
        return new ResponseEntity<>(resultDto,HttpStatus.OK);
    }
}

package com.hospitalDepartment.Controller;

import com.hospitalDepartment.Entity.Department;
import com.hospitalDepartment.Payload.DepartmentDto;
import com.hospitalDepartment.Payload.DepartmentWithDoctorDto;
import com.hospitalDepartment.Payload.DepartmentWithPatientDto;
import com.hospitalDepartment.Payload.DepartmentWithStaffDto;
import com.hospitalDepartment.Service.DepartmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // create a new department-->

    @PostMapping("/createDepartment")
    //  http://localhost:8081/api/v1/department/createDepartment
    public ResponseEntity<DepartmentDto>addDepartment(@RequestBody DepartmentDto departmentDto){
        DepartmentDto departmentDto1 = departmentService.addDepartment(departmentDto);
        return  new ResponseEntity<>(departmentDto1, HttpStatus.CREATED);
    }

    //  return a department by departmentId-->

    @GetMapping("/getAllDepartmentById/department/{departmentId}")
    //  http://localhost:8081/api/v1/department/getAllDepartmentById/department/1
    public ResponseEntity<Department> getAllDepartmentById(@PathVariable Long departmentId){
        Department departmentDto = departmentService.getAllDepartmentById(departmentId);
        return new ResponseEntity<>(departmentDto,HttpStatus.OK);
    }

    // return all doctor with respective department-->

    @GetMapping("/getAllDepartmentWithDoctor/department/{departmentId}")
    //  http://localhost:8081/api/v1/department/getAllDepartmentWithDoctor/department/{departmentId}
    public ResponseEntity<DepartmentWithDoctorDto> getAllDepartmentWithDoctor(@PathVariable Long departmentId, @RequestHeader("Authorization") String authHeader){
        String jwtToken = authHeader.substring(7);
        DepartmentWithDoctorDto departmentDoctorList = departmentService.getAllDepartmentWithDoctor(departmentId,jwtToken);
        return new ResponseEntity<>(departmentDoctorList,HttpStatus.OK);
    }

    // return all staff with respective department-->

    @GetMapping("/getDepartmentWithStaff/department/{departmentId}")
    //  http://localhost:8081/api/v1/department/getDepartmentWithStaff/department/{departmentId}
    public ResponseEntity<DepartmentWithStaffDto>getDepartmentWithStaff(@PathVariable Long departmentId,@RequestHeader("Authorization") String authHeader){
        String jwtToken  = authHeader.substring(7);
        DepartmentWithStaffDto DepartmentwithStaffDto = departmentService.getDepartmentWithStaff(departmentId,jwtToken);
        return new ResponseEntity<>(DepartmentwithStaffDto,HttpStatus.OK);
    }

    // return all patient records with respective department-->

    @GetMapping("/getDepartmentWithPatient/department/{departmentId}")
    //  http://localhost:8081/api/v1/department/getDepartmentWithPatient/department/{departmentId}
    public ResponseEntity<DepartmentWithPatientDto>getDepartmentWithPatient(@PathVariable long departmentId){
        DepartmentWithPatientDto resultDto = departmentService.departmentWithPatient(departmentId);
        return new ResponseEntity<>(resultDto,HttpStatus.OK);
    }
}

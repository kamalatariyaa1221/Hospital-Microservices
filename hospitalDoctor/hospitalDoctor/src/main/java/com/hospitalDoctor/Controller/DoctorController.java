package com.hospitalDoctor.Controller;

import com.hospitalDoctor.Entity.Doctor;
import com.hospitalDoctor.Payload.DoctorDto;
import com.hospitalDoctor.Services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // add a new doctor by department-->

    @PostMapping("/addDoctor/department/{departmentId}")
    //  http://localhost:8082/api/v1/doctor/addDoctor/department/1
    public ResponseEntity<DoctorDto> addDoctor(@PathVariable Long departmentId, @RequestBody DoctorDto dto, @RequestHeader("Authorization") String authHeader) {
        // Extract the JWT token from the Authorization header (Bearer token)
        String jwtToken = authHeader.substring(7);

        // Call the service to add doctor details
        DoctorDto doctorDto = doctorService.addDoctorDetails(dto, departmentId, jwtToken);

        return new ResponseEntity<>(doctorDto, HttpStatus.CREATED);
    }

    // get all doctor under department without using the restTemplate function-->

    @GetMapping("/getAllDoctorWithDepartmentId/Department/{departmentId}")
    //  http://localhost:8082/api/v1/doctor/getAllDoctorWithDepartmentId/Department/{departmentID}
    public ResponseEntity<List<DoctorDto>> getAllDoctorWithDepartmentId(@PathVariable Long departmentId){
        List<DoctorDto> savedDoctor = doctorService.getAllDoctorWithDepartmentId(departmentId);
        return new ResponseEntity<>(savedDoctor,HttpStatus.OK);
    }

    //get doctor with doctorId-->

    @GetMapping("/getDoctorWithId/doctor/{doctorId}")
    //  http://localhost:8082/api/v1/doctor/getDoctorWithId/doctor/{doctorId}
    public ResponseEntity<Doctor> getAllDoctorWithId(@PathVariable Long doctorId){
        Doctor savedDoctor = doctorService.getAllDoctorWithId(doctorId);
        return new ResponseEntity<>(savedDoctor, HttpStatus.OK);
    }



}

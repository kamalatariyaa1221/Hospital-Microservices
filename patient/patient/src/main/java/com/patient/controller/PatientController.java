package com.patient.controller;

import com.patient.entity.Patient;
import com.patient.payload.PatientDto;
import com.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;

    // Add Patient--> with doctor and it's department-->

    @PostMapping("/addPatient/department/{departmentId}/doctor/{doctorId}")
    //  http://localhost:8084/api/v1/patient/addPatient/department/{departmentId}/doctor/{doctorId}
    public ResponseEntity<PatientDto> addPatient(@RequestBody PatientDto patientDto, @PathVariable long departmentId, @PathVariable long doctorId){
        PatientDto dto = patientService.addPatient(patientDto, departmentId, doctorId);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //get all Patient Records with it's department-->

    @GetMapping("/getPatientRecordsByDepartment/department/{departmentId}")
    //http://localhost:8084/api/v1/patient/getPatientRecordsByDepartment/department/{departmentId}
    public ResponseEntity<List<PatientDto>> getPatientWithDepartmentId(@PathVariable Long departmentId){
        List<PatientDto> patientWithDepartmentId = patientService.getPatientWithDepartmentId(departmentId);
        return new ResponseEntity<>(patientWithDepartmentId, HttpStatus.OK);
    }

    //get Patient-> with their specific patientId-->

    @GetMapping("/getPatientWithId/patient/{patientId}")
    // http://localhost:8084/api/v1/patient/getPatientWithId/patient/{patientId}
    public ResponseEntity<Patient> getPatientWithId(@PathVariable long patientId){
        Patient patientWithId = patientService.getPatientWithId(patientId);
        return new ResponseEntity<>(patientWithId, HttpStatus.OK);
    }
}

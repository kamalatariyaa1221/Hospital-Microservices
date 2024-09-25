package com.patient.service;

import com.patient.config.RestTemplateConfig;
import com.patient.entity.Patient;
import com.patient.payload.Department;
import com.patient.payload.Doctor;
import com.patient.payload.PatientDto;
import com.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private RestTemplateConfig restTemplate;

    @Autowired
    private PatientRepository patientRepository;
    public PatientDto addPatient(PatientDto patientDto, long departmentId, long doctorId) {

        String url = "http://localhost:8081/api/v1/department/getAllDepartmentById/department/"+departmentId;
        Department response=restTemplate.getRestTemplate().getForObject(url, Department.class);

        String url2="http://localhost:8082/api/v1/doctor/getDoctorWithId/doctor/"+doctorId;
        Doctor response2= restTemplate.getRestTemplate().getForObject(url2, Doctor.class);
        Patient patient= new Patient();
        if(response!=null){
            patient.setPatientName(patientDto.getPatientName());
            patient.setDoctorId(doctorId);
            patient.setDob(patientDto.getDob());
            patient.setPhoneNumber(patientDto.getPhoneNumber());
            patient.setAddress(patientDto.getAddress());
            patient.setDepartmentId(patientDto.getDepartmentId());
            patient.setDepartmentId(departmentId);
            Patient savedPatient = patientRepository.save(patient);

            patientDto.setPatientId(savedPatient.getPatientId());
            patientDto.setPatientName(savedPatient.getPatientName());

            patientDto.setDoctorId(savedPatient.getDoctorId());
            patientDto.setAddress(savedPatient.getAddress());
            patientDto.setDob(savedPatient.getDob());
            patientDto.setPhoneNumber(savedPatient.getPhoneNumber());
            patientDto.setDepartmentId(savedPatient.getDepartmentId());
            return patientDto;
        }else{
            throw new RuntimeException("Department Not found");
        }
    }

    public Patient getPatientWithId(long patientId) {
        Patient patient = patientRepository.findById(patientId).get();
        return patient;
    }

    public List<PatientDto> getPatientWithDepartmentId(Long departmentId) {
        List<PatientDto> collect = patientRepository.findByDepartmentId(departmentId).stream().map(
                patient -> {
                    PatientDto dto = new PatientDto();
                    dto.setPatientId(patient.getPatientId());
                    dto.setPatientName(patient.getPatientName());
                    dto.setDob(patient.getDob());
                    dto.setAddress(patient.getAddress());
                    dto.setPhoneNumber(patient.getPhoneNumber());
                    dto.setDepartmentId(patient.getDepartmentId());
                    dto.setDoctorId(patient.getDoctorId());
                    return dto;
                }
        ).collect(Collectors.toList());
        return collect;
    }


}


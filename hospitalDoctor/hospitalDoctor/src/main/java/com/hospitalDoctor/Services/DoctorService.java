package com.hospitalDoctor.Services;

import com.hospitalDoctor.Config.RestTemplateConfig;
import com.hospitalDoctor.Payload.Department;
import com.hospitalDoctor.Entity.Doctor;
import com.hospitalDoctor.Payload.DoctorDto;
import com.hospitalDoctor.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private RestTemplateConfig restTemplate;

    public DoctorDto addDoctorDetails(DoctorDto doctorDto, Long departmentId, String jwtToken) {
        String url = "http://localhost:8081/api/v1/department/getAllDepartmentById/department/"+departmentId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Department> response = restTemplate.getRestTemplate().exchange(
                    url, HttpMethod.GET, entity, Department.class);


            // Check if response status is OK and get the body
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {

                Department department = response.getBody();

                // Now set the department details to the doctor
                Doctor doctor = new Doctor();
                doctor.setDoctorName(doctorDto.getDoctorName());
                doctor.setSpecialization(doctorDto.getSpecialization());
                doctor.setEmailId(doctorDto.getEmailId());
                doctor.setDepartmentId(department.getDepartmentId());

                // Save the doctor entity in the database
                Doctor savedDoctor = doctorRepository.save(doctor);

                // Convert saved doctor entity back to DTO
                doctorDto.setDoctorId(savedDoctor.getDoctorId());
                doctorDto.setDoctorName(savedDoctor.getDoctorName());
                doctorDto.setSpecialization(savedDoctor.getSpecialization());
                doctorDto.setEmailId(savedDoctor.getEmailId());
                doctorDto.setDepartmentId(savedDoctor.getDepartmentId());

                return doctorDto;
            } else {
                throw new IllegalArgumentException("Department not found");
            }
        } catch (HttpClientErrorException e) {
            // Handle 403, 404 or any client errors
            throw new RuntimeException("Error fetching department: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            // General exception handling
            throw new RuntimeException("An error occurred while adding doctor details", e);
        }
    }



    public List<DoctorDto> getAllDoctorWithDepartmentId(Long departmentId) {
    List<Doctor> findDoctor = doctorRepository.findByDepartmentId(departmentId);
    List<DoctorDto> result = findDoctor.stream()
            .map(doctor -> {
                DoctorDto doctorDto = new DoctorDto();
                doctorDto.setDoctorId(doctor.getDoctorId());
                doctorDto.setDoctorName(doctor.getDoctorName());
                doctorDto.setSpecialization(doctor.getSpecialization());
                doctorDto.setEmailId(doctor.getEmailId());
                doctorDto.setDepartmentId(departmentId);
                return doctorDto;

    }).collect(Collectors.toList());
    return result;
    }

    public Doctor getAllDoctorWithId(Long doctorId) {
        Doctor doctorList = doctorRepository.findById(doctorId).get();
        return doctorList;
    }
}

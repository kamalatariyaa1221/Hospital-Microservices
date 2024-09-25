package com.hospitalDepartment.Service;

import com.hospitalDepartment.Config.RestTemplateConfig;
import com.hospitalDepartment.Entity.Department;
import com.hospitalDepartment.Payload.*;
import com.hospitalDepartment.Repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RestTemplateConfig restTemplate;

    public DepartmentDto addDepartment(DepartmentDto departmentDto) {
        Department department = new Department();
        department.setDepartmentName(departmentDto.getDepartmentName());
        Department saved = departmentRepository.save(department);

        departmentDto.setDepartmentId(saved.getDepartmentId());
        departmentDto.setDepartmentName(saved.getDepartmentName());
        return departmentDto;
    }

    public Department getAllDepartmentById(Long departmentId) {
        Department allList = departmentRepository.findById(departmentId).get();
        return allList;
    }

    public DepartmentWithDoctorDto getAllDepartmentWithDoctor(Long departmentId, String jwtToken) {
        Department department = departmentRepository.findById(departmentId).get();

        String url= "http://localhost:8082/api/v1/doctor/getAllDoctorWithDepartmentId/Department/"+departmentId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList> doctorsArray = restTemplate.getRestTemplate()
                    .exchange(url, HttpMethod.GET, entity, ArrayList.class);


        ArrayList DoctorsList = doctorsArray.getBody();

        DepartmentWithDoctorDto resultFinalDto = new DepartmentWithDoctorDto();

        resultFinalDto.setDepartmentId(department.getDepartmentId());
        resultFinalDto.setDepartmentName(department.getDepartmentName());
        resultFinalDto.setDoctorList(DoctorsList);
        return resultFinalDto;
    }

    public DepartmentWithStaffDto getDepartmentWithStaff(Long departmentId, String jwtToken) {

        Department department = departmentRepository.findById(departmentId).get();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ArrayList> staffArray = restTemplate.getRestTemplate().exchange
                ("http://localhost:8083/api/v1/staff/getAllStaffByDepartment/department/"+departmentId, HttpMethod.GET, entity, ArrayList.class);


        ArrayList staffList = staffArray.getBody();

        DepartmentWithStaffDto finalResultDto = new DepartmentWithStaffDto();

        finalResultDto.setDepartmentId(department.getDepartmentId());
        finalResultDto.setDepartmentName(department.getDepartmentName());
        finalResultDto.setStaffList(staffList);
        return finalResultDto;
    }

    public DepartmentWithPatientDto departmentWithPatient(long departmentId) {
        Department department = departmentRepository.findById(departmentId).get();
        ArrayList patientArray = restTemplate.getRestTemplate().getForObject("http://localhost:8084/api/v1/patient/getPatientRecordsByDepartment/department/"+departmentId, ArrayList.class);

        DepartmentWithPatientDto finalResultDto = new DepartmentWithPatientDto();

        finalResultDto.setDepartmentId(department.getDepartmentId());
        finalResultDto.setDepartmentName(department.getDepartmentName());
        finalResultDto.setPatientList(patientArray);
        return  finalResultDto;
    }
}

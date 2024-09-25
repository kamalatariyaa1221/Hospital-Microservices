package com.hospitalStaff.Service;

import com.hospitalStaff.Config.RestTemplateConfig;
import com.hospitalStaff.Entity.Staff;
import com.hospitalStaff.Entity.checkStaff;
import com.hospitalStaff.Payload.Department;
import com.hospitalStaff.Payload.StaffDto;
import com.hospitalStaff.Repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private RestTemplateConfig restTemplate;

    public StaffDto addStaff(StaffDto staffDto, Long departmentId, String jwtToken) {
        String url = "http://localhost:8081/api/v1/department/getAllDepartmentById/department/" + departmentId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Department> response = restTemplate.getRestTemplate().exchange(
                    url, HttpMethod.GET, entity, Department.class);

            // Check if response status is OK and get the body
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {

                Department department = response.getBody();

                Staff staff = new Staff();

                staff.setStaffName(staffDto.getStaffName());
                staff.setGender(staffDto.getGender());
                staff.setPosition(staffDto.getPosition());
                staff.setStatus(staffDto.getStatus());
                staff.setDepartmentId(department.getDepartmentId());
                staff.setDepartmentName(department.getDepartmentName());

                Staff savedStaff = staffRepository.save(staff);
                StaffDto savedStaffDto = new StaffDto();

                savedStaffDto.setStaffId(savedStaff.getStaffId());
                savedStaffDto.setStaffName(savedStaff.getStaffName());
                savedStaffDto.setGender(savedStaff.getGender());
                savedStaffDto.setPosition(savedStaff.getPosition());
                savedStaffDto.setStatus(savedStaff.getStatus());
                savedStaffDto.setDepartmentId(savedStaff.getDepartmentId());
                return savedStaffDto;

            }

        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }

        return staffDto;
    }
    public List<StaffDto> getAllStaffByDepartment(Long departmentId) {
        List<Staff> staffdata = staffRepository.findByDepartmentId(departmentId);

                List<StaffDto> staffDto = staffdata.stream()
                .map(showStaff->{
                    StaffDto staffDto1 = new StaffDto();

                    staffDto1.setStaffId(showStaff.getStaffId());
                    staffDto1.setStaffName(showStaff.getStaffName());
                    staffDto1.setGender(showStaff.getGender());
                    staffDto1.setPosition(showStaff.getPosition());
                    staffDto1.setStatus(showStaff.getStatus());
                    staffDto1.setDepartmentId(showStaff.getDepartmentId());

                    return staffDto1;

                }).collect(Collectors.toList());
        return staffDto;
    }

    public StaffDto releaseStaff(long staffId) {
        Staff findStaff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with this id"));

        if(findStaff.getStatus()== checkStaff.busy){
            findStaff.setStatus(checkStaff.free);
//            findStaff.setDepartmentId(null);
//            findStaff.setDepartmentName(null);

            Staff savedStaff = staffRepository.save(findStaff);
            StaffDto returnStaffDto = new StaffDto();

            returnStaffDto.setStaffId(savedStaff.getStaffId());
            returnStaffDto.setStaffName(savedStaff.getStaffName());
            returnStaffDto.setGender(savedStaff.getGender());
            returnStaffDto.setPosition(savedStaff.getPosition());
            returnStaffDto.setStatus(savedStaff.getStatus());
            returnStaffDto.setDepartmentId(savedStaff.getDepartmentId());
            return returnStaffDto;
        } else {
            throw new RuntimeException("Could'nt find the staff what you looking for!! ");
        }
    }

    public StaffDto freetoBusyStaff(long staffId) {
        Staff findStaff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with this id"));

        if(findStaff.getStatus()==checkStaff.free){
            findStaff.setStatus(checkStaff.busy);
            Staff savedStaff = staffRepository.save(findStaff);
            StaffDto returnStaffDto = new StaffDto();

            returnStaffDto.setStaffId(savedStaff.getStaffId());
            returnStaffDto.setStaffName(savedStaff.getStaffName());
            returnStaffDto.setGender(savedStaff.getGender());
            returnStaffDto.setPosition(savedStaff.getPosition());
            returnStaffDto.setStatus(savedStaff.getStatus());
            returnStaffDto.setDepartmentId(savedStaff.getDepartmentId());
            return returnStaffDto;

        } else {
            throw new RuntimeException("Could'nt find the staff what you looking for!! ");
        }
    }
}


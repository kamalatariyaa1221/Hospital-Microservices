package com.hospitalDoctor.Repository;

import com.hospitalDoctor.Entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {


    List<Doctor> findByDepartmentId(Long departmentId);
}

package com.hospitalStaff.Repository;

import com.hospitalStaff.Entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByDepartmentId(Long departmentId);
}
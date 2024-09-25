package com.hospitalDepartment.Repository;

import com.hospitalDepartment.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
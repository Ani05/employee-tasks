package com.employee.demo.repository;

import com.employee.demo.model.Employee;
import com.employee.demo.model.EmployeeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmail(String name);
    Optional<Employee> findByToken(String token);
    Optional<Employee> findAllByEmployeeTypeNotLike(EmployeeType employeeType);

}

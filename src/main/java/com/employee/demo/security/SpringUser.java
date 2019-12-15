package com.employee.demo.security;

import com.employee.demo.model.Employee;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class SpringUser extends User  {
    private Employee employee;
    public SpringUser(Employee employee) {
        super(employee.getEmail(), employee.getPassword(),employee.isActive(),true,true,true, AuthorityUtils.createAuthorityList(employee.getEmployeeType().name()));
        this.employee=employee;
    }
    public Employee getEmployee(){
        return employee;
    }
}

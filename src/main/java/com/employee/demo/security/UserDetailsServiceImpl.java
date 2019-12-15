package com.employee.demo.security;

import com.employee.demo.model.Employee;
import com.employee.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 @Autowired
 private EmployeeRepository employeeRepository;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Employee> byEmail =employeeRepository.findByEmail(s);
        if (!byEmail.isPresent()){
            throw new UsernameNotFoundException("user whit " + s + "does not exists");
        }
        return new SpringUser(byEmail.get());
    }
}

package com.employee.demo;

import com.employee.demo.model.Employee;
import com.employee.demo.model.EmployeeType;
import com.employee.demo.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
@AllArgsConstructor
public class WebApplication implements CommandLineRunner {

    private EmployeeRepository employeeRepository;

    public static void main(String[] args) {

        SpringApplication.run(WebApplication.class, args);
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();

    }

    @Override
    public void run(String... args) throws Exception {
        Optional<Employee> byEmail = employeeRepository.findByEmail("admin@gmial.com");
        if (!byEmail.isPresent()) {
            Employee manager = new Employee();
            manager.setPassword(bCryptPasswordEncoder().encode("admin"));
            manager.setName("admin");
            manager.setSurname("Ghalachyan");
            manager.setEmail("admin@gmial.com");
            manager.setActive(true);
            manager.setCountry("TEQSTIL");
            manager.setPicUrl("1575139136024_profile-picture.png");
            manager.setToken("1575139136024_profile-picture.png");
            manager.setTown("58");
            manager.setPhone("555558");
            manager.setSpecialty("duygf ");
            manager.setEmployeeType(EmployeeType.MANAGER);
            employeeRepository.save(manager);
        }

    }
}

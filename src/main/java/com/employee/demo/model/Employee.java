package com.employee.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String phone;
    @Column
    private String country;
    @Column
    private String town;
    @Column
    private String specialty;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "pic_url")
    private String picUrl;
    @Column
    private String token;
    @Column(name = "employee_type")
    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType = EmployeeType.USER;

}

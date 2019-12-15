package com.employee.demo.repository;

import com.employee.demo.model.Status;
import com.employee.demo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findTaskByEmployee_Id(int id);
    List<Task> findTaskByStatus(Status status);

}

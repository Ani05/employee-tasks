package com.employee.demo.controller;

import com.employee.demo.model.Employee;
import com.employee.demo.model.Status;
import com.employee.demo.model.Task;
import com.employee.demo.repository.EmployeeRepository;
import com.employee.demo.repository.TaskRepository;
import com.employee.demo.security.SpringUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class TaskController {
    private EmployeeRepository employeeRepository;
    private TaskRepository taskRepository;

    @GetMapping("/getAllTasks")
    public String taskPage(ModelMap modelMap) {

        List<Task> allTask = taskRepository.findAll();
        List<Employee> allEmployee = employeeRepository.findAll();
        modelMap.addAttribute("allTasks", allTask);
        modelMap.addAttribute("allEmployees", allEmployee);


        return "task";
    }

    @PostMapping("/addTask")
    public String addTask(@Valid Task task) {
        task.setStatus(Status.NEW);
        task.setDate(String.valueOf(new Date()));
        taskRepository.save(task);
        return "redirect:/getAllTasks";


    }

    @GetMapping("/taskPage")
    public String taskPage(ModelMap modelMap, @RequestParam("id") int id) {
        Optional<Task> byId = taskRepository.findById(id);

        if (byId.isPresent()){
            modelMap.addAttribute("task", byId.get());
        }
        return "taskPage";
    }

    @GetMapping("/deleteTask")
    public String deleteUser(@RequestParam("id") int id) {
        taskRepository.deleteById(id);
        return "redirect:/getAllTasks";

    }

    @GetMapping("/editTask")
    public String editTask(ModelMap modelMap,
                           @RequestParam("id") int id
    ) {
        Optional<Task> byId = taskRepository.findById(id);
        List<Employee> all = employeeRepository.findAll();
        if (byId.isPresent()) {
            modelMap.addAttribute("task", byId.get());
            modelMap.addAttribute("employees", all);
            modelMap.addAttribute("status", Status.values());
            return "editTask";
        } else {
            return "redirect:/error";
        }
    }

    @PostMapping("/editTask")
    public String changeUser(@ModelAttribute("task") Task task,
                             @RequestParam("id") int id) {
        task.setId(id);
        task.setDate(String.valueOf(new Date()));
        taskRepository.save(task);
        return "redirect:/getAllTasks";

    }

    @GetMapping("/changeStatus")
    public String editSingleEmployeeTask(@RequestParam("id") int id) {
        Optional<Task> byId = taskRepository.findById(id);
        if (byId.isPresent()) {
            Task taskData = byId.get();
            if (taskData.getStatus().getName().equals(Status.NEW.getName())) {
                taskData.setStatus(Status.INPROGRESS);
            } else if (taskData.getStatus().getName().equals(Status.INPROGRESS.getName())) {
                taskData.setStatus(Status.FINISH);
            }
            taskRepository.save(taskData);
            return "redirect:/taskPage?id=" + id;
        }
        return "redirect:/error";
    }


    @GetMapping("/profile1")
    public String profile1(ModelMap modelMap,
                           @AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null) {
            modelMap.addAttribute("employee", springUser.getEmployee());
            modelMap.addAttribute("tasks", taskRepository.findTaskByEmployee_Id(springUser.getEmployee().getId()));
            modelMap.addAttribute("newTask",taskRepository.findTaskByStatus(Status.NEW));
            return "profile1";
        }

        return "redirect:/";
    }

}

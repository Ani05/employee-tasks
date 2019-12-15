package com.employee.demo.controller;

import com.employee.demo.model.Employee;
import com.employee.demo.model.EmployeeType;
import com.employee.demo.model.Status;
import com.employee.demo.model.Task;
import com.employee.demo.repository.EmployeeRepository;
import com.employee.demo.repository.TaskRepository;
import com.employee.demo.security.SpringUser;
import com.employee.demo.service.MailService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class EmployeeController {
    @Value("${image.upload.dir}")
    private String imageUploadDir;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailService mailService;
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @GetMapping("/profile")
    public String profile(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        int id = springUser.getEmployee().getId();
        modelMap.addAttribute("employee", employeeRepository.findById(id).get());

        return "profile";
    }


    @GetMapping("/task")
    public String taskPage() {
        return "task";
    }

    @GetMapping("/error")
    public String errorPage() {

        return "404";
    }

    @GetMapping("/")
    public String loginSuccess(@AuthenticationPrincipal SpringUser springUser) {

        if (springUser.getEmployee().getEmployeeType() == EmployeeType.USER && springUser.getEmployee().isActive()) {
            return "redirect:/profile1";

        } else if (springUser.getEmployee().getEmployeeType() == EmployeeType.MANAGER && springUser.getEmployee().isActive()) {
            return "redirect:/profile";
        }
        return "redirect:/login";
    }

    @GetMapping("/getAllEmployees")
    public String getAllEmployees(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        List<Employee> all = employeeRepository.findAll();
        int id = springUser.getEmployee().getId();
        modelMap.addAttribute("allEmpls", all);
        modelMap.addAttribute("employee", employeeRepository.findById(id).get());
        return "employee";

    }

    @PostMapping("/register")
    public String register(@Valid Employee employee, @RequestParam("picture") MultipartFile file) throws IOException {
        String token = UUID.randomUUID().toString();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File picture = new File(imageUploadDir + File.separator + fileName);
        file.transferTo(picture);
        employee.setPicUrl(fileName);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee.setName(employee.getName());
        employee.setEmail(employee.getEmail());
        employee.setToken(token);
        employeeRepository.save(employee);
        mailService.sendSimpleMessage(employee.getEmail(), "Welcome dear" + employee.getName(), "Please check the link for activating your profile: \n http://localhost:8080/activateProfile?token=" + token);
        return "redirect:/getAllEmployees";
    }

    @GetMapping("/getImage")
    public void getImageAsByteArray(HttpServletResponse response, @RequestParam("picUrl") String picUrl) throws IOException {
        InputStream in = new FileInputStream(imageUploadDir + File.separator + picUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    @GetMapping("/activateProfile")
    public String activate(@RequestParam("token") String token) {
        Optional<Employee> byToken = employeeRepository.findByToken(token);
        if (byToken.isPresent()) {
            byToken.get().setActive(true);
            byToken.get().setToken("");
            employeeRepository.save(byToken.get());
        }
        return "redirect:/login";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") int id) {


        employeeRepository.deleteById(id);
        return "redirect:/getAllEmployees";
    }


    @GetMapping("/edit")
    public String changeProfile(@RequestParam("id") int id, @AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        Optional<Employee> allById = employeeRepository.findById(id);
        if (springUser != null) {
            modelMap.addAttribute("employee", allById.get());
                modelMap.addAttribute("emplManager", employeeRepository.findById(springUser.getEmployee().getId()).get());
            return "edit";
        } else {
            return "redirect:/";
        }
    }
    //myam run kudas nayenq vor masn e հա

    @PostMapping("/edit")
    public String changeUser(@ModelAttribute Employee employee,
                             @RequestParam("picture") MultipartFile file,
                             @RequestParam("id") int id) throws IOException {

        Optional<Employee> byId = employeeRepository.findById(id);
        if (byId.isPresent()) {
            Employee emplFromDb = byId.get();
            emplFromDb.setName(employee.getName());
            emplFromDb.setSurname(employee.getSurname());
            emplFromDb.setCountry(employee.getCountry());
            emplFromDb.setTown(employee.getTown());
            emplFromDb.setPhone(employee.getPhone());
            emplFromDb.setSpecialty(employee.getSpecialty());

//            emplFromDb.setPassword(passwordEncoder.encode(employee.getPassword()));

          if (!file.isEmpty()){
              String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
              File picture = new File(imageUploadDir + File.separator + fileName);
              file.transferTo(picture);
              emplFromDb.setPicUrl(fileName);

          }
            employeeRepository.save(emplFromDb);
            return "redirect:/getAllEmployees";
        }

        return "redirect:/login";

    }
}
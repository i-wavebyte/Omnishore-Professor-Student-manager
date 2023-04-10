package backend.server.service.controllers;

import backend.server.service.domain.Student;

import backend.server.service.services.IStudentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//codegpt do: generate an angular service that communicates with this controller (the controller is at http://localhost:8080, also create an interface Student in typescript to map the objects received from the backend
//here is the class in java
//@Data @AllArgsConstructor @NoArgsConstructor
//public class Student {
//    private Long id;
//    private String name;
//    private String email;
//    private String telephone;
//    private String cin;
//    private String groupe;
//    private List<Long> professors = new ArrayList<>();
//}
@RestController
@RequestMapping("/studentService")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StudentController {

    private final IStudentService studentService;

    public StudentController(IStudentService studentService) {
        this.studentService = studentService;
    }
    @PreAuthorize("hasRole('ROLE_PROF_MANAGER')")
    @GetMapping("/find/{id}")
    public Student getById(@PathVariable Long id) {
        return studentService.getById(id);
    }
    @PreAuthorize("hasRole('ROLE_PROF_MANAGER')")
    @GetMapping("/findAll")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }
    @PreAuthorize("hasRole('ROLE_PROF_MANAGER')")
    @GetMapping("/getIdsList")
    public List<Student> getStudentsByList(@RequestParam List<Long> studentIds) {
        return studentService.getStudentsByList(studentIds);
    }

    @PreAuthorize("hasRole('ROLE_PROF_MANAGER')")
    @PutMapping("/assignProfessor/{studentId}")
    public Student assignProfessor(@PathVariable Long studentId, @RequestParam Long profId) {
        return studentService.assignProfessor(studentId, profId);
    }
}
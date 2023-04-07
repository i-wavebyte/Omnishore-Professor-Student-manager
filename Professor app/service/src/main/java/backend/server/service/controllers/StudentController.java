package backend.server.service.controllers;

import backend.server.service.domain.Student;

import backend.server.service.services.IStudentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
package backend.server.service.controllers;

import backend.server.service.domain.Student;
import backend.server.service.service.IStudentService;
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

    @PreAuthorize("hasRole('ROLE_STUD_MANAGER')")
    @PostMapping("/add")
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PreAuthorize("hasRole('ROLE_STUD_MANAGER') or hasRole('ROLE_PROF_MANAGER')")
    @GetMapping("/findAll")
    public List<Student> findAllStudents() {
        return studentService.findAllStudents();
    }

    @PreAuthorize("hasRole('ROLE_STUD_MANAGER')")
    @PutMapping("/update/{id}")
    public Student updateStudent(@PathVariable long id, @RequestBody Student student) {
        student.setId(id);
        return studentService.updateStudent(student);
    }

    @PreAuthorize("hasRole('ROLE_STUD_MANAGER')")
    @DeleteMapping("/delete/{id}")
    public void deleteStudent(@PathVariable long id) {
        studentService.deleteStudent(id);
    }

    @PreAuthorize("hasRole('ROLE_STUD_MANAGER') or hasRole('ROLE_PROF_MANAGER')")
    @GetMapping("/find/{id}")
    public Student findStudent(@PathVariable long id) {
        return studentService.findStudent(id);
    }

    @PreAuthorize("hasRole('ROLE_STUD_MANAGER') or hasRole('ROLE_PROF_MANAGER')")
    @GetMapping("/getIdsList")
    public List<Student> getIdsList(@RequestParam List<Long> studentIds) {
        return studentService.getIdsList(studentIds);
    }

    @PreAuthorize("hasRole('ROLE_STUD_MANAGER') or hasRole('ROLE_PROF_MANAGER')")
    @PutMapping("/assignProfessor/{studentId}")
    public Student assignProfessor(@PathVariable Long studentId, @RequestParam Long profId) {
        return studentService.assignProfessor(studentId, profId);
    }

    @PreAuthorize("hasRole('ROLE_STUD_MANAGER')")
    @PostMapping("/addAll")
    public List<Student> addAll(@RequestBody List<Student> students) {
        return studentService.addAll(students);
    }
}
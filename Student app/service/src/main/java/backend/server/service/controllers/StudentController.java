package backend.server.service.controllers;

import backend.server.service.domain.PageResponse;
import backend.server.service.domain.Student;
import backend.server.service.service.IStudentService;
import org.springframework.http.ResponseEntity;
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
    public Student updateStudent(@PathVariable("id") long studentId, @RequestBody Student student) {
        student.setId(studentId);
        return studentService.updateStudent(student);
    }

    @PreAuthorize("hasRole('ROLE_STUD_MANAGER')")
    @DeleteMapping("/delete/{id}")
    public void deleteStudent(@PathVariable("id") long studentId) {
        studentService.deleteStudent(studentId);
    }

    @PreAuthorize("hasRole('ROLE_STUD_MANAGER') or hasRole('ROLE_PROF_MANAGER')")
    @GetMapping("/find/{id}")
    public Student findStudent(@PathVariable("id") long studentId) {
        return studentService.findStudent(studentId);
    }

    @PreAuthorize("hasRole('ROLE_STUD_MANAGER') or hasRole('ROLE_PROF_MANAGER')")
    @GetMapping("/getIdsList")
    public ResponseEntity<PageResponse<Student>> getIdsList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(value = "searchQuery", required = false) String searchQuery,
            @RequestParam(value = "groupeFilter", required = false) String groupeFilter,
            @RequestParam(value = "studentIds", required = false) List<Long> studentIds) {

        PageResponse<Student> result = studentService.getIdsList(page, size, sortBy, sortOrder, searchQuery, groupeFilter, studentIds);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_STUD_MANAGER') or hasRole('ROLE_PROF_MANAGER')")
    @PutMapping("/assignProfessor/{studentId}")
    public Student assignProfessor(@PathVariable("studentId") Long studentId, @RequestParam Long profId) {
        return studentService.assignProfessor(studentId, profId);
    }

    @PreAuthorize("hasRole('ROLE_STUD_MANAGER')")
    @PutMapping("/addAll/{studentId}")
    public List<Student> addAll(List<Student> students) {
        return studentService.addAll(students);
    }
}
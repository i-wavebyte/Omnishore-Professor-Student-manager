package backend.server.service.service;

import backend.server.service.domain.Student;
import backend.server.service.exception.userNotFoundException;
import backend.server.service.repositories.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

//codegpt do: generate a controller for this service set crossOrigin to * with a max age of 3600, and the request mapping to /studentService
@Service @Transactional @Slf4j
public class StudentService implements IStudentService{

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    //we injected this repository in this class so we can work with it in this class
    private final StudentRepository studentRepo;
    @Autowired
    public StudentService(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }
    public Student addStudent(Student student)
    {
        return studentRepo.save(student);
    }

    public List<Student> findAllStudents(){
        return studentRepo.findAll();
    }

    public Student updateStudent(Student student){
        return studentRepo.save(student);
    }

    public void deleteStudent(long id)
    {
        studentRepo.deleteById(id);
    }

    public Student findStudent(long id){
        return studentRepo.findStudentById(id)
                .orElseThrow(()-> new userNotFoundException("User by id:  " + id +" was not found!"));
    }

    public List<Student> getIdsList(List<Long> studentIds) {
        List<Student> students = new ArrayList<Student>();
        for(Long id : studentIds){
            Student p = studentRepo.findById(id).orElse(new Student());
            if(p.getId()==null) continue;
            students.add(p);
        }
        return students;
    }

    public Student assignProfessor(Long studentId, Long profId) {
        Student student = studentRepo.findStudentById(studentId).
                orElseThrow(()-> new userNotFoundException("User by id:  " + studentId +" was not found!"));
        System.out.println(ANSI_RED+ student.getProfessors() + ANSI_RESET);
        List<Long> s = student.getProfessors();
        s.add(profId);
        student.setProfessors(s);
        Set<Long> set = new HashSet<>(student.getProfessors()); // Convert list to Set to remove duplicates
        student.getProfessors().clear();
        student.getProfessors().addAll(set); // Add the distinct values back to the list
        System.out.println(ANSI_BLUE+ student.getProfessors() + ANSI_RESET);
        return studentRepo.save(student);

    }

    public List<Student> addAll(List<Student> students){
        return studentRepo.saveAll(students);
    }
}

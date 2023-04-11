package backend.server.service.service;

import backend.server.service.domain.PageResponse;
import backend.server.service.domain.Student;
import backend.server.service.exception.userNotFoundException;
import backend.server.service.repositories.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

//codegpt do: generate a controller for this service set crossOrigin to * with a max age of 3600, and the request mapping to /studentService
@Service @Transactional @Slf4j
public class StudentService implements IStudentService{

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    @Autowired
    private  IProfessorService professorService;
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

    @Override
    public PageResponse<Student> getIdsList(int page, int size, String sortBy, String sortOrder, String searchQuery, String groupeFilter, List<Long> studentIds) {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        int start = page * size;

        List<Student> students = studentRepo.findAllById(studentIds);

        // Sort students
        Comparator<Student> comparator;
        switch (sortBy) {
            case "name":
                comparator = Comparator.comparing(Student::getName);
                break;
            default:
                comparator = Comparator.comparing(Student::getId);
        }

        if (direction == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }

        students.sort(comparator);

        if (searchQuery != null && !searchQuery.isEmpty()) {
            students = students.stream()
                    .filter(student -> student.getName().toLowerCase().contains(searchQuery.toLowerCase())
                            || student.getCin().toLowerCase().contains(searchQuery.toLowerCase())
                            || student.getEmail().toLowerCase().contains(searchQuery.toLowerCase())
                            || student.getTelephone().toLowerCase().contains(searchQuery.toLowerCase()))
                    .toList();
        }
        if (groupeFilter != null && !groupeFilter.isEmpty()) {
            students = students.stream()
                    .filter(student -> student.getGroupe().equalsIgnoreCase(groupeFilter))
                    .toList();
        }

        int end = Math.min(start + size, students.size());
        List<Student> pageContent = students.subList(start, end);
        return new PageResponse<>(pageContent, students.size());
    }


    public Student assignProfessor(Long studentId, Long profId) {
        Student student = studentRepo.findStudentById(studentId).
                orElseThrow(()-> new userNotFoundException("User by id:  " + studentId +" was not found!"));
        log.info(ANSI_RED+ student.getProfessors() + ANSI_RESET);
        List<Long> s = student.getProfessors();
        s.add(profId);
        student.setProfessors(s);
        Set<Long> set = new HashSet<>(student.getProfessors()); // Convert list to Set to remove duplicates
        student.getProfessors().clear();
        student.getProfessors().addAll(set); // Add the distinct values back to the list
        log.info(ANSI_BLUE+ student.getProfessors() + ANSI_RESET);
        professorService.assignStudent(profId,studentId);
        return studentRepo.save(student);

    }

    public List<Student> addAll(List<Student> students){
        return studentRepo.saveAll(students);
    }

    @Override
    public PageResponse<Student> getStudentsPage(int page, int size, String sortBy, String sortOrder, String searchQuery, String groupeFilter) {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Sort sort = Sort.by(direction, sortBy);
        int start = page * size;
        int end = Math.min(start + size, (int) studentRepo.count());

        List<Student> students = studentRepo.findAll(sort);

        if (searchQuery != null && !searchQuery.isEmpty()) {
            students = students.stream()
                    .filter(professor -> professor.getName().toLowerCase().contains(searchQuery.toLowerCase())
                            || professor.getCin().toLowerCase().contains(searchQuery.toLowerCase())
                            || professor.getEmail().toLowerCase().contains(searchQuery.toLowerCase())
                            || professor.getTelephone().toLowerCase().contains(searchQuery.toLowerCase()))
                    .toList();
        }
        if (groupeFilter != null && !groupeFilter.isEmpty()) {
            students = students.stream()
                    .filter(professor -> professor.getGroupe().equalsIgnoreCase(groupeFilter))
                    .toList();
        }

        List<Student> pageContent = students.subList(start, Math.min(end, students.size()));
        return new PageResponse<>(pageContent, students.size());
    }
}

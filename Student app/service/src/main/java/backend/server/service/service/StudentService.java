package backend.server.service.service;

import backend.server.service.domain.PageResponse;
import backend.server.service.domain.Student;
import backend.server.service.exception.BadRequestException;
import backend.server.service.exception.userNotFoundException;
import backend.server.service.repositories.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service @Transactional @Slf4j
public class StudentService implements IStudentService{

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    static final  String ERROR_PREFIX = "Error: ";
    // using format specifiers instead of concatenation
    static final String FORMAT_SPECIFIER_PREFIX = "{}{}{}{}";

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
        try {
            boolean existEmail = studentRepo.existsByEmail(student.getEmail());
            if (existEmail) {
                throw new BadRequestException("Email " + student.getEmail() + " Already taken");
            }
            return studentRepo.save(student);
        }catch (BadRequestException x){
            log.info(FORMAT_SPECIFIER_PREFIX, ANSI_BLUE , ERROR_PREFIX ,x.getMessage() , ANSI_RESET);
            return null;
        }
    }

    public List<Student> findAllStudents()
    {
        return studentRepo.findAll();
    }

    public Student updateStudent(Student student){
        return studentRepo.save(student);
    }

    public void deleteStudent(long studentId)
    {
        try {
            if (!studentRepo.existsById(studentId))
            {
                throw new userNotFoundException("User by id: "+ studentId+ " doesn't exist !");
            }
            studentRepo.deleteById(studentId);
        }
        catch(userNotFoundException x)
        {
            log.info(FORMAT_SPECIFIER_PREFIX, ANSI_BLUE , ERROR_PREFIX ,x.getMessage() , ANSI_RESET);
            throw x;
        }
    }

    public Student findStudent(long id){
        try {
            return studentRepo.findById(id)
                    .orElseThrow(() -> new userNotFoundException("User by id:  " + id + " was not found!"));
        } catch(userNotFoundException x)
        {
            log.info(FORMAT_SPECIFIER_PREFIX, ANSI_BLUE , ERROR_PREFIX ,x.getMessage() , ANSI_RESET);
            return null;
        }
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
        Student student = studentRepo.findById(studentId).
                orElseThrow(()-> new userNotFoundException("User by id:  " + studentId +" was not found!"));
        log.info(student.getProfessors().toString());
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

    @Override
    public List<Student> addAll(List<Student> students) {
        return studentRepo.saveAll(students);
    }

}

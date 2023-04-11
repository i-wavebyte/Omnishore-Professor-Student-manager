package backend.server.service.service;

import backend.server.service.domain.PageResponse;
import backend.server.service.domain.Student;

import java.util.List;

public interface IStudentService {
    Student addStudent(Student student);
    List<Student> findAllStudents();
    Student updateStudent(Student student);
    void deleteStudent(long id);
    Student findStudent(long id);

    PageResponse<Student> getIdsList(int page, int size, String sortBy, String sortOrder, String searchQuery, String groupeFilter, List<Long> studentIds);

    Student assignProfessor(Long studentId, Long profId);
    List<Student> addAll(List<Student> students);

}
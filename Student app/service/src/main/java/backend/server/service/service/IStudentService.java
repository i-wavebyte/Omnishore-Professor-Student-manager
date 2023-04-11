package backend.server.service.service;

import backend.server.service.domain.Student;

import java.util.List;

public interface IStudentService {
    Student addStudent(Student student);
    List<Student> findAllStudents();
    Student updateStudent(Student student);
    void deleteStudent(long id);
    Student findStudent(long id);
    List<Student> getIdsList(List<Long> studentIds);
    Student assignProfessor(Long studentId, Long profId);
}
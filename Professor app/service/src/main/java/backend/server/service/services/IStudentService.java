package backend.server.service.services;

import backend.server.service.domain.Student;
import backend.server.service.domain.StudentServiceAuthObject;

import java.util.List;

public interface IStudentService {

    //calls http://localhost:8081/api/auth/signin, gets username and password as an object (send StudentServiceAuthObject)
    String getAccessToken();
    //calls http.localhost:8081/studentService/find/{id}
    Student getById(Long id);
    //calls http.localhost:8081/studentService/findAll
    List<Student> getAllStudents();
    //calls http.localhost:8081/studentService/getIdsList?studentIds=<list of student ids>

    List<Student> getStudentsByList(List<Long> studentIds);

    //calls http.localhost:8081/studentService/assignProfessor/{studentId}?profId=<prof id here>
    Student assignProfessor(Long studentId, Long professorId);
}

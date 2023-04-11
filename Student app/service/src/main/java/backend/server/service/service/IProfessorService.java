package backend.server.service.service;
import backend.server.service.domain.Professor;
import java.util.List;

public interface IProfessorService {
    List<Professor> getAllProfessors();
//    Professor getProfessorById(Long id);
    List<Professor> getFromIdList(List<Long> ids);
//    Professor assignStudent(Long id, Long student);
}

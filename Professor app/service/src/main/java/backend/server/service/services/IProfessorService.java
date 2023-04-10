package backend.server.service.services;


import backend.server.service.domain.PageResponse;
import backend.server.service.domain.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProfessorService {
    Professor addProfessor(Professor professor);
    Professor updateProfessor(Long id, Professor professor);
    List<Professor> getAllProfessors();
    Professor getProfessorById(Long id);
    void deleteProfessor(Long id);
    List<Professor> getFromIdList(List<Long> ids);
    Professor assignStudents(Long id, List<Long> ids);
    Professor assignStudent(Long id, Long student);
    List<Professor> addAll(List<Professor> professors);
    List<String> getAllUniqueSubjects();
    PageResponse<Professor> getProfessorsPage(int page, int size, String sortBy, String sortOrder, String searchQuery, String subjectFilter);
}

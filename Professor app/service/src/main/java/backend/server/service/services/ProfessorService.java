package backend.server.service.services;

import backend.server.service.constants.Literals;
import backend.server.service.domain.PageResponse;
import backend.server.service.domain.Professor;
import backend.server.service.repositories.ProfessorRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Define the Professor service as a Spring service
@Transactional
@Slf4j @Service
public class ProfessorService implements IProfessorService {

    @Autowired
    private IStudentService studentService;
    // Inject the Professor repository using Spring's dependency injection
    @Autowired
    private ProfessorRepository professorRepository;

    // Implement the addProfessor method from the interface
    @Override
    public Professor addProfessor(Professor professor) {
        return professorRepository.save(professor);
    }

    // Implement the updateProfessor method from the interface
    @Override
    public Professor updateProfessor(Long prodId, Professor professor) {
        // Find the Professor by ID or throw an exception if not found
        Professor p = professorRepository.findById(prodId).orElseThrow(() -> new RuntimeException(Literals.PROFESSOR_NOT_FOUND));
        // Update the Professor fields if not null
        p.setName(professor.getName());
        p.setSubject(professor.getSubject());
        p.setEmail(professor.getEmail());
        p.setTelephone(professor.getTelephone());
        // Save the updated Professor and return it
        return professorRepository.save(p);
    }

    @Override
    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    // Implement the getAllProfessors method from the interface
    @Override
    public PageResponse<Professor> getProfessorsPage(int page, int size, String sortBy, String sortOrder, String searchQuery, String subjectFilter) {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Sort sort = Sort.by(direction, sortBy);
        int start = page * size;
        int end = Math.min(start + size, (int) professorRepository.count());

        List<Professor> professors = professorRepository.findAll(sort);

        if (searchQuery != null && !searchQuery.isEmpty()) {
            professors = professors.stream()
                    .filter(professor -> professor.getName().toLowerCase().contains(searchQuery.toLowerCase())
                            || professor.getCin().toLowerCase().contains(searchQuery.toLowerCase())
                            || professor.getEmail().toLowerCase().contains(searchQuery.toLowerCase())
                            || professor.getTelephone().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (subjectFilter != null && !subjectFilter.isEmpty()) {
            professors = professors.stream()
                    .filter(professor -> professor.getSubject().equalsIgnoreCase(subjectFilter))
                    .collect(Collectors.toList());
        }

        List<Professor> pageContent = professors.subList(start, Math.min(end, professors.size()));
        return new PageResponse<>(pageContent, professors.size());
    }

    // Implement the getProfessorById method from the interface
    @Override
    public Professor getProfessorById(Long profId) {
        // Find the Professor by ID or throw an exception if not found
        return professorRepository.findById(profId).orElseThrow(() -> new RuntimeException(Literals.PROFESSOR_NOT_FOUND));
    }

    // Implement the deleteProfessor method from the interface
    @Override
    public void deleteProfessor(Long profId) {
        // Find the Professor by ID or throw an exception if not found
        Professor p = professorRepository.findById(profId).orElseThrow(() -> new RuntimeException(Literals.PROFESSOR_NOT_FOUND));
        // Delete the Professor
        professorRepository.delete(p);
    }

    // Implement the getFromIdList method from the interface
    @Override
    public List<Professor> getFromIdList(List<Long> profIds) {
        List<Professor> professors = new ArrayList<>();
        for(Long id : profIds){
            // Find the Professor by ID or create a new one if not found
            Professor p = professorRepository.findById(id).orElse(new Professor());
            // If the ID is null, skip this Professor
            if(p.getId()==null) continue;
            // Add the Professor to the list
            professors.add(p);
        }
        // Return the list of Professors
        return professors;
    }

    // Implement the assignStudents method from the interface
    @Override
    public Professor assignStudents(Long profIds, List<Long> studentIds) {
        // Find the Professor by ID or throw an exception if not found
        Professor p = professorRepository.findById(profIds).orElseThrow(() -> new RuntimeException(Literals.PROFESSOR_NOT_FOUND));
        // Add the student IDs to the Professor's list of students
        p.getEtudiants().addAll(studentIds);
        // Convert the list to a set to remove duplicates
        Set<Long> set = new HashSet<>(p.getEtudiants());
        // Clear the list and add back the distinct values
        p.getEtudiants().clear();
        p.getEtudiants().addAll(set);
        // Save the updated Professor and return it
        return professorRepository.save(p);
    }

    // Implement the assignStudent method from the interface
    @Override
    public Professor assignStudent(Long idProfessor, Long idStudent) {
        // Find the Professor by ID or throw an exception if not found
        Professor p = professorRepository.findById(idProfessor).orElseThrow(() -> new RuntimeException(Literals.PROFESSOR_NOT_FOUND));
        // Add the student ID to the Professor's list of students
        p.getEtudiants().add(idStudent);
        // Convert the list to a set to remove duplicates
        Set<Long> set = new HashSet<>(p.getEtudiants());
        // Clear the list and add back the distinct values
        p.getEtudiants().clear();
        p.getEtudiants().addAll(set);
        // Save the updated Professor and return it
        return professorRepository.save(p);
    }

    @Override
    public List<Professor> addAll(List<Professor> professors) {
        return professorRepository.saveAll(professors);
    }
    public List<String> getAllUniqueSubjects() {
        return professorRepository.findAllUniqueSubjects();
    }
}

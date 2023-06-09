package backend.server.service.controllers;
import backend.server.service.domain.PageResponse;
import backend.server.service.domain.Professor;
import backend.server.service.services.IProfessorService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;



@RestController
@RequestMapping("/professorService")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class ProfessorController {



    private final IProfessorService professorService;
    public ProfessorController(IProfessorService professorService){
        this.professorService = professorService;
    }


    @PreAuthorize("hasRole('ROLE_PROF_MANAGER') or hasRole('ROLE_STUD_MANAGER')")
    @GetMapping("/get/{profId}")
    public Professor getProfessor(@PathVariable long profId){
        return professorService.getProfessorById(profId);
    }


    @PreAuthorize("hasRole('ROLE_PROF_MANAGER') or hasRole('ROLE_STUD_MANAGER')")
    @GetMapping("/get")
    public PageResponse<Professor> getAllProfessors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder,
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) String subjectFilter
    ) {
        return professorService.getProfessorsPage(page, size, sortBy, sortOrder, searchQuery, subjectFilter);

    }


    @PreAuthorize("hasRole('ROLE_PROF_MANAGER') or hasRole('ROLE_STUD_MANAGER')")
    @GetMapping("/get/list")
    public List<Professor> getProfessorList(@RequestParam List<Long> profIds){
        return professorService.getFromIdList(profIds);
    }


    @PreAuthorize("hasRole('ROLE_PROF_MANAGER')")
    @PostMapping("/add")
    public Professor addProfessor(@RequestBody Professor professor){
        return professorService.addProfessor(professor);
    }


    @PreAuthorize("hasRole('ROLE_PROF_MANAGER')")
    @PutMapping("/update/{profId}")
    public Professor updateProfessor(@PathVariable Long profId,@RequestBody Professor professor){
        return professorService.updateProfessor(profId,professor);
    }


    @PreAuthorize("hasRole('ROLE_PROF_MANAGER')")
    @DeleteMapping("/delete/{profId}")
    public void deleteProfessor(@PathVariable Long profId){
        professorService.deleteProfessor(profId);
    }


    @PreAuthorize("hasRole('ROLE_PROF_MANAGER') or hasRole('ROLE_STUD_MANAGER')")
    @PutMapping("/assign/{profId}")
    public Professor assignStudent(@PathVariable Long profId,@RequestParam Long studentId){
        return professorService.assignStudent(profId,studentId);
    }


    @PreAuthorize("hasRole('ROLE_PROF_MANAGER') or hasRole('ROLE_STUD_MANAGER')")
    @PutMapping("/assignAll/{id}")
    public Professor assignStudent(@PathVariable Long profId,@RequestBody List<Long> studentIds){
        return professorService.assignStudents(profId,studentIds);
    }


    @PreAuthorize("hasRole('ROLE_PROF_MANAGER')")
    @PostMapping("/addAll")
    public List<Professor> addAll(@RequestBody List<Professor> professors){
        return professorService.addAll(professors);
    }

    @PreAuthorize("hasRole('ROLE_PROF_MANAGER') or hasRole('ROLE_STUD_MANAGER')")
    @GetMapping("/subjects")
    public List<String> getAllUniqueSubjects() {
        return professorService.getAllUniqueSubjects();
    }
}
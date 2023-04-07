package backend.server.service.controllers;

import backend.server.service.domain.Professor;
import backend.server.service.service.IProfessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professorService")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class ProfessorController {

    private final IProfessorService professorService;

    @Autowired
    public ProfessorController(IProfessorService professorService) {
        this.professorService = professorService;
    }
    @PreAuthorize("hasRole('ROLE_STUD_MANAGER')")
    @GetMapping("/getAll")
    public ResponseEntity<List<Professor>> getAllProfessors() {
        return ResponseEntity.ok(professorService.getAllProfessors());
    }
    @PreAuthorize("hasRole('ROLE_STUD_MANAGER')")
    @GetMapping("/get/{id}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.getProfessorById(id));
    }
    @PreAuthorize("hasRole('ROLE_STUD_MANAGER')")
    @GetMapping("/get/list")
    public ResponseEntity<List<Professor>> getFromIdList(@RequestParam List<Long> profIds) {
        return ResponseEntity.ok(professorService.getFromIdList(profIds));
    }
    @PreAuthorize("hasRole('ROLE_STUD_MANAGER')")
    @PutMapping("/assign/{id}")
    public ResponseEntity<Professor> assignStudent(@PathVariable Long id, @RequestParam Long studentId) {
        return ResponseEntity.ok(professorService.assignStudent(id, studentId));
    }
}

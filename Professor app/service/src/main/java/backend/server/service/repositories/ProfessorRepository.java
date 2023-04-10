package backend.server.service.repositories;



import backend.server.service.domain.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor,Long> {
    @Query("SELECT DISTINCT p.subject FROM Professor p")
    List<String> findAllUniqueSubjects();

}

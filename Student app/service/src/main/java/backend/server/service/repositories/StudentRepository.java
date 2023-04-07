package backend.server.service.repositories;
import backend.server.service.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    //this is called query method, spring is gping to crate  a query that understand the convention of the name and execute it
    Optional<Student> findStudentById(long id);
}

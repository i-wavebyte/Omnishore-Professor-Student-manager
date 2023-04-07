package backend.server.service.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Professor {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String name;
    private String cin;
    private String subject;
    private String email;
    private String telephone;
    // this contains Ids of students assigned to the professor instanced from this entity
    @ElementCollection
    @CollectionTable(name = "professor_etudiants", joinColumns = @JoinColumn(name = "professor_id"))
    @Column(name = "etudiant_id")
    private List<Long> etudiants = new ArrayList<>();
}

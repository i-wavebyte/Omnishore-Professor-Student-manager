package backend.server.service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class Professor {

    private Long id;
    private String name;
    private String cin;
    private String subject;
    private String email;
    private String telephone;
    // this contains Ids of students assigned to the professor instanced from this entity
    private List<Long> etudiants = new ArrayList<>();
}

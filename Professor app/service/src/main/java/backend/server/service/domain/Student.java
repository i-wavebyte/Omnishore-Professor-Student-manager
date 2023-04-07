package backend.server.service.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class Student {
    private Long id;
    private String name;
    private String email;
    private String telephone;
    private String cin;
    private String groupe;
    private List<Long> professors = new ArrayList<>();
}
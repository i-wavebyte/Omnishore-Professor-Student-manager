package backend.server.service.domain;


import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String name;
    private String email;
    private String telephone;
    private String cin;
    private String groupe;
    @ElementCollection
    @CollectionTable(name = "student_professors", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "professor_id")
    private List<Long> professors = new ArrayList<>();
}

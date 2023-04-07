package backend.server.service.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ProfessorServiceAuthObject {
    private String username;
    private String password;
}

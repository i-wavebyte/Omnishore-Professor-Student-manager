package backend.server.service.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;

@Data @AllArgsConstructor @NoArgsConstructor
public class StudentServiceAuthObject {
    private String username;
    private String password;
}

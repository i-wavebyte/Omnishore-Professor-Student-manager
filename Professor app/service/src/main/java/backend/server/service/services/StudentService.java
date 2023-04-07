package backend.server.service.services;


import backend.server.service.domain.Student;
import backend.server.service.domain.StudentServiceAuthObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudentService<IOException extends Throwable> implements IStudentService {

    @Value("${backend.app.StudentBackend.login}")
    private String username;
    @Value("${backend.app.StudentBackend.password}")
    private String password;
    private final String uri ="http://localhost:8081";


    public StudentService() {

    }

    public String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();

        log.info("username: {} ;password: {}", username, password);
        StudentServiceAuthObject requestBody = new StudentServiceAuthObject(username, password);
        HttpEntity<StudentServiceAuthObject> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8081/api/auth/signin",
                HttpMethod.POST,
                entity,
                String.class
        );

        log.info("Response: {}", response.getBody());
        return extractTokenFromResponse(response.getBody(), objectMapper);
    }

    private String extractTokenFromResponse(String responseBody, ObjectMapper objectMapper) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("token").asText();
        } catch (Exception e) {
            log.error("Error parsing JSON response", e);
            throw new RuntimeException("Failed to parse token from response", e);
        }
    }

    @Override
    public Student getById(Long id) {
        String accessToken = getAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Student> response = restTemplate.exchange(
                "http://localhost:8081/studentService/find/" + id,
                HttpMethod.GET,
                entity,
                Student.class);

        return response.getBody();
    }

    @Override
    public List<Student> getAllStudents() {
        String accessToken = getAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Student[]> response = restTemplate.exchange(
                "http://localhost:8081/studentService/findAll",
                HttpMethod.GET,
                entity,
                Student[].class);

        return Arrays.asList(response.getBody());
    }

    @Override
    public List<Student> getStudentsByList(List<Long> studentIds) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("http://localhost:8081/studentService/getIdsList")
                .queryParam("studentIds", String.join(",", studentIds.stream().map(Object::toString).collect(Collectors.toList())));

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Student>>() {});

        return response.getBody();
    }

    //codegpt implement this method, here is a call example: http.localhost:8081/studentService/assignProfessor/2?professorId=4
    @Override
    public Student assignProfessor(Long studentId, Long professorId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("http://localhost:8081/studentService/assignProfessor/{studentId}")
                .queryParam("profId", professorId);

        ResponseEntity<Student> response = restTemplate.exchange(
                uriBuilder.buildAndExpand(studentId).toUri(),
                HttpMethod.PUT,
                entity,
                Student.class);

        return response.getBody();
    }
}
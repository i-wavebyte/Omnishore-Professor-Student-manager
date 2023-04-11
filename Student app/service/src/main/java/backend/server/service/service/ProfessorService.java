package backend.server.service.service;

import backend.server.service.domain.Professor;
import backend.server.service.domain.ProfessorServiceAuthObject;
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
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ProfessorService implements IProfessorService {
    @Value("${backend.app.ProfessorBackend.login}")
    private String username;
    @Value("${backend.app.ProfessorBackend.password}")
    private String password;
    private final String uri = "http://localhost:8080";
    public ProfessorService() {}
    //return a string representing the access token obtained from the remote server
    private String getAccessToken() {
        //RestTemplate is an object used to send an HTTP request to the remote server
        RestTemplate restTemplate = new RestTemplate();
        //The ObjectMapper class is part of the Jackson JSON parsing library for Java.It is used to map between JSON and Java objects.
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();

        log.info("username: {} ;password: {}", username, password);
        //construct the user making the request
        ProfessorServiceAuthObject requestBody = new ProfessorServiceAuthObject(username, password);
        //create a http request entity
        HttpEntity<ProfessorServiceAuthObject> entity = new HttpEntity<>(requestBody, headers);
        //the String.class parameter specifies the type of the response body that is expected from the HTTP request
        ResponseEntity<String> response = restTemplate.exchange(
                uri + "/api/auth/signin",
                HttpMethod.POST,
                entity,
                String.class
        );
        log.info("Response: {}", response.getBody());
        return extractTokenFromResponse(response.getBody(), objectMapper);
    }

    private String extractTokenFromResponse(String responseBody, ObjectMapper objectMapper) {
        try {
            //The readTree method of ObjectMapper is used to parse a JSON string into a JsonNode object, which is a tree representation of the JSON data
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("token").asText();
        } catch (Exception e) {
            log.error("Error parsing JSON response", e);
            throw new RuntimeException("Failed to parse token from response", e);
        }
    }

    @Override
    public List<Professor> getAllProfessors() {
        String accessToken = getAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Professor[]> response = restTemplate.exchange(
                uri + "/professorService/get",
                HttpMethod.GET,
                entity,
                Professor[].class);

        return Arrays.asList(response.getBody());
    }

//    @Override
//    public Professor getProfessorById(Long id) {
//        String accessToken = getAccessToken();
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//
//        HttpEntity<?> entity = new HttpEntity<>(headers);
//        ResponseEntity<Professor> response = restTemplate.exchange(
//                uri + "/professorService/get/" + id,
//                HttpMethod.GET,
//                entity,
//                Professor.class);
//        return response.getBody();
//    }

    @Override
    public List<Professor> getFromIdList(List<Long> ids) {
        String accessToken = getAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uri + "/professorService/get/list")
                .queryParam("profIds", String.join(",", ids.stream().map(Object::toString).collect(Collectors.toList())));

        ResponseEntity<List<Professor>> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Professor>>() {});
        return response.getBody();
    }

    @Override
    public Professor assignStudent(Long id, Long student) {
        String accessToken = getAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uri + "/professorService/assign/{id}")
                .queryParam("studentId", student);

        ResponseEntity<Professor> response = restTemplate.exchange(
                uriBuilder.buildAndExpand(id).toUri(),
                HttpMethod.PUT,
                entity,
                Professor.class);

        return response.getBody();
    }
}

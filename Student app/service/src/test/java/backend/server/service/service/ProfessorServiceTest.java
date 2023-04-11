package backend.server.service.service;

import backend.server.service.domain.ProfessorServiceAuthObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class ProfessorServiceTest {
    private String username = "test";
    private String password = "password";
    private String uri = "http://localhost:8080";

    @MockBean
    RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void testGetAccessToken() {
        // Given
        ProfessorServiceAuthObject requestBody = new ProfessorServiceAuthObject(username, password);
        HttpEntity<ProfessorServiceAuthObject> entity = new HttpEntity<>(requestBody);

        ResponseEntity<String> response = new ResponseEntity<String>("{\"token\":\"test_token\"}", HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.POST),
                        Mockito.eq(entity), Mockito.eq(String.class)))
                .thenReturn(response);

        // When
        String token = getAccessToken();

        // Then
        Assertions.assertEquals("test_token", token);
    }
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
    @Test
    void getAllProfessors() {
    }

    @Test
    void getFromIdList() {
    }
}
package backend.server.service;

import backend.server.service.security.entities.EROLE;
import backend.server.service.security.entities.Role;
import backend.server.service.security.entities.User;
import backend.server.service.security.repositories.RoleRepository;
import backend.server.service.security.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication @Slf4j
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    // this command line runner creates new roles and new users for testing
    @Bean
    CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            try{
                roleRepository.save(new Role(null, EROLE.ROLE_STUD_MANAGER));
                roleRepository.save(new Role(null, EROLE.ROLE_PROF_MANAGER));
                log.info("roles persisted");
            }
            catch(Exception e)
            {
                log.info("roles already exist");
            }
            try{
                User user = new User();
                user.setEmail("reda@gmail.com");
                user.setPassword(passwordEncoder.encode("password"));
                user.getRoles().add(roleRepository.findByName(EROLE.ROLE_STUD_MANAGER).orElseThrow(()-> new RuntimeException("role not found")));
                user.setUsername("reda");
                userRepository.save(user);
                log.info("reda user created");
            }
            catch(Exception e)
            {
                log.info("user already exist");
            }


        };
    }

}

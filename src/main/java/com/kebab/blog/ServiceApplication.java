package com.kebab.blog;

import com.kebab.blog.model.AppUser;
import com.kebab.blog.model.Role;
import com.kebab.blog.service.AppUserService;
import com.kebab.blog.utils.JWTUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@EnableConfigurationProperties
public class ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(AppUserService service) {
		return args -> {
			service.saveRole(new Role(null, "ROLE_USER"));
			service.saveRole(new Role(null, "ROLE_MANAGER"));
			service.saveRole(new Role(null, "ROLE_ADMIN"));

			service.saveUser(new AppUser(null, "Kebab", "1234", new ArrayList<>()));
			service.saveUser(new AppUser(null, "Kebab2", "1234", new ArrayList<>()));
			service.saveUser(new AppUser(null, "Kebab3", "1234", new ArrayList<>()));
			service.saveUser(new AppUser(null, "Kebab4", "1234", new ArrayList<>()));

			service.addRoleToUser("Kebab", "ROLE_ADMIN");
			service.addRoleToUser("Kebab", "ROLE_MANAGER");
			service.addRoleToUser("Kebab", "ROLE_USER");
			service.addRoleToUser("Kebab2", "ROLE_USER");
			service.addRoleToUser("Kebab3", "ROLE_USER");
			service.addRoleToUser("Kebab4", "ROLE_USER");
		};
	}
}

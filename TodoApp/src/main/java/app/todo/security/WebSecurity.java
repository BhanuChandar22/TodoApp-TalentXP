package app.todo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurity {
	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception
	{
		http.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests((req)-> req.requestMatchers("/users/createUser").permitAll()
									.anyRequest().authenticated()
								);
		
		return http.build();
	}
}

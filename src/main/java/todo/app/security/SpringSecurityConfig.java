package todo.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import todo.app.service.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SpringSecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity security, UserService userService,
			BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = security
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

		/*AuthenticationFilter filter = new AuthenticationFilter(userService, jwtService);
		filter.setAuthenticationManager(authenticationManager);*/

		return security.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(request -> request
						.requestMatchers("/register","/getAllUsers", "/", "/h2-console/**", "/login/**", "/todo/getAllTodos")
						.permitAll()
						// .requestMatchers("/delete/{userId}").hasAnyRole("ADMIN")
						.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilter(new AuthenticationFilter(authenticationManager))
				.addFilter(new AuthorizationFilter(authenticationManager)).authenticationManager(authenticationManager)
				.headers(headers -> headers.frameOptions(frames -> frames.sameOrigin())).build();
	}
}
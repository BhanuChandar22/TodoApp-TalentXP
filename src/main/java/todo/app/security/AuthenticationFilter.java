package todo.app.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import todo.app.SpringApplicationContext;
import todo.app.constants.SecurityConstants;
import todo.app.dto.UserRequest;
import todo.app.exception.UserNotFoundException;
import todo.app.service.JwtService;
import todo.app.service.UserService;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			UserRequest userRequest = new ObjectMapper().readValue(request.getInputStream(), UserRequest.class);
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(userRequest.getId(), userRequest.getPassword()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String id = ((UserPrincipal) authResult.getPrincipal()).getUsername();
		UserService userService = SpringApplicationContext.fetchBean("userServiceImpl", UserService.class);
		UserRequest userRequest = null;
		try {
			userRequest = userService.findById(id);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		String token = SpringApplicationContext.fetchBean("jwtService", JwtService.class)
				.generateToken(userRequest.getId());

		response.setHeader(SecurityConstants.AUTH_HEADER, SecurityConstants.TOKEN_TYPE + token);
		response.setHeader(SecurityConstants.USER_ID, userRequest.getId());
	}
}
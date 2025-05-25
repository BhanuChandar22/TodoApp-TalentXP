package todo.app.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import todo.app.SpringApplicationContext;
import todo.app.constants.SecurityConstants;
import todo.app.entity.UserEntity;
import todo.app.repository.UserRepository;
import todo.app.service.JwtService;

public class AuthorizationFilter extends BasicAuthenticationFilter {

	public AuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String authToken = request.getHeader(SecurityConstants.AUTH_HEADER);
		String userId = null;
		String token = null;
		JwtService jwtService = SpringApplicationContext.fetchBean("jwtService", JwtService.class);
		if (authToken != null && authToken.startsWith(SecurityConstants.TOKEN_TYPE)) {
			token = authToken.substring(7).trim();
			userId = jwtService.extractUserId(token);
		}
		if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserEntity userEntity = SpringApplicationContext.fetchBean("userRepository", UserRepository.class)
					.findById(userId).orElseThrow();
			UserPrincipal principal = new UserPrincipal(userEntity);
			if (jwtService.validateToken(token, principal)) {
				SecurityContextHolder.getContext().setAuthentication(
						new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
			}
		}
		chain.doFilter(request, response);
	}
}
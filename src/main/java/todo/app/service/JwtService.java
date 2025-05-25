package todo.app.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import todo.app.security.AppProperties;
import todo.app.security.UserPrincipal;

@Component
public class JwtService {

	@Autowired
	private AppProperties properties;

	public boolean validateToken(String token, UserPrincipal userPrincipal) {
		String userId = extractUserId(token);
		return (userId.equals(userPrincipal.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		Date expirationTime = extractExpirationTime(token);
		return expirationTime.before(new Date());
	}

	public Date extractExpirationTime(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public String extractUserId(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey(properties.getSecret())).build().parseClaimsJws(token)
				.getBody();
	}

	public String generateToken(String userId) {
		Map<String, Object> map = new HashMap<>();
		return createToken(map, userId);
	}

	private String createToken(Map<String, Object> map, String userId) {
		return Jwts.builder().setClaims(map).setSubject(userId).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(properties.getExpirationTime())))
				.signWith(getSignKey(properties.getSecret()), SignatureAlgorithm.HS512).compact();
	}

	private Key getSignKey(String secret) {
		byte[] decode = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(decode);
	}
}

package todo.app.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import todo.app.SpringApplicationContext;
import todo.app.constants.SecurityConstants;
import todo.app.dto.UserRequest;
import todo.app.dto.UserResponse;
import todo.app.entity.RoleEntity;
import todo.app.entity.UserEntity;
import todo.app.exception.UserNotFoundException;
import todo.app.repository.RoleRepository;
import todo.app.repository.UserRepository;
import todo.app.security.UserPrincipal;
import todo.app.singleton.SingletonModelMapper;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Override
	public boolean addUser(UserRequest request) {
		UserEntity entity = SingletonModelMapper.mapData(request, UserEntity.class);
		entity.setId(UUID.randomUUID().toString());
		String passowrd = encoder.encode(request.getPassword());
		entity.setPassword(passowrd);

		Collection<RoleEntity> roleEntities = new ArrayList<RoleEntity>();
		request.getRoles().forEach(role -> {
			RoleEntity roleEntity = roleRepository.findByName(role);
			if (roleEntity != null) {
				roleEntities.add(roleEntity);
			}
		});
		entity.setRoles(roleEntities);
		UserEntity userEntity = userRepository.save(entity);
		return userEntity != null;
	}

	@Override
	public boolean generateNewToken(HttpServletRequest request, HttpServletResponse response) {
		JwtService jwtService = SpringApplicationContext.fetchBean("jwtService", JwtService.class);
		String authHeader = request.getHeader(SecurityConstants.AUTH_HEADER);
		String userId = null;
		String oldToken = null;
		if (authHeader != null && authHeader.startsWith(SecurityConstants.TOKEN_TYPE)) {
			oldToken = authHeader.substring(SecurityConstants.TOKEN_TYPE.length()).trim();
			userId = jwtService.extractUserId(oldToken);
		} else {
			return false;
		}
		if (userId == null || oldToken == null || !existsById(userId)) {
			return false;
		}
		Date expirationTime = jwtService.extractExpirationTime(oldToken);
		if (!(expirationTime.before(new Date()))) {
			final String token = jwtService.generateToken(userId);
			response.setHeader(SecurityConstants.AUTH_HEADER, SecurityConstants.TOKEN_TYPE + token);
			return true;
		}
		return false;
	}

	@Override
	public boolean existsById(String userId) {
		return userRepository.existsById(userId);
	}

	@Override
	public UserRequest findById(String id) throws UserNotFoundException {
		UserEntity entity = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(SecurityConstants.USER_NOT_FOUND));

		Collection<String> roles = new HashSet<>();
		entity.getRoles().forEach(roleEntity -> roles.add(roleEntity.getName()));
		UserRequest userRequest = new UserRequest();
		userRequest.setRoles(roles);

		return SingletonModelMapper.mapData(entity, userRequest);
	}

	@Override
	public UserRequest findUserByEmail(String email) throws UserNotFoundException {
		UserEntity entity = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(SecurityConstants.USER_NOT_FOUND));
		Collection<String> roles = new HashSet<>();
		entity.getRoles().forEach(roleEntity -> roles.add(roleEntity.getName()));

		UserRequest userRequest = new UserRequest();
		userRequest.setRoles(roles);
		return SingletonModelMapper.mapData(entity, userRequest);
	}

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		UserEntity entity = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException(SecurityConstants.USER_NOT_FOUND));
		return new UserPrincipal(entity);
	}

	@Override
//	@ExecutionTime
	public List<UserResponse> getAllUsers() {
		List<UserEntity> list = userRepository.findAll();
		if (!list.isEmpty()) {
			List<UserResponse> responses = new ArrayList<>();
			for (UserEntity entity : list) {
				Collection<String> roles = new HashSet<>();
				Collection<RoleEntity> roleEntities = entity.getRoles();
				roleEntities.forEach(roleEntity -> roles.add(roleEntity.getName()));

				UserResponse userResponse = new UserResponse();
				userResponse.setRoles(roles);

				responses.add(SingletonModelMapper.mapData(entity, userResponse));
			}
			return responses;
		}
		throw new RuntimeException(SecurityConstants.DATA_NOT_FOUND);
	}

	@Override
	public String updateUser(UserRequest request) throws UserNotFoundException {
		UserEntity entity = userRepository.findById(request.getId())
				.orElseThrow(() -> new UserNotFoundException(SecurityConstants.USER_NOT_FOUND));
		SingletonModelMapper.mapData(request, entity);
		userRepository.save(entity);
		return SecurityConstants.USER_UPDATED;
	}

	@Override
	public String deleteUserById(String userId) throws UserNotFoundException {
		UserEntity entity = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(SecurityConstants.USER_NOT_FOUND));
		userRepository.delete(entity);
		return "User Deleted " + userId;
	}
}
package todo.app.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import todo.app.dto.UserRequest;
import todo.app.dto.UserResponse;
import todo.app.exception.UserNotFoundException;

public interface UserService extends UserDetailsService {
	boolean addUser(UserRequest request);

	String updateUser(UserRequest request) throws UserNotFoundException;

	List<UserResponse> getAllUsers();
	
	boolean existsById(String userId);
	
	boolean generateNewToken(HttpServletRequest request,HttpServletResponse response);

	UserRequest findById(String id) throws UserNotFoundException;

	UserRequest findUserByEmail(String email) throws UserNotFoundException;
	
	String deleteUserById(String userId) throws UserNotFoundException;
}
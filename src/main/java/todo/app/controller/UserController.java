package todo.app.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import todo.app.annotation.Crypto;
import todo.app.dto.UserRequest;
import todo.app.dto.UserResponse;
import todo.app.exception.UserNotFoundException;
import todo.app.service.UserService;
import todo.app.shared.Role;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private UserService service;

	@GetMapping("/")
	public String home() {
		return "Welcome to Spring Rest";
	}

	@Crypto
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody UserRequest request) throws IOException {
		request.setRoles(new HashSet<>(Arrays.asList(Role.ROLE_USER.name())));
		boolean user = service.addUser(request);
		if (user) {
			return ResponseEntity.status(HttpStatus.CREATED).body("Created");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in creating (:");
	}

	@PostMapping("/newToken")
	public ResponseEntity<String> generateNewTokenByUserConsentWithoutReLogin(HttpServletRequest request,
			HttpServletResponse response) {
		boolean isTokenGenerated = service.generateNewToken(request, response);
		if(isTokenGenerated)
			return ResponseEntity.ok().body("You can continue");
		else
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

//	@PostMapping("/reset")
//	public ResponseEntity<String> resetPassword(String oldPassword, String newPassword) {
//		return null;
//	}

	@GetMapping("/getAllUsers")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		List<UserResponse> users = service.getAllUsers();
		if (users.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(users);
		}
		return ResponseEntity.status(HttpStatus.FOUND).body(users);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateUser(@RequestBody UserRequest request) throws Exception {
		String updateUser = service.updateUser(request);
		return ResponseEntity.status(HttpStatus.OK).body(updateUser);
	}

	@PreAuthorize("hasRole('ADMIN') or #userId==principal.userId")
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<String> deleteUserData(@PathVariable String userId) throws UserNotFoundException {
		String response = service.deleteUserById(userId);
		return ResponseEntity.status(HttpStatus.GONE).body(response);
	}
}
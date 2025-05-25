package todo.app.dto;

import java.util.Collection;

import lombok.Data;

@Data
public class UserResponse {
	private String id;
	private String name;
	private String email;
	private Long mobile;
	private String address;
	private Collection<String> roles;
}
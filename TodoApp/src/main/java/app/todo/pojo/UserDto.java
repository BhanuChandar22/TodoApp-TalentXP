package app.todo.pojo;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String firstName;
	private String lastName;
	private String emailId;
	private String password;
	private String encryptedPassword;
}

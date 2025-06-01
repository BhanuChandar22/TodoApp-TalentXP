package app.todo.pojo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data 
public class UserRequestModel {
	@NotNull(message = "First name must not be null")
	@Size(min = 3,message = "First name cannot be less than 3 char's")
	private String firstName;
	
	@NotNull(message = "Last name must not be null")
	@Size(min = 3,message = "Last name cannot be less than 3 char's")
	private String lastName;
	
	@NotNull(message = "Email must not be null")
	@Email()
	private String emailId;
	
	@NotNull(message = "Password must not be null")
	@Size(min = 4, max = 16, message = "Password must be in the range of 4 to 16")
	private String password;
}

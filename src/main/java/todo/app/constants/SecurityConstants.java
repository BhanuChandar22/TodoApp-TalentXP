package todo.app.constants;

public class SecurityConstants {
	public static final String AUTH_HEADER = "Authorization";
	public static final String TOKEN_TYPE = "Bearer ";
	public static final String TOKEN = "token";
	public static final String USER_ID = "User_Id";
	public static final String USER_NOT_FOUND = "User Not Found";
	public static final String DATA_NOT_FOUND = "No Data Found";
	public static final String USER_UPDATED = "User Updated";
	
	
	public String getSecretKey() {
		return "";
	}
	private SecurityConstants() {}
}
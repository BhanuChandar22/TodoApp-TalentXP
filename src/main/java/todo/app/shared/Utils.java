package todo.app.shared;

import java.security.SecureRandom;
import java.util.Random;

public class Utils {

	private static final Random random = new SecureRandom();
	private static final String BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	public static String generateUserId(int length) {
		return generateSecureRandomId(length);
	}

	private static String generateSecureRandomId(int length) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(BASE64.length());
			char ch = BASE64.charAt(index);
			builder.append(ch);
		}
		return builder.toString();
	}
	
	private Utils() {}
}

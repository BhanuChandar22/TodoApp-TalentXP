package todo.app.aop;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import todo.app.annotation.Crypto;
import todo.app.annotation.ExecutionTime;
import todo.app.dto.UserRequest;

@Aspect
@Component
public class SpringAOP {

	@Around(value = "@annotation(executionTime)")
	public Object calculateTime(ProceedingJoinPoint proceedingJoinPoint, ExecutionTime executionTime) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = proceedingJoinPoint.proceed();
		long totalTimeTaken = System.currentTimeMillis() - start;
		System.out.println("Total Time Taken :: " + totalTimeTaken);
		return result;
	}

	@Around(value = "@annotation(crypto)")
	public Object validateRequestBody(ProceedingJoinPoint proceedingJoinPoint, Crypto crypto) throws Throwable {
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			List<String> values = extractStringValues(arg);
			values.forEach(val -> {
				if (val.matches(".*[<>].*")) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input :: "+val);
				}
			});
		}
		return proceedingJoinPoint.proceed();
	}

	private static List<String> extractStringValues(Object args) {
		UserRequest request = (UserRequest) args;
		List<String> list = new ArrayList<>();
		list.add(request.getName());
		list.add(request.getEmail());
		list.add(request.getAddress());
		return list;
	}
}

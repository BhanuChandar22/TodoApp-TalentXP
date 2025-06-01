package app.todo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import app.todo.annotation.ExecutionTime;

@Component
@Aspect
public class TodoAspect {
	@Around("@annotation(executionTime)")
	public Object calculateExecutionTime(ProceedingJoinPoint point, ExecutionTime executionTime) throws Throwable {
		long start = System.currentTimeMillis(); 
		
		Object proceed = point.proceed();
		
		long totalExecutionTime = System.currentTimeMillis() - start;
		System.out.println("The Method executionTime is "+ totalExecutionTime);
		
		return proceed;
	}
}

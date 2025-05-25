package todo.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import todo.app.dto.TodoRequest;
import todo.app.service.TodoServiceImpl;
import todo.app.todo.entity.TodoResponse;

@RestController
@RequestMapping("/todo/")
public class TodoController {

	@Autowired
	private TodoServiceImpl service;

	@PostMapping("create")
	@PreAuthorize("hasAnyRole('ADMIN','USER')") // Any one can create to do
	public ResponseEntity<TodoResponse> createTodo(@RequestBody TodoRequest request, Authentication authentication) {
		TodoResponse todoResponse = service.createTodo(request);
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
		if (todoResponse == null) {
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some Error");
		}
		if(isAdmin)
		{
			return ResponseEntity.status(HttpStatus.CREATED).body(todoResponse);
		}else {
			return ResponseEntity.status(HttpStatus.CREATED).build(); 
		}
	}

	@GetMapping("getAllTodos")
	public ResponseEntity<List<TodoResponse>> getAllTodos() {
		List<TodoResponse> list = service.getAllTodos();
		if(list.isEmpty())
			return  ResponseEntity.noContent().build();
		else
			return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@PutMapping("update")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<TodoResponse> updateTodo(@RequestBody TodoRequest request) {
		TodoResponse todoResponse = service.updateTodo(request);
		if(todoResponse != null)
			return ResponseEntity.ok(todoResponse);
		else		
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@DeleteMapping("delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteTodoById(@PathVariable Long id) {
		boolean response = service.deleteTodoById(id);
		if(response)
			return ResponseEntity.ok("Deleted");
		else
			return ResponseEntity.notFound().build();
	}
}
/*getAll todo's(everyone),create todo (request can be sent only by admin and 
 * logged in user,response can be only viewed by only admin),
 * delete todo(only admin ),update todo(logged in user and admin)*/
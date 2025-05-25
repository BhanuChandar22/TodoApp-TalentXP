package todo.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import todo.app.dto.TodoRequest;
import todo.app.repository.TodoRepository;
import todo.app.singleton.SingletonModelMapper;
import todo.app.todo.entity.ToDoEntity;
import todo.app.todo.entity.TodoResponse;

@Service
public class TodoServiceImpl {

	@Autowired
	private TodoRepository repository;

	public TodoResponse createTodo(TodoRequest request) {
		ToDoEntity toDoEntity = SingletonModelMapper.mapData(request, ToDoEntity.class);
		toDoEntity.setStartTime(new Date());
		toDoEntity.setEndTime(new Date(System.currentTimeMillis() + (1000 * 60)));
		toDoEntity = repository.save(toDoEntity);
		return SingletonModelMapper.mapData(toDoEntity, TodoResponse.class);
	}

	public List<TodoResponse> getAllTodos() {
		List<ToDoEntity> list = repository.findAll();
		List<TodoResponse> responses = new ArrayList<>();
		list.forEach(todoEntity -> {
			responses.add(SingletonModelMapper.mapData(todoEntity, TodoResponse.class));
		});
		return responses;
	}

	public TodoResponse updateTodo(TodoRequest request) {
		ToDoEntity toDoEntity = repository.findById(request.getId()).orElseThrow();
		toDoEntity.setName(request.getName());
		toDoEntity.setDescription(request.getDescription());
		toDoEntity.setEndTime(new Date());
		ToDoEntity toDoEntity2 = repository.save(toDoEntity);
		return SingletonModelMapper.mapData(toDoEntity2, TodoResponse.class);
	}

	public boolean deleteTodoById(Long id) {
		Optional<ToDoEntity> optional = repository.findById(id);
		if (optional.isPresent()) {
			repository.deleteById(id);
			return true;
		}
		return false;
	}
}
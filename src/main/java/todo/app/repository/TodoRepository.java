package todo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import todo.app.todo.entity.ToDoEntity;

@Repository
public interface TodoRepository extends JpaRepository<ToDoEntity, Long> {

}
package app.todo.repository;

import org.springframework.data.repository.CrudRepository;

import app.todo.entity.UserEntity;

public interface IUserRepo extends CrudRepository<UserEntity, Long> {

}

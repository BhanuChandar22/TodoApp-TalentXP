package todo.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import todo.app.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
	Optional<UserEntity> findByEmail(String email);
	boolean existsByEmail(String email);
}
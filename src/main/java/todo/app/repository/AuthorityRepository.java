package todo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import todo.app.entity.AuthorityEntity;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {

	AuthorityEntity findByName(String name);

}
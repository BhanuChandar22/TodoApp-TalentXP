package todo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import todo.app.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

	RoleEntity findByName(String name);

}
package todo.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import todo.app.entity.AuthorityEntity;
import todo.app.entity.RoleEntity;
import todo.app.entity.UserEntity;
import todo.app.repository.AuthorityRepository;
import todo.app.repository.RoleRepository;
import todo.app.repository.UserRepository;
import todo.app.security.AppProperties;
import todo.app.shared.Role;
import todo.app.shared.Utils;

@Component
public class InitialSetUp {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private AppProperties properties;

	@Transactional
	@EventListener
	public void setUpAdmin(ApplicationReadyEvent event) {
		System.out.println("InitialSetUp.setUpAdmin()" + event.getClass().getName());

		AuthorityEntity readAuthority = saveAuthorities("read_authority");
		AuthorityEntity writeAuthority = saveAuthorities("write_authority");
		AuthorityEntity deleteAuthority = saveAuthorities("delete_authority");

		saveRoles(Role.ROLE_USER.name(), new ArrayList<>(Arrays.asList(readAuthority, writeAuthority)));
		RoleEntity roleAdmin = saveRoles(Role.ROLE_ADMIN.name(),
				new ArrayList<>(Arrays.asList(readAuthority, writeAuthority, deleteAuthority)));

		if (roleAdmin == null) {
			System.out.println("Admin Role is Null,cannot proceed");
			return;
		}
		UserEntity admin = new UserEntity();
		admin.setId(Utils.generateUserId(15));
		admin.setName("Bhanu Chandar");
		admin.setPassword(encoder.encode(properties.getDefaultAdminPswrd()));
		admin.setEmail(properties.getAdminMail());
		admin.setMobile(9640139672L);
		admin.setAddress("Hyderabad");
		admin.setRoles(new ArrayList<>(Arrays.asList(roleAdmin)));

		if (!userRepository.existsByEmail(admin.getEmail())) {
			userRepository.save(admin);
		}
	}

	private RoleEntity saveRoles(String name, Collection<AuthorityEntity> authorityEntities) {
		RoleEntity roleEntity = roleRepository.findByName(name);
		if (roleEntity == null) {
			roleEntity = new RoleEntity(name);
			roleEntity.setAuthorities(authorityEntities);
			return roleRepository.save(roleEntity);
		}
		return roleEntity;
	}

	private AuthorityEntity saveAuthorities(String name) {
		AuthorityEntity authorityEntity = authorityRepository.findByName(name);
		if (authorityEntity == null) {
			authorityEntity = new AuthorityEntity(name);
			return authorityRepository.save(authorityEntity);
		}
		return authorityEntity;
	}
}
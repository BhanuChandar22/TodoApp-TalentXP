package todo.app.security;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import todo.app.entity.RoleEntity;
import todo.app.entity.UserEntity;

@Component
public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final transient UserEntity entity;
	private final String userId;

	public UserPrincipal(UserEntity entity) {
		this.entity = entity;
		this.userId = this.entity.getId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new HashSet<>();
//		Collection<AuthorityEntity> authorityEntities = new HashSet<>();
		Collection<RoleEntity> roles = entity.getRoles();
		if (roles == null) {
			return authorities;
		}
		roles.forEach(roleEntity -> {
			authorities.add(new SimpleGrantedAuthority(roleEntity.getName()));
			
			roleEntity.getAuthorities().forEach(authorityEntity -> {
				authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()));
			});
		});
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.entity.getPassword();
	}

	@Override
	public String getUsername() {
		return this.entity.getId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getUserId() {
		return userId;
	}
}
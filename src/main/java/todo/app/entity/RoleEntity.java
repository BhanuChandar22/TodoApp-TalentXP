package todo.app.entity;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "Role_Entity")
public class RoleEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NonNull
	private String name;

	@ManyToMany(mappedBy = "roles")
	@JsonIgnore
	private Collection<UserEntity> users;

	@ManyToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JoinTable(name = "Roles_Authorities", joinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "authorities_id", referencedColumnName = "id"))
	private Collection<AuthorityEntity> authorities;
}
package todo.app.entity;

import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "UserEntity")
public class UserEntity {
	@Id
	private String id;
	private String name;
	private String password;
	private String email;
	private Long mobile;
	private String address;
	
	@ManyToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JoinTable(name="User_Roles",joinColumns = @JoinColumn(name = "userId",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name="roleId",referencedColumnName = "id"))
	private Collection<RoleEntity> roles;
}
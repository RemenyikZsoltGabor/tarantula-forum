package hu.remzso.tarantulaForum.entities;




import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Column(name = "FIRST_NAME")
	String firstName;
	@Column(name = "LAST_NAME")
	String lastName;
	@Column(name = "AGE")
	int age;
	@Column(name ="EMAIL")
	String email;
	@Column(name = "USERNAME")
	String username;
	@Column(name = "PASSWORD")
	String password;
	@Column(name = "ADDRESS")
	@OneToMany(mappedBy ="user", cascade = CascadeType.ALL)
	List<Address> addresses;
}

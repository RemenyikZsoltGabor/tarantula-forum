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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import hu.remzso.tarantulaForum.converters.UserSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
@JsonSerialize(using = UserSerializer.class)
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
	@Column(name = "EMAIL")
	String email;
	@Column(name = "USERNAME")
	String username;
	@Column(name = "PASSWORD")
	String password;
	@Column(name = "ADDRESS")
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	List<Address> addresses;
}

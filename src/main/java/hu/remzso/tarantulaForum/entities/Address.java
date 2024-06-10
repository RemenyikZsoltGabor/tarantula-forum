package hu.remzso.tarantulaForum.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Column(name = "COUNTRY")
	String country;
	@Column(name = "CITY")
	String city;
	@Column(name = "STREET")
	String street;
	@Column(name = "NUMBER")
	String number;
	@Column(name = "postCode")
	int postCode;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	User user;
}

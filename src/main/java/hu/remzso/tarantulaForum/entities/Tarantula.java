package hu.remzso.tarantulaForum.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Tarantula {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Column(name = "GENUS")
	String genus;
	@Column(name = "SPIECES")
	String spieces;
	@Column(name ="LOCATION")
	String location;
	@Column(name = "COLOR")
	String color;
	@Column(name = "BODY_SIZE")
	long bodyLength;
	@Column(name = "LEGSPAN")
	long legspan;
	@Column(name = "AGRESSIVENESS")
	String aggressiveness;
	@Column(name = "DESCRIPTION")
	String description;
	@Column(name = "KEEP_INFO")
	String keepInfo;
	@Column(name = "BREED_INFO")
	String breedInfo;
	
	
	
}

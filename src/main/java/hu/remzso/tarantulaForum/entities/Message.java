package hu.remzso.tarantulaForum.entities;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "TITTLE")
	private String tittle;
	
	@ManyToOne
	private User sender;

	@ManyToMany
	private Set<User> recipients;

	@Lob
	@Column(name = "CONTENT")
	private String content;

	@Column(name = "CREATED_AT")
	private LocalDateTime createdAt;

	@Column(name = "IS_READED")
	private boolean isReaded;

}

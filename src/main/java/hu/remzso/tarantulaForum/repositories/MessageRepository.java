package hu.remzso.tarantulaForum.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.remzso.tarantulaForum.entities.Message;
@Transactional
public interface MessageRepository extends JpaRepository<Message, Long> {

	@Query("SELECT m FROM Message m JOIN m.recipients r WHERE r.id = :userId AND m.isReaded = false")
	List<Message> findUnreadMessagesByUserId(@Param("userId") Long userId);
	@Query("SELECT m FROM Message m JOIN m.recipients r WHERE r.id = :userId")
	List<Message> findMessagesByUserId(@Param("userId") Long userId);
}

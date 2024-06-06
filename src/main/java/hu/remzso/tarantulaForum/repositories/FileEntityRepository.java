package hu.remzso.tarantulaForum.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.remzso.tarantulaForum.entities.FileEntity;

public interface FileEntityRepository extends JpaRepository<FileEntity, Long> {
	
	 Optional<FileEntity> findFirstByTarantulaIDOrderByIdAsc(Long tarantulaId);
	Long countByTarantulaID(Long tarantulaId);
	Optional<List<FileEntity>> findAllByTarantulaID(Long tarantulaId);
}

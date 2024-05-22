package hu.remzso.tarantulaForum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.remzso.tarantulaForum.entities.FileEntity;

public interface FileEntityRepository extends JpaRepository<FileEntity, Long> {
	
	FileEntity findFirstByOrderByIdAsc();
}

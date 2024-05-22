package hu.remzso.tarantulaForum.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.remzso.tarantulaForum.entities.Tarantula;

public interface TarantulaRepository extends JpaRepository<Tarantula, Long>{
	Optional<Tarantula> findByGenusAndSpieces(String genus, String spiece);
}

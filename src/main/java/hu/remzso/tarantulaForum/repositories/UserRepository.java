package hu.remzso.tarantulaForum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.remzso.tarantulaForum.entities.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}

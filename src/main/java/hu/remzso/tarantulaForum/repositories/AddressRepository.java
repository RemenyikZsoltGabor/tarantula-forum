package hu.remzso.tarantulaForum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.remzso.tarantulaForum.entities.Address;

public interface AddressRepository extends JpaRepository<Address,Long> {

}

package hu.remzso.tarantulaForum.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.remzso.tarantulaForum.entities.Address;
import hu.remzso.tarantulaForum.repositories.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {
	@Autowired
	private AddressRepository addressRepository;

	@Override
	public Address saveAddress(Address address) {
		
		return addressRepository.save(address);

	}

}

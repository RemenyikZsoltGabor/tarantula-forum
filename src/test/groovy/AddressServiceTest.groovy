import hu.remzso.tarantulaForum.entities.Address
import hu.remzso.tarantulaForum.entities.User
import hu.remzso.tarantulaForum.repositories.AddressRepository
import hu.remzso.tarantulaForum.services.AddressService
import hu.remzso.tarantulaForum.services.AddressServiceImpl
import spock.lang.Specification

class AddressServiceTest extends Specification{
    AddressService addressService
    User user

    def setup(){
        addressService = Mock(AddressServiceImpl)
        user = Mock(User)
    }

    def"Save a new address instance - positive case"(){
        setup:
            def address = new Address()
            address.setCity("Szoboszlo")
            address.setCountry("Hungary")
            address.setId(11L)
            address.setNumber("21")
            address.setPostCode(1234)
            address.setStreet("Major")
            address.setUser(user)

        when:
            def savedAddress = addressService.saveAddress(address)
        then:

        1 * addressService.saveAddress(address) >> new Address(11L,"Hungary","Szoboszlo","Major","21",1234,user)
        address == savedAddress
        0 * _
    }
}

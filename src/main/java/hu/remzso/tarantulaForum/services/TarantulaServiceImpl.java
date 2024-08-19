package hu.remzso.tarantulaForum.services;

import hu.remzso.tarantulaForum.entities.Tarantula;
import hu.remzso.tarantulaForum.exceptions.TarantulaNotFoundException;
import hu.remzso.tarantulaForum.repositories.TarantulaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class TarantulaServiceImpl implements TarantulaService {
    TarantulaRepository tarantulaRepository;
 public TarantulaServiceImpl(TarantulaRepository tarantulaRepository){
     this.tarantulaRepository = tarantulaRepository;
 }

    @Override
    public Tarantula getTarantula(Long tarantulaId) {
        Optional<Tarantula> tarantula = tarantulaRepository.findById(tarantulaId);

        return tarantula.orElseThrow(TarantulaNotFoundException::new);
    }

    @Override
    public List<Tarantula> getAllTarantulas() {
        return tarantulaRepository.findAll();
    }

    @Override
    public Tarantula saveTarantula(Tarantula tarantula) {
        return tarantulaRepository.save(tarantula);
    }

    @Override
    public Tarantula findByGenusAndSpecies(String genus, String species) {
        Optional<Tarantula> tarantulaOptional = tarantulaRepository.findByGenusAndSpieces(genus, species);
        return tarantulaOptional.orElse(new Tarantula());
    }

    @Override
    public boolean isTarantulaExistByGenusAndSpecies(String genus, String species) {

     return tarantulaRepository.findByGenusAndSpieces(genus,species).isPresent();
    }

}

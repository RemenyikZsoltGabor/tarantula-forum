package hu.remzso.tarantulaForum.services;

import hu.remzso.tarantulaForum.entities.Tarantula;

import java.util.List;

public interface TarantulaService {
    Tarantula getTarantula(Long tarantulaId);
    List<Tarantula> getAllTarantulas();
    Tarantula saveTarantula(Tarantula tarantula);
    Tarantula findByGenusAndSpecies(String genus, String species);
    boolean isTarantulaExistByGenusAndSpecies(String genus, String species);
}

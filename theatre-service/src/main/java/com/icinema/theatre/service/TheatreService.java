package com.icinema.theatre.service;

import org.springframework.stereotype.Service;
import com.icinema.theatre.repository.TheatreRepository;
import com.icinema.theatre.entity.Theatre;
import java.util.List;

@Service 
public class TheatreService {
    private final TheatreRepository theatreRepository;

    public TheatreService(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }

    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

    public Theatre getTheatreById(Long id) {
        return theatreRepository.findById(id).orElseThrow(() -> new RuntimeException("Theatre not found"));
    }

    public Theatre createTheatre(Theatre theatre) {
        return theatreRepository.save(theatre);
    }  

    public Theatre updateTheatre(Long id, Theatre theatreDetails) {
        Theatre theatre = getTheatreById(id);
        theatre.setName(theatreDetails.getName());
        theatre.setLocation(theatreDetails.getLocation());
        theatre.setCity(theatreDetails.getCity());
        theatre.setStatus(theatreDetails.getStatus());
        return theatreRepository.save(theatre);
    }

    public void deleteTheatre(Long id){
        if(!theatreRepository.existsById(id)) {
            throw new RuntimeException("Theatre not found");
        }
        theatreRepository.deleteById(id);
    }
}

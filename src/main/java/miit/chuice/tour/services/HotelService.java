package miit.chuice.tour.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;

import miit.chuice.tour.models.Hotel;
import miit.chuice.tour.repositories.HotelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HotelService {

    private final HotelRepository repository;

    @Autowired
    public HotelService(HotelRepository repository) {
        this.repository = repository;
    }

    public Hotel find(long id) {
        return repository.findById(id).orElse(null);
    }

    public ObservableList<Hotel> findAll() {
        return FXCollections.observableList(repository.findAll());
    }

    public List<Hotel> findByTitle(String title) {
        return repository.findByTitle(title);
    }

    public void save(Hotel hotel) {
        repository.save(hotel);
    }
}
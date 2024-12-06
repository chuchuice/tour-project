package miit.chuice.tour.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

import miit.chuice.tour.models.Hotel;
import miit.chuice.tour.repositories.HotelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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

    public void save(Hotel hotel) {
        repository.save(hotel);
    }

    public void delete(Hotel hotel) {
        repository.delete(hotel);
    }
}
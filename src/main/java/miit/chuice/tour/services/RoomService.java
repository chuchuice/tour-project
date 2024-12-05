package miit.chuice.tour.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import miit.chuice.tour.models.Human;
import miit.chuice.tour.models.Room;
import miit.chuice.tour.models.RoomAvailable;
import miit.chuice.tour.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository repository;
    private final RoomAvailableService availableService;

    @Autowired
    public RoomService(RoomRepository repository, RoomAvailableService availableService) {
        this.repository = repository;
        this.availableService = availableService;
    }

    public ObservableList<Room> findAllAvailableRoomsByHotelIdAndDate(long hotelId, LocalDate from, LocalDate to) {
        return FXCollections.observableList(repository.findAllByHotelIdAndLodgerIdIsNullAndAvailableDatesBetweenFromAndTo(hotelId, from, to));
    }

    public Room findRoomById(long id){
        return repository.findById(id);
    }

    public List<Room> findAllByHotelId(long id) {
        return repository.findAllByHotelId(id);
    }

    public void save(Room room) {
        repository.save(room);
    }

    public void save(Room room, Human human, LocalDate checkIn, LocalDate departure) {
        availableService.save(new RoomAvailable(room, human, checkIn, departure));
        repository.save(room);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
package miit.chuice.tour.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import miit.chuice.tour.models.Human;
import miit.chuice.tour.models.Room;
import miit.chuice.tour.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomService {

    private final RoomRepository repository;

    @Autowired
    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public ObservableList<Room> findAllAvailableRoomsByHotelId(long hotelId) {
        return FXCollections.observableList(repository.findAllByHotelIdAndLodgerIdIsNull(hotelId));
    }

    public ObservableList<Room> findAllRoomsByHotelId(long hotelId) {
        return FXCollections.observableList(findAllByHotelId(hotelId));
    }

    public List<Room> findAllRoomsByLodgerId(long lodgerId) {
        return repository.findAllByLodgerId(lodgerId);
    }

    public Room findRoomById(long id){
        return repository.findById(id).orElse(null);
    }

    public List<Room> findRoomsWithWaitingStatus() {
        return repository.findAllByStatusAndLodgerIdIsNotNull(Room.Status.WAITING);
    }

    public List<Room> findAllByHotelId(long id) {
        return repository.findAllByHotelId(id);
    }

    public void save(Room room) {
        repository.save(room);
    }

    public void save(Room room, Human human) {
        room.setLodger(human);
        repository.save(room);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
package miit.chuice.tour.services;

import miit.chuice.tour.models.Room;
import miit.chuice.tour.models.RoomAvailable;
import miit.chuice.tour.repositories.RoomAvailableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomAvailableService {

    private final RoomAvailableRepository repository;

    @Autowired
    public RoomAvailableService(RoomAvailableRepository repository) {
        this.repository = repository;
    }

    public List<Room> findAllRoomsByLodgerId(long id) {

        List<RoomAvailable> available = repository.findAllByLodgerId(id);
        List<Room> rooms = new ArrayList<>();

        for (RoomAvailable roomAvailable : available) {
            rooms.add(roomAvailable.getRoom());
        }

        return rooms;
    }

    public List<RoomAvailable> findAllRoomsByLodger(long id) {
        return repository.findAllByLodgerId(id);
    }

    public RoomAvailable findRoomById(long id) {
        return repository.findRoomAvailableById(id);
    }

    public void save(RoomAvailable roomAvailable) {
        repository.save(roomAvailable);
    }

    public void delete(RoomAvailable roomAvailable) {
        roomAvailable.setLodger(null);
        repository.save(roomAvailable);
    }

    public void delete(List<RoomAvailable> rooms) {
        repository.deleteAll(rooms);
    }

    public List<RoomAvailable> findAllRoomsAvailableByWaitingStatus() {
        return repository.findAllByStatus(Room.Status.WAITING);
    }

    public List<Room> findAllRoomsByWaitingStatus() {
        List<RoomAvailable> available = repository.findAllByStatus(Room.Status.WAITING);
        List<Room> rooms = new ArrayList<>();

        for (RoomAvailable roomAvailable : available) {
            rooms.add(roomAvailable.getRoom());
        }

        return rooms;
    }
}
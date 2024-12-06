package miit.chuice.tour.services;

import miit.chuice.tour.models.Room;
import miit.chuice.tour.models.RoomBooked;
import miit.chuice.tour.repositories.RoomBookedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomBookedService {

    private final RoomBookedRepository repository;

    @Autowired
    public RoomBookedService(RoomBookedRepository repository) {
        this.repository = repository;
    }

    public List<Room> findAllRoomsByLodgerId(long id) {

        List<RoomBooked> available = repository.findAllByLodgerId(id);
        List<Room> rooms = new ArrayList<>();

        for (RoomBooked roomBooked : available) {
            rooms.add(roomBooked.getRoom());
        }

        return rooms;
    }

    public List<RoomBooked> findAllRoomsByLodger(long id) {
        return repository.findAllByLodgerId(id);
    }

    public RoomBooked findRoomById(long id) {
        return repository.findRoomBookedById(id);
    }

    public void save(RoomBooked roomBooked) {
        repository.save(roomBooked);
    }

    public void delete(RoomBooked roomBooked) {
        roomBooked.setLodger(null);
        repository.save(roomBooked);
    }

    public void delete(List<RoomBooked> rooms) {
        repository.deleteAll(rooms);
    }

    public List<RoomBooked> findAllRoomsAvailableByWaitingStatus() {
        return repository.findAllByStatus(Room.Status.WAITING);
    }

    public List<Room> findAllRoomsByWaitingStatus() {
        List<RoomBooked> available = repository.findAllByStatus(Room.Status.WAITING);
        List<Room> rooms = new ArrayList<>();

        for (RoomBooked roomBooked : available) {
            rooms.add(roomBooked.getRoom());
        }

        return rooms;
    }
}
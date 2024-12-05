package miit.chuice.tour.repositories;

import miit.chuice.tour.models.Room;
import miit.chuice.tour.models.RoomAvailable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomAvailableRepository extends JpaRepository<RoomAvailable, Long> {
    List<RoomAvailable> findAllByLodgerId(long id);
    List<RoomAvailable> findAllByStatus(Room.Status status);
    RoomAvailable findRoomAvailableById(long id);
}
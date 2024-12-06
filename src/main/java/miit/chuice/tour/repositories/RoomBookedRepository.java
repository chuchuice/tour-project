package miit.chuice.tour.repositories;

import miit.chuice.tour.models.Room;
import miit.chuice.tour.models.RoomBooked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomBookedRepository extends JpaRepository<RoomBooked, Long> {
    List<RoomBooked> findAllByLodgerId(long id);
    List<RoomBooked> findAllByStatus(Room.Status status);
    RoomBooked findRoomBookedById(long id);
}
package miit.chuice.tour.repositories;

import miit.chuice.tour.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByHotelIdAndLodgerIdIsNull(long hotelId);
    List<Room> findAllByLodgerId(long lodgerId);
    List<Room> findAllByStatus(Room.Status status);
    List<Room> findAllByHotelId(long id);
}
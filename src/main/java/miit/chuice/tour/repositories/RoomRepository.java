package miit.chuice.tour.repositories;

import miit.chuice.tour.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByHotelIdAndLodgerIdIsNull(long hotelId);
    List<Room> findAllByLodgerId(long lodgerId);
    @Query("SELECT r FROM Room r WHERE r.lodger IS NOT NULL AND r.status = 'NOT_YET'")
    List<Room> findAllByStatusAndLodger();
}
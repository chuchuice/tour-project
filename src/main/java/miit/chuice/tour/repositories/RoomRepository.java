package miit.chuice.tour.repositories;

import miit.chuice.tour.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findById(long id);

    List<Room> findAllByHotelId(long hotelId);

    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId AND NOT EXISTS (" +
            "SELECT 1 FROM RoomAvailable a WHERE a.room = r AND a.checkIn >= :from AND a.departure <= :to)")
    List<Room> findAllByHotelIdAndLodgerIdIsNullAndAvailableDatesBetweenFromAndTo(
            @Param(value = "hotelId") long hotelId,
            @Param(value = "from") LocalDate from,
            @Param(value = "to") LocalDate to
    );

}
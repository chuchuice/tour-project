package miit.chuice.tour.models;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class RoomDTO {
    private long id;
    private String hotelTitle;
    private int number;
    private int countOfBeds;
    private long userId;
    private Room.Status status;
    private LocalDate checkIn;
    private LocalDate departure;

    public RoomDTO(long id, String hotelTitle, int number,
                   int countOfBeds, Room.Status status,
                   LocalDate checkIn, LocalDate departure) {
        this.id = id;
        this.hotelTitle = hotelTitle;
        this.number = number;
        this.countOfBeds = countOfBeds;
        this.status = status;
        this.checkIn = checkIn;
        this.departure = departure;
    }

    public RoomDTO(long id, String hotelTitle, int number,
                   long userId, Room.Status status,
                   LocalDate checkIn, LocalDate departure) {
        this.id = id;
        this.hotelTitle = hotelTitle;
        this.number = number;
        this.userId = userId;
        this.status = status;
        this.checkIn = checkIn;
        this.departure = departure;
    }
}
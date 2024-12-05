package miit.chuice.tour.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "room_availability")
public class RoomAvailable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "lodger_id")
    private Human lodger;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "departure", nullable = false)
    private LocalDate departure;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Room.Status status;

    public RoomAvailable(Room room, Human lodger, LocalDate checkIn, LocalDate departure) {
        this.room = room;
        this.lodger = lodger;
        this.checkIn = checkIn;
        this.departure = departure;
        this.status = Room.Status.WAITING;
    }

    public RoomAvailable() {

    }
}
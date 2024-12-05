package miit.chuice.tour.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(name = "number")
    private int number;

    @Column(name = "count_of_beds")
    private int countOfBeds;

    @Column(name = "cost")
    private int cost;


    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<RoomAvailable> availableDates;

    public Room(Hotel hotel, int number, int countOfBeds, int cost) {
        this.hotel = hotel;
        this.number = number;
        this.countOfBeds = countOfBeds;
        this.cost = cost;
    }

    public Room() {

    }

    public enum Status {
        ACCEPTED,
        REJECTED,
        NOT_YET,
        PAID,
        WAITING
    }
}
package miit.chuice.tour.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

@Setter
@Getter
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(name = "number")
    private int number;

    @Column(name = "count_of_beds")
    private int countOfBeds;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "lodger_id")
    private Human lodger;

    @Column(name = "cost")
    private int cost;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;

    public Room(Hotel hotel, int number, int countOfBeds, int cost) {
        this.hotel = hotel;
        this.number = number;
        this.countOfBeds = countOfBeds;
        this.cost = cost;
        this.status = Status.NOT_YET;
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
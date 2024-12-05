package miit.chuice.tour.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "stars")
    private int stars;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Room> rooms;

    public Hotel(String title, int stars, String country, String city, String address) {
        this.title = title;
        this.stars = stars;
        this.country = country;
        this.city = city;
        this.address = address;
    }

    public Hotel() {

    }

    public @Override String toString() {
        return this.getTitle();
    }

}
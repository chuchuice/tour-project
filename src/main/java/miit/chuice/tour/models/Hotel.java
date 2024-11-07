package miit.chuice.tour.models;

import lombok.Data;

@Data
public class Hotel {
    private Long id;
    private String title;
    private String country;
    private String city;
    private String address;
    private int stars;
}
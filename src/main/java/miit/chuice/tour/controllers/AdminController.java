package miit.chuice.tour.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import miit.chuice.tour.models.Hotel;
import miit.chuice.tour.models.Room;
import miit.chuice.tour.services.HotelService;
import miit.chuice.tour.services.RoomService;
import miit.chuice.tour.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static miit.chuice.tour.utils.SecurityUtils.makeAlert;

@Component
public class AdminController implements Initializable {

    @FXML private TextField title;
    @FXML private TextField countOfStars;
    @FXML private TextField country;
    @FXML private TextField city;
    @FXML private TextField address;

    @FXML private Button addHotel;
    @FXML private Button other;
    @FXML private Button logout;

    private final Utils utils;
    private final HotelService hotelService;

    @Autowired
    public AdminController(Utils utils, HotelService hotelService) {
        this.utils = utils;
        this.hotelService = hotelService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        logout.setOnAction(event -> {
            utils.changeScene(event, "/miit/chuice/tour/views/login.fxml", "Аутентификация");
            UserController.setTheUser(null);
        });

        addHotel.setOnAction(event -> {
            if (title.getText().isBlank() || countOfStars.getText().isBlank() || country.getText().isBlank()
                    || city.getText().isBlank() || address.getText().isBlank()) {
                makeAlert("Введите все данные для добавления отеля!", Alert.AlertType.ERROR);
            } else {
                hotelService.save(new Hotel(title.getText(), Integer.parseInt(countOfStars.getText()),
                        country.getText(), city.getText(), address.getText()));
                makeAlert("Отель успешно добавлен!", Alert.AlertType.INFORMATION);
            }
        });

        other.setOnAction(event -> utils.changeScene(event, "/miit/chuice/tour/views/admin-hotels.fxml", "Отели"));

    }
}
package miit.chuice.tour.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import miit.chuice.tour.models.Hotel;
import miit.chuice.tour.services.HotelService;
import miit.chuice.tour.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static miit.chuice.tour.utils.SecurityUtils.makeAlert;

@Slf4j
@Component
public class AdminHotelsController implements Initializable {

    @FXML private TableView<Hotel> hotels;
    @FXML private TableColumn<?, ?> address;
    @FXML private TableColumn<?, ?> city;
    @FXML private TableColumn<?, ?> country;
    @FXML private TableColumn<?, ?> hotel;
    @FXML private TableColumn<?, ?> id;
    @FXML private TableColumn<?, ?> stars;
    @FXML private TextField idToDelete;
    @FXML private TextField idToRooms;
    @FXML private Button reload;
    @FXML private Button findRooms;
    @FXML private Button delete;
    @FXML public Button addHotel;

    private final HotelService hotelService;
    private final Utils utils;

    @Autowired
    public AdminHotelsController(HotelService hotelService, Utils utils) {
        this.hotelService = hotelService;
        this.utils = utils;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reload();

        reload.setOnAction(event -> reload());

        addHotel.setOnAction(event -> utils.changeScene(event, "/miit/chuice/tour/views/admin.fxml", "Администратор"));

        findRooms.setOnAction(event -> {
            if (idToRooms.getText().isEmpty()) {
                log.info("Не указано id отеля");
                makeAlert("Вы не указали id отеля", Alert.AlertType.ERROR);
            } else {
                Hotel findedHotel = hotelService.find(Long.parseLong(idToRooms.getText()));
                if (findedHotel != null) {
                    UserController.setTheHotel(findedHotel);
                    utils.changeScene(event, "/miit/chuice/tour/views/admin-rooms.fxml","Комнаты");
                } else {
                    makeAlert("Отель не был найден в нашей базе", Alert.AlertType.ERROR);
                }
            }
        });

        delete.setOnAction(event -> {
            if (idToDelete.getText().isEmpty()) {
                makeAlert("Вы не указали id отеля", Alert.AlertType.ERROR);
            } else {
                Hotel thisHotel = hotelService.find(Long.parseLong(idToDelete.getText()));
                if (thisHotel != null) {
                    hotelService.delete(thisHotel);
                    makeAlert("Вы успешно удалили отель из базы данных", Alert.AlertType.INFORMATION);
                } else {
                    makeAlert("Отель не был найден в нашей базе", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void reload() {

        hotels.setItems(hotelService.findAll());

        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.hotel.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.stars.setCellValueFactory(new PropertyValueFactory<>("stars"));
        this.country.setCellValueFactory(new PropertyValueFactory<>("country"));
        this.city.setCellValueFactory(new PropertyValueFactory<>("city"));
        this.address.setCellValueFactory(new PropertyValueFactory<>("address"));

    }
}

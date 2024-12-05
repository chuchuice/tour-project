package miit.chuice.tour.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import miit.chuice.tour.controllers.user.UserController;
import miit.chuice.tour.models.Hotel;
import miit.chuice.tour.models.Room;
import miit.chuice.tour.services.RoomService;
import miit.chuice.tour.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static miit.chuice.tour.utils.SecurityUtils.makeAlert;

@Component
public class AdminAddRoomСontroller implements Initializable {

    @FXML private Button add;
    @FXML private Button back;
    @FXML private TextField cost;
    @FXML private TextField countOfBeds;
    @FXML private TextField number;

    private final Utils utils;
    private final RoomService roomService;

    @Autowired
    public AdminAddRoomСontroller(Utils utils, RoomService roomService) {
        this.utils = utils;
        this.roomService = roomService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        back.setOnAction(event -> utils.changeScene(event, "/miit/chuice/tour/views/admin-rooms.fxml", "Комнаты"));
        add.setOnAction(event -> {
            if (cost.getText().isBlank() || countOfBeds.getText().isBlank() || number.getText().isBlank()) {
                makeAlert("Вы не указали данные", Alert.AlertType.ERROR);
            } else {
                Hotel hotel = UserController.getTheHotel();
                roomService.save(new Room(hotel, Integer.parseInt(number.getText()),
                        Integer.parseInt(countOfBeds.getText()), Integer.parseInt(cost.getText())));
                makeAlert("Вы успешно добавили комнату в этот отель!", Alert.AlertType.INFORMATION);
            }
        });
    }

}
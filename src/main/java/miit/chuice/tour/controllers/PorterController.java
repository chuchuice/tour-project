package miit.chuice.tour.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import miit.chuice.tour.models.Human;
import miit.chuice.tour.models.Room;
import miit.chuice.tour.services.RoomService;
import miit.chuice.tour.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static miit.chuice.tour.utils.SecurityUtils.makeAlert;

@Component
public class PorterController implements Initializable {

    @FXML private TableView<Room> info;
    @FXML private TableColumn<?, ?> status;
    @FXML private TableColumn<?, ?> userId;
    @FXML private TableColumn<?, ?> email;
    @FXML private TableColumn<?, ?> hotel;
    @FXML private TableColumn<?, ?> id;
    @FXML private TableColumn<?, ?> number;
    @FXML private TextField clientId;
    @FXML private Button logout;
    @FXML private Button accept;
    @FXML private Button reject;
    @FXML private Button reload;

    private final Utils utils;
    private final RoomService roomService;

    @Autowired
    public PorterController(Utils utils, RoomService roomService) {
        this.utils = utils;
        this.roomService = roomService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        reload();

        logout.setOnAction(event -> {
            UserController.setTheUser(null);
            utils.changeScene(event, "/miit/chuice/tour/views/login.fxml", "Аутентификация");
        });

        accept.setOnAction(event -> {
            if (clientId.getText().isBlank()) {
                makeAlert("Введите id пользователя", Alert.AlertType.ERROR);
            } else {
                List<Room> rooms = roomService.findAllRoomsByLodgerId(Long.parseLong(clientId.getText()));
                if (rooms.isEmpty()) makeAlert("Этот пользователь ничего не бронировал", Alert.AlertType.ERROR);
                else {
                    for (Room room : rooms) {
                        room.setStatus(Room.Status.ACCEPTED);
                        roomService.save(room);
                    }
                    makeAlert("Статусы успешно изменены", Alert.AlertType.INFORMATION);
                    reload();
                }
            }
        });

        reject.setOnAction(event -> {
            if (clientId.getText().isBlank()) {
                makeAlert("Введите id пользователя", Alert.AlertType.ERROR);
            } else {
                List<Room> rooms = roomService.findAllRoomsByLodgerId(Long.parseLong(clientId.getText()));
                if (rooms.isEmpty()) makeAlert("Этот пользователь ничего не бронировал", Alert.AlertType.ERROR);
                else {
                    for (Room room : rooms) {
                        room.setStatus(Room.Status.REJECTED);
                        roomService.save(room);
                    }
                    makeAlert("Статусы успешно изменены", Alert.AlertType.INFORMATION);
                    reload();
                }
            }
        });
    }

    public void reload() {

        info.setItems(FXCollections.observableList(roomService.findRoomsWithWaitingStatus()));

        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.hotel.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.email.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        this.number.setCellValueFactory(new PropertyValueFactory<>("number"));
        this.status.setCellValueFactory(new PropertyValueFactory<>("status"));

    }
}
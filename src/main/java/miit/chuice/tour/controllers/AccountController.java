package miit.chuice.tour.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import miit.chuice.tour.models.Room;
import miit.chuice.tour.services.RoomService;
import miit.chuice.tour.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static miit.chuice.tour.utils.SecurityUtils.makeAlert;

@Component
public class AccountController implements Initializable {

    @FXML private TableView<Room> rooms;
    @FXML private TableColumn<?, ?> id;
    @FXML private TableColumn<?, ?> hotel;
    @FXML private TableColumn<?, ?> number;
    @FXML private TableColumn<?, ?> countOfBeds;
    @FXML private TableColumn<?, ?> status;
    @FXML private TextField idToBuy;
    @FXML private TextField idToCancel;
    @FXML private Button cancelBook;
    @FXML private Button buy;
    @FXML private Button back;
    @FXML private Button logout;
    @FXML private Button reload;

    private final Utils utils;
    private final RoomService roomService;

    @Autowired
    public AccountController(Utils utils, RoomService roomService) {
        this.utils = utils;
        this.roomService = roomService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reload();

        back.setOnAction(event -> utils.changeScene(event, "/miit/chuice/tour/views/user-hotels.fxml", "Отели"));

        logout.setOnAction(event -> {
            UserController.setTheUser(null);
            utils.changeScene(event, "/miit/chuice/tour/views/login.fxml", "Аутентификация");
        });

        reload.setOnAction(event -> reload());

        cancelBook.setOnAction(event -> {
            if (idToCancel.getText().isBlank()) {
                makeAlert("Укажите id", Alert.AlertType.ERROR);
            } else {
                Room room = roomService.findRoomById(Long.parseLong(idToCancel.getText()));
                roomService.save(room, null);
                utils.changeScene(event, "/miit/chuice/tour/views/user-hotels.fxml", "Мои комнаты");
                reload();
            }
        });

        buy.setOnAction(event -> {
            if (idToBuy.getText().isBlank()) {
                makeAlert("Введите id комнаты", Alert.AlertType.ERROR);
            } else {
                Room room = roomService.findRoomById(Long.parseLong(idToBuy.getText()));
                if (!room.getStatus().equals(Room.Status.ACCEPTED)) {
                    makeAlert("Вы ещё не можете арендовать комнату!", Alert.AlertType.ERROR);
                } else {
                    room.setStatus(Room.Status.PAID);
                    roomService.save(room);
                    makeAlert("Вы арендовали комнату", Alert.AlertType.INFORMATION);
                    reload();
                }
            }
        });

    };

    private void reload() {
        rooms.setItems(FXCollections.observableList(roomService.findAllRoomsByLodgerId(UserController.getTheUser().getId())));

        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.hotel.setCellValueFactory(new PropertyValueFactory<>("hotel"));
        this.number.setCellValueFactory(new PropertyValueFactory<>("number"));
        this.countOfBeds.setCellValueFactory(new PropertyValueFactory<>("countOfBeds"));
        this.status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

}
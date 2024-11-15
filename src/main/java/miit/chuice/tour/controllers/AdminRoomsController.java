package miit.chuice.tour.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;
import lombok.Setter;
import miit.chuice.tour.models.Room;
import miit.chuice.tour.services.RoomService;
import miit.chuice.tour.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static miit.chuice.tour.utils.SecurityUtils.makeAlert;

@Component
public class AdminRoomsController implements Initializable {

    @FXML private TableColumn<?, ?> countOfBeds;
    @FXML private TableColumn<?, ?> hotel;
    @FXML private TableColumn<?, ?> id;
    @FXML private TableColumn<?, ?> number;
    @FXML private TableColumn<?, ?> cost;
    @FXML private TableView<Room> rooms;
    @FXML private TextField room;
    @FXML private Button back;
    @FXML private Button delete;
    @FXML private Button reload;
    @FXML private Button add;

    private final RoomService roomService;
    private final Utils utils;

    @Getter @Setter private static long roomId;

    @Autowired
    public AdminRoomsController(RoomService roomService, Utils utils) {
        this.roomService = roomService;
        this.utils = utils;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reload();

        reload.setOnAction(event -> reload());

        back.setOnAction(event -> {
            utils.changeScene(event, "/miit/chuice/tour/views/admin-hotels.fxml", "Отели");
        });

        delete.setOnAction(event -> {
            if (room.getText().isEmpty()) {
                makeAlert("Пожалуйста, выберите комнату", Alert.AlertType.ERROR);
            } else {
                Room room = roomService.findRoomById(Long.parseLong(this.room.getText()));
                if (room == null) {
                    makeAlert("Комнаты не существует", Alert.AlertType.ERROR);
                } else {
                    room.setLodger(null);
                    room.setHotel(null);
                    roomService.save(room);
                    makeAlert("Вы успешно удалили комнату", Alert.AlertType.INFORMATION);
                    reload();
                }
            }
        });

        add.setOnAction(event -> {
            utils.changeScene(event, "/miit/chuice/tour/views/admin-add-room.fxml", "Добавить комнату");
        });
    }

    private void reload() {
        this.rooms.setItems(FXCollections.observableList(roomService.findAllByHotelId(UserController.getTheHotel().getId())));

        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.hotel.setCellValueFactory(new PropertyValueFactory<>("hotel"));
        this.number.setCellValueFactory(new PropertyValueFactory<>("number"));
        this.countOfBeds.setCellValueFactory(new PropertyValueFactory<>("countOfBeds"));
        this.cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }

}
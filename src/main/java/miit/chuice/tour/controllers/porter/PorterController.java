package miit.chuice.tour.controllers.porter;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import miit.chuice.tour.controllers.user.UserController;
import miit.chuice.tour.models.Room;
import miit.chuice.tour.models.RoomDTO;
import miit.chuice.tour.services.RoomAvailableService;
import miit.chuice.tour.services.RoomService;
import miit.chuice.tour.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static miit.chuice.tour.utils.SecurityUtils.makeAlert;

@Component
public class PorterController implements Initializable {

    @FXML private TableView<RoomDTO> info;
    @FXML private TableColumn<?, ?> to;
    @FXML private TableColumn<?, ?> from;
    @FXML private TableColumn<?, ?> status;
    @FXML private TableColumn<?, ?> userId;
    @FXML private TableColumn<?, ?> hotel;
    @FXML private TableColumn<?, ?> id;
    @FXML private TableColumn<?, ?> number;
    @FXML private TextField bookId;
    @FXML private Button logout;
    @FXML private Button accept;
    @FXML private Button reject;
    @FXML private Button reload;

    private final Utils utils;
    private final RoomService roomService;
    private final RoomAvailableService service;

    @Autowired
    public PorterController(Utils utils, RoomService roomService, RoomAvailableService service) {
        this.utils = utils;
        this.roomService = roomService;
        this.service = service;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reload();
        reload.setOnAction(event -> reload());

        logout.setOnAction(event -> {
            UserController.setTheUser(null);
            utils.changeScene(event, "/miit/chuice/tour/views/login.fxml", "Аутентификация");
        });

        accept.setOnAction(event -> {
            if (bookId.getText().isBlank()) {
                makeAlert("Введите id бронирования", Alert.AlertType.ERROR);
            } else {
                var room = service.findRoomById(Integer.parseInt(bookId.getText()));
                if (room == null) {
                    makeAlert("Такой комнаты нет!", Alert.AlertType.ERROR);
                } else {
                    room.setStatus(Room.Status.ACCEPTED);
                    service.save(room);
                    makeAlert("Статусы успешно изменены", Alert.AlertType.INFORMATION);
                    reload();
                }
            }
        });

        reject.setOnAction(event -> {
            if (bookId.getText().isBlank()) {
                makeAlert("Введите id бронирования", Alert.AlertType.ERROR);
            } else {
                var room = service.findRoomById(Integer.parseInt(bookId.getText()));
                if (room == null) {
                    makeAlert("Такой комнаты нет!", Alert.AlertType.ERROR);
                } else {
                    room.setStatus(Room.Status.REJECTED);
                    service.save(room);
                    makeAlert("Статусы успешно изменены", Alert.AlertType.INFORMATION);
                    reload();
                }
            }
        });
    }

    private void reload() {
        var tableRoom = service.findAllRoomsByWaitingStatus();
        var tableAvailable = service.findAllRoomsAvailableByWaitingStatus();
        List<RoomDTO> table = new ArrayList<>();

        for (int i = 0; i < tableRoom.size(); i++) {
            var room = tableRoom.get(i);
            var available = tableAvailable.get(i);

            table.add(new RoomDTO(
                    available.getId(),
                    room.getHotel().getTitle(),
                    room.getNumber(),
                    available.getLodger().getId(),
                    available.getStatus(),
                    available.getCheckIn(),
                    available.getDeparture()));
        }

        info.setItems(FXCollections.observableList(table));

        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.hotel.setCellValueFactory(new PropertyValueFactory<>("hotelTitle"));
        this.number.setCellValueFactory(new PropertyValueFactory<>("number"));
        this.userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        this.status.setCellValueFactory(new PropertyValueFactory<>("status"));
        this.from.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        this.to.setCellValueFactory(new PropertyValueFactory<>("departure"));
    }

}
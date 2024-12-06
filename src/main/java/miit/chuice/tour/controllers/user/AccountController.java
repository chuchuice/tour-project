package miit.chuice.tour.controllers.user;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import miit.chuice.tour.models.Room;
import miit.chuice.tour.models.RoomBooked;
import miit.chuice.tour.models.RoomDTO;
import miit.chuice.tour.services.RoomBookedService;
import miit.chuice.tour.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static miit.chuice.tour.utils.SecurityUtils.makeAlert;

@Component
public class AccountController implements Initializable {

    @FXML private TableView<RoomDTO> rooms;
    @FXML private TableColumn<?, ?> from;
    @FXML private TableColumn<?, ?> out;
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
    private final RoomBookedService service;

    @Autowired
    public AccountController(Utils utils, RoomBookedService service) {
        this.utils = utils;
        this.service = service;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        checkTime();
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
                var room = service.findRoomById(Integer.parseInt(idToCancel.getText()));
                if (!room.getLodger().getId().equals(UserController.getTheUser().getId())) {
                    makeAlert("Вы не бронировали эту комнату!", Alert.AlertType.ERROR);
                } else {
                    room.setStatus(Room.Status.NOT_YET);
                    service.delete(room);
                    utils.changeScene(event, "/miit/chuice/tour/views/user-hotels.fxml", "Мои комнаты");
                }
            }
        });

        buy.setOnAction(event -> {
            if (idToBuy.getText().isBlank()) {
                makeAlert("Введите id комнаты", Alert.AlertType.ERROR);
            } else {
                RoomBooked room = service.findRoomById(Integer.parseInt(idToBuy.getText()));
                if (!room.getStatus().equals(Room.Status.ACCEPTED)) {
                    makeAlert("Вы ещё не можете арендовать комнату!", Alert.AlertType.ERROR);
                } else {
                    room.setStatus(Room.Status.PAID);
                    service.save(room);
                    makeAlert("Вы арендовали комнату", Alert.AlertType.INFORMATION);
                    reload();
                }
            }
        });

    };

    private void checkTime() {

        var rooms = service.findAllRoomsByLodger(UserController.getTheUser().getId());

        for (int i = 0; i < rooms.size(); i++) {
            if (ChronoUnit.DAYS.between(rooms.get(i).getDeparture(), LocalDate.now()) >= 0) {
                service.delete(rooms.get(i));
                makeAlert("Сообщение от отеля!\nСпасибо, что посетили нас! Надеемся, вам всё понравилось. Ждём вас ещё раз! :)", Alert.AlertType.INFORMATION);
            }
        }

    }

    private void reload() {
        var tableRoom = service.findAllRoomsByLodgerId(UserController.getTheUser().getId());
        var tableAvailable = service.findAllRoomsByLodger(UserController.getTheUser().getId());
        List<RoomDTO> table = new ArrayList<>();

        for (int i = 0; i < tableRoom.size(); i++) {
            var room = tableRoom.get(i);
            var available = tableAvailable.get(i);

            table.add(new RoomDTO(
                    available.getId(),
                    room.getHotel().getTitle(),
                    room.getNumber(),
                    room.getCountOfBeds(),
                    available.getStatus(),
                    available.getCheckIn(),
                    available.getDeparture()));
        }

        rooms.setItems(FXCollections.observableList(table));

        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.hotel.setCellValueFactory(new PropertyValueFactory<>("hotelTitle"));
        this.number.setCellValueFactory(new PropertyValueFactory<>("number"));
        this.countOfBeds.setCellValueFactory(new PropertyValueFactory<>("countOfBeds"));
        this.status.setCellValueFactory(new PropertyValueFactory<>("status"));
        this.from.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        this.out.setCellValueFactory(new PropertyValueFactory<>("departure"));
    }

}
package miit.chuice.tour.controllers.user;

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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import static miit.chuice.tour.utils.SecurityUtils.makeAlert;

@Component
public class BookController implements Initializable {

    @FXML private DatePicker in;
    @FXML private DatePicker out;
    @FXML private Button search;
    @FXML private TableColumn<?, ?> countOfBeds;
    @FXML private TableColumn<?, ?> hotel;
    @FXML private TableColumn<?, ?> id;
    @FXML private TableColumn<?, ?> number;
    @FXML private TableColumn<?, ?> cost;
    @FXML private TableView<Room> rooms;
    @FXML private TextField room;
    @FXML private Button back;
    @FXML private Button book;
    @FXML private Button reload;

    private final RoomService roomService;
    private final Utils utils;

    @Autowired
    public BookController(RoomService roomService, Utils utils) {
        this.roomService = roomService;
        this.utils = utils;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        reload.setOnAction(event -> reloadRooms());

        back.setOnAction(event -> utils.changeScene(event, "/miit/chuice/tour/views/user-hotels.fxml", "Отели"));

        search.setOnAction(event -> {
            if (in.getValue() == null || out.getValue() == null) {
                makeAlert("Введите дату заезда и выезда", Alert.AlertType.ERROR);
            } else {
                reloadRooms();
            }
        });

        book.setOnAction(event -> {
           if (room.getText().isEmpty()) {
               makeAlert("Пожалуйста, выберите комнату", Alert.AlertType.ERROR);
           } else {
               if (in.getValue() == null || out.getValue() == null) {
                   makeAlert("Введите дату заезда и выезда", Alert.AlertType.ERROR);
               } else if (ChronoUnit.DAYS.between(LocalDate.now(), in.getValue()) <= 0){
                   makeAlert("Вы не можете забронировать комнату в прошлом :)", Alert.AlertType.ERROR);
               } else {
                   Room room = roomService.findRoomById(Long.parseLong(this.room.getText()));
                   if (room == null) {
                       makeAlert("Комнаты не существует", Alert.AlertType.ERROR);
                   } else {
                       if (room.getHotel().getId().longValue() != UserController.getTheHotel().getId().longValue()) {
                           makeAlert("У этого отеля нет этой свободной комнаты!", Alert.AlertType.ERROR);
                       } else {
                           roomService.save(room, UserController.getTheUser(), in.getValue(), out.getValue());
                           utils.changeScene(event, "/miit/chuice/tour/views/user-hotels.fxml", "Забронированные комнаты");
                           long sumToPay = ((ChronoUnit.DAYS.between(in.getValue(), out.getValue())) + 1) * room.getCost();
                           makeAlert("Вы забронировали комнату, ожидайте ответ от отеля!\nСумма для оплаты: " + sumToPay, Alert.AlertType.INFORMATION);
                       }
                   }
               }
           }
        });
    }

    private void reloadRooms() {
        System.out.println(UserController.getTheHotel().getTitle());
        this.rooms.setItems(roomService.findAllAvailableRoomsByHotelIdAndDate(UserController.getTheHotel().getId(),
                in.getValue(), out.getValue()));

        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.hotel.setCellValueFactory(new PropertyValueFactory<>("hotel"));
        this.number.setCellValueFactory(new PropertyValueFactory<>("number"));
        this.countOfBeds.setCellValueFactory(new PropertyValueFactory<>("countOfBeds"));
        this.cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }

}
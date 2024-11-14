package miit.chuice.tour.controllers;

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
public class BookController implements Initializable {

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
        reloadRooms();

        reload.setOnAction(event -> reloadRooms());

        back.setOnAction(event -> utils.changeScene(event, "/miit/chuice/tour/views/user-hotels.fxml", "Hotels"));

        book.setOnAction(event -> {
           if (room.getText().isEmpty()) {
               makeAlert("Пожалуйста, выберите комнату", Alert.AlertType.ERROR);
           } else {
               Room room = roomService.findRoomById(Long.parseLong(this.room.getText()));
               if (room == null) {
                   makeAlert("Комнаты не существует", Alert.AlertType.ERROR);
                   } else {
                        if (checkRoom(room)) {
                            if (room.getHotel().getId().longValue() != UserController.getTheHotel().getId().longValue()) {
                                makeAlert("У этого отеля нет этой свободной комнаты!", Alert.AlertType.ERROR);
                            } else {
                                room.setStatus(Room.Status.WAITING);
                                roomService.save(room, UserController.getTheUser());
                                utils.changeScene(event, "/miit/chuice/tour/views/user-hotels.fxml", "Забронированные комнаты");
                                makeAlert("Вы забронировали комнату, ожидайте ответ от отеля", Alert.AlertType.INFORMATION);
                       }
                   } else {
                       makeAlert("Комната уже забронирована, посмотрите другую!", Alert.AlertType.ERROR);
                   }
               }
           }
        });
    }

    public boolean checkRoom(Room room) {
        return room.getLodger() == null;
    }

    private void reloadRooms() {
        System.out.println(UserController.getTheHotel().getTitle());
        this.rooms.setItems(roomService.findAllAvailableRoomsByHotelId(UserController.getTheHotel().getId()));

        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.hotel.setCellValueFactory(new PropertyValueFactory<>("hotel"));
        this.number.setCellValueFactory(new PropertyValueFactory<>("number"));
        this.countOfBeds.setCellValueFactory(new PropertyValueFactory<>("countOfBeds"));
        this.cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }

}
package miit.chuice.tour.controllers.user;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;
import lombok.Setter;
import miit.chuice.tour.models.Hotel;
import miit.chuice.tour.models.Human;
import miit.chuice.tour.services.HotelService;
import miit.chuice.tour.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static miit.chuice.tour.utils.SecurityUtils.makeAlert;

@Component
public class UserController implements Initializable {

    @FXML private TableColumn<?, ?> id;
    @FXML private TableColumn<?, ?> hotel;
    @FXML private TableColumn<?, ?> stars;
    @FXML private TableColumn<?, ?> country;
    @FXML private TableColumn<?, ?> city;
    @FXML private TableColumn<?, ?> address;
    @FXML private TableView<Hotel> hotels;
    @FXML private TextField findHotel;
    @FXML private Button find;
    @FXML private Button account;
    @FXML private Button reload;

    @Setter @Getter private static Human theUser;
    @Setter @Getter private static Hotel theHotel;

    private final Utils utils;
    private final HotelService hotelService;

    @Autowired
    public UserController(Utils utils, HotelService hotelService) {
        this.utils = utils;
        this.hotelService = hotelService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.reloadTable();

        account.setOnAction(event -> utils.changeScene(event, "/miit/chuice/tour/views/account.fxml", "Брони"));

        reload.setOnAction(event -> reloadTable());

        find.setOnAction(event -> {
            if (findHotel.getText().isEmpty()) {
                makeAlert("Вы не указали id отеля", Alert.AlertType.ERROR);
            } else {
                Hotel userHotel = hotelService.find(Long.parseLong(findHotel.getText()));
                if (userHotel != null) {
                    UserController.theHotel = userHotel;
                    utils.changeScene(event, "/miit/chuice/tour/views/rooms-in-hotel.fxml","Rooms");
                } else {
                    makeAlert("Отель не был найден в нашей базе", Alert.AlertType.ERROR);
                }
            }
        });

    }

    public void reloadTable() {

        hotels.setItems(hotelService.findAll());

        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.hotel.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.stars.setCellValueFactory(new PropertyValueFactory<>("stars"));
        this.country.setCellValueFactory(new PropertyValueFactory<>("country"));
        this.city.setCellValueFactory(new PropertyValueFactory<>("city"));
        this.address.setCellValueFactory(new PropertyValueFactory<>("address"));

    }

}
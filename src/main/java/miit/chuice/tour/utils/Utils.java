package miit.chuice.tour.utils;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import miit.chuice.tour.config.CustomFXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Utils {

    private final CustomFXMLLoader customFXMLLoader;

    @Autowired
    public Utils(CustomFXMLLoader customFXMLLoader) {
        this.customFXMLLoader = customFXMLLoader;
    }

    public void changeScene(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent root = customFXMLLoader.load(fxmlPath);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
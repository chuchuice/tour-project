package miit.chuice.tour;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class HelloApplication extends Application {
    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Tour.class);
        context = builder.run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/miit/chuice/tour/views/login.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @SpringBootApplication
    static class Tour {
        public static void main(String[] args) {
            SpringApplication.run(miit.chuice.tour.Tour.class, args);
        }
    }

}
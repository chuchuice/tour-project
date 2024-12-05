package miit.chuice.tour;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import miit.chuice.tour.config.CustomFXMLLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class HelloApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Tour.class);
        context = builder.run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        CustomFXMLLoader customFXMLLoader = context.getBean(CustomFXMLLoader.class);
        Parent root = customFXMLLoader.load("/miit/chuice/tour/views/login.fxml");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    @Override
    public void stop() {
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

@SpringBootApplication
class Tour {

}

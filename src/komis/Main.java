package komis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("cos");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("application.fxml"));
        AnchorPane root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Komis Samochodowy");
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}

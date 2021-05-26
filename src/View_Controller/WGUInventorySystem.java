package View_Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class WGUInventorySystem extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }



    /** This is the main method. This is the first method that gets called when the Java app is run.
     JAVADOC Folder location: \WGUInventorySystem\Javadoc
     */
    /** @param args the command line arguments.*/
    public static void main(String[] args) {
        launch(args);
    }

}
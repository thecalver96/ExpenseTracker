package expense;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class ExpenseTrackerApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/OverviewScene.fxml")));
        stage.setTitle("Expense Tracker v1.0");
        stage.setScene(new Scene(root));
        stage.show();
    }
}

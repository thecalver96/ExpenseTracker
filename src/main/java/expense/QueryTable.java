package expense;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public interface QueryTable {

     void onAction(ActionEvent actionEvent) throws IOException;
     void modifyEntry(MouseEvent mouseEvent) throws IOException;
}

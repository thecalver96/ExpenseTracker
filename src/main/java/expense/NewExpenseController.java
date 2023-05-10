package expense;

import com.sun.javafx.fxml.FXMLLoaderHelper;
import expense.model.Expense;
import expense.model.ExpenseDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class NewExpenseController  {
    public Button fSubmitButton;
    public DatePicker fDate;
    public ChoiceBox<Expense.MainCategory> fCategory;
    public TextField fTitle;
    public TextField fAmount;
    public ChoiceBox<Expense.Type> fType;
    public Label fErrorLine;
    public AnchorPane fNewExpense;

    private ExpenseDao expenseDao;


    public void initialize() throws IOException {
        expenseDao = OverviewController.initDB();



        ObservableList<Expense.Type> l = FXCollections.observableArrayList(Expense.Type.values());
        fType.getItems().addAll(Expense.Type.values());
        fCategory.getItems().addAll(Expense.MainCategory.values());


    }


    public void submitExpense(ActionEvent actionEvent) throws IOException {
        Button button = (Button) actionEvent.getSource();

        if(button.getId().equals("fSubmitButton")){

            List<String> data = getFieldValues();

            if(isFormCompleted(data)){
                Expense e = Expense.builder()
                        .title(fTitle.getText())
                        .type(fType.getValue())
                        .category(fCategory.getValue())
                        .cost(Double.parseDouble(fAmount.getText()))
                        .date(LocalDate.parse(String.valueOf(fDate.getValue())))
                        .build();
                expenseDao.persist(e);

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/OverviewScene.fxml"));
                stage.setScene(new Scene(root));
                stage.show();

            }
            else {
                fErrorLine.setVisible(true);

            }


        }
    }

    private boolean isFormCompleted(List<String> data){
        for (String datum : data) {
            if (datum == null || datum.isEmpty() || datum.equals("null")) {

                return false;
            }

        }
        return true;
    }

    private List<String> getFieldValues(){
        List<String> data = new ArrayList<>();
        data.add(fTitle.getText());
        data.add(String.valueOf(fType.getValue()));
        data.add(String.valueOf(fCategory.getValue()));
        data.add(fAmount.getText());
        data.add(String.valueOf(fDate.getValue()));

        return data;
    }


}
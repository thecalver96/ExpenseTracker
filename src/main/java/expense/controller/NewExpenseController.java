package expense.controller;

import expense.model.Expense;
import expense.model.ExpenseDaoImpl;
import expense.model.SelectedDataModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class NewExpenseController {
    @FXML
    public Button fSubmitButton;
    @FXML
    public DatePicker fDate;
    @FXML
    public ChoiceBox<Expense.MainCategory> fCategory;
    @FXML
    public TextField fTitle;
    @FXML
    public TextField fAmount;
    @FXML
    public ChoiceBox<Expense.Type> fType;
    @FXML
    public Label fErrorLine;
    @FXML
    public AnchorPane fNewExpense;

    private ExpenseDaoImpl expenseDaoImpl;

    private Expense selected;

    public void initialize() {

        expenseDaoImpl = DatabaseConnection.initDB();

        fType.getItems().addAll(Expense.Type.values());
        fCategory.getItems().addAll(Expense.MainCategory.values());

        selected = SelectedDataModel.getSelectedExpense();
        SelectedDataModel.setSelectedExpense(null);
        if(selected != null)
           setFieldValues(selected);


    }


    public void submitExpense(ActionEvent actionEvent) throws IOException {
        Button button = (Button) actionEvent.getSource();

        if (button.getId().equals("fSubmitButton")) {

            List<String> data = getFieldValues();

            if (isFormCompleted(data)) {
                updateOrCreateExpense(selected);

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/OverviewScene.fxml")));
                stage.setScene(new Scene(root));
                stage.show();

            } else {
                fErrorLine.setVisible(true);

            }


        }
    }

    private boolean isFormCompleted(List<String> data) {
        for (String datum : data) {
            if (datum == null || datum.isEmpty() || datum.equals("null")) {

                return false;
            }

        }
        return true;
    }

    private List<String> getFieldValues() {
        List<String> data = new ArrayList<>();
        data.add(fTitle.getText());
        data.add(String.valueOf(fType.getValue()));
        data.add(String.valueOf(fCategory.getValue()));
        data.add(fAmount.getText());
        data.add(String.valueOf(fDate.getValue()));

        return data;
    }

    private void setFieldValues(Expense e) {

        fTitle.setText(String.valueOf(e.getTitle()));
        fType.setValue(e.getType());
        fCategory.setValue(e.getCategory());
        fAmount.setText(String.valueOf(e.getCost()));
        fDate.setValue(e.getDate());

    }

    private void updateOrCreateExpense(Expense e){

        if(e == null) {
            e = Expense.builder()
                    .title(fTitle.getText())
                    .type(fType.getValue())
                    .category(fCategory.getValue())
                    .cost(Double.parseDouble(fAmount.getText()))
                    .date(LocalDate.parse(String.valueOf(fDate.getValue())))
                    .build();

        }
        else {
            e.setCategory(fCategory.getValue());
            e.setTitle(fTitle.getText());
            e.setCost(Double.parseDouble(fAmount.getText()));
            e.setDate(LocalDate.parse(String.valueOf(fDate.getValue())));
            e.setType(fType.getValue());


        }
        expenseDaoImpl.persist(e);
    }

}
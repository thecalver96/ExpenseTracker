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
    @FXML
    public Button fDeleteButton;
    @FXML
    public Button fCancelButton;

    private ExpenseDaoImpl expenseDaoImpl;

    private Expense selected;

    public void initialize() {

        expenseDaoImpl = DatabaseConnection.initDB();
        //    expenseDaoImpl = ExpenseDaoImpl.initDB();
        fType.getItems().addAll(Expense.Type.values());
        fCategory.getItems().addAll(Expense.MainCategory.values());

        selected = SelectedDataModel.getSelectedExpense();
        SelectedDataModel.setSelectedExpense(null);
        if (selected != null) {
            setFieldValues(selected);
            fDeleteButton.setVisible(true);
        }

    }


    public void submitExpense(ActionEvent actionEvent) throws IOException {
        Button button = (Button) actionEvent.getSource();
        Stage stage = (Stage) button.getScene().getWindow();

        if (button.getId().equals("fSubmitButton")) {

            if (fieldValidation()) {
                if ((selected == null)) {
                    createExpense(null);
                } else {
                    updateExpense(selected);
                }
                stage.close();

            } else
                fErrorLine.setVisible(true);

        }
        if (button.getId().equals("fCancelButton")) {
            stage.close();
        }
        if (button.getId().equals("fDeleteButton")) {

            removeExpense();
            stage.close();
        }
    }

    private boolean fieldValidation() {

            return  isValidTitle() && isValidCost() && isFormCompleted();
    }

    private boolean isValidTitle(){
        return fTitle.getText().length() > 0 && fTitle.getText().length() < 40;
    }

    private boolean isValidCost(){
        try {
            Double.parseDouble(fAmount.getText());
            return true;
        }catch (NumberFormatException exception){
            return false;
        }
    }

    private void removeExpense() {
        expenseDaoImpl.remove(selected);
    }

    private boolean isFormCompleted() {

        List<String> fieldValues = getFieldValues();
        for (String value : fieldValues) {
            if (value == null || value.isEmpty() || value.equals("null"))
                return false;
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

    private void updateExpense(Expense expense) {

        expense.setTitle(fTitle.getText());
        expense.setCategory(fCategory.getValue());
        expense.setType(fType.getValue());
        expense.setCost(Double.parseDouble(fAmount.getText()));
        expense.setDate(LocalDate.parse(String.valueOf(fDate.getValue())));

        expenseDaoImpl.persist(expense);
    }

    private void createExpense(Expense expense) {
        expense = Expense.builder()
                .title(fTitle.getText())
                .type(fType.getValue())
                .category(fCategory.getValue())
                .cost(Double.parseDouble(fAmount.getText()))
                .date(LocalDate.parse(String.valueOf(fDate.getValue())))
                .build();

        expenseDaoImpl.persist(expense);
    }

}
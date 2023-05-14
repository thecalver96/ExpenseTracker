package expense;

import expense.model.Expense;
import expense.model.ExpenseDao;
import expense.model.ExpenseDaoInt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import javax.annotation.Nullable;
import java.io.IOException;
import java.time.LocalDate;

import java.util.*;

public class OverviewController implements QueryTable{


    @FXML
    public TableView<Expense> FXTable;
    @FXML
    public TableColumn<Expense, String> fTitle;
    @FXML
    public TableColumn<Expense, Integer> fId;
    @FXML
    public TableColumn<Expense, Expense.Type> fType;
    @FXML
    public TableColumn<Expense, LocalDate> fDate;
    @FXML
    public TableColumn<Expense, Double> fCost;
    @FXML
    public TableColumn<Expense, Expense.MainCategory> fCategory;
    public TextField fBalance;
    public TextField fSpent;
    public PieChart fPieChart;

    public DatePicker fStartDate;
    public DatePicker fEndDate;
    public Button fSearchButton;
    public TextField fSearchBar;
    public Button fClearButton;
    public Button fAddNewExpenseButton;
    public CheckComboBox<Expense.MainCategory> fCombobox;

    private ExpenseDao expenseDao;

    public void initialize() {

        expenseDao = (ExpenseDao) initDB();


        ObservableList<Expense> allTransactions = getAllTransactions();
        setFXTable(allTransactions);

        ObservableList<Expense.MainCategory> categories = FXCollections.observableArrayList(
                Expense.MainCategory.values()
        );

        fCombobox.getItems().addAll(categories);
        fCombobox.getCheckModel().checkAll();


        fBalance.setText(String.valueOf(expenseDao.getBalance()));
        fSpent.setText(String.valueOf(expenseDao.getSumOfExpenses()));

        fStartDate.setValue(LocalDate.now().minusDays(7));
        fEndDate.setValue(LocalDate.now());

        setPieChart();

    }

    public static ExpenseDaoInt initDB() {
        return DatabaseConnection.getInstance()
                .getInjector()
                .getInstance(ExpenseDao.class);

    }

    private ObservableList<Expense> getAllTransactions() {
        return FXCollections.observableArrayList(
                expenseDao.findAll()
        );
    }

    private void setFXTable(ObservableList<Expense> list) {
        setTableFields(fId, fCost, fTitle, fDate, fType, fCategory);
        FXTable.setItems(list);
    }

    private void setTableFields(TableColumn<Expense, Integer> fId, TableColumn<Expense, Double> fCost, TableColumn<Expense, String> fTitle,
                                TableColumn<Expense, LocalDate> fDate, TableColumn<Expense, Expense.Type> fType,
                                TableColumn<Expense, Expense.MainCategory> fCategory) {

        fId.setCellValueFactory(new PropertyValueFactory<Expense, Integer>("id"));
        fCost.setCellValueFactory(new PropertyValueFactory<Expense, Double>("cost"));
        fTitle.setCellValueFactory(new PropertyValueFactory<Expense, String>("title"));
        fDate.setCellValueFactory(new PropertyValueFactory<Expense, LocalDate>("date"));
        fType.setCellValueFactory(new PropertyValueFactory<Expense, Expense.Type>("type"));
        fCategory.setCellValueFactory(new PropertyValueFactory<Expense, Expense.MainCategory>("category"));
    }


    public void onAction(ActionEvent actionEvent) throws IOException {
        Button button = (Button) actionEvent.getSource();

        if (button.getId().equals("fSearchButton")) {

            ObservableList<Expense> list1 = FXCollections.observableArrayList(
                        expenseDao.getSearch(fSearchBar.getText(),fStartDate.getValue(),fEndDate.getValue(),
                                fCombobox.getCheckModel().getCheckedItems())
                    );
            setFXTable(list1);


        }
        if(button.getId().equals("fClearButton")){
            ObservableList<Expense> list =FXCollections.observableArrayList(expenseDao.findAll());
            fCombobox.getCheckModel().checkAll();
            fStartDate.setValue(LocalDate.now().minusDays(7));
            fEndDate.setValue(LocalDate.now());
            fSearchBar.setText("");
            setFXTable(list);

        }

        if(button.getId().equals("fAddNewExpenseButton")){
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            changeScene(stage,null);
        }
    }

    private void changeScene(Stage stage, @Nullable Expense e) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/NewExpense.fxml"));
        stage.setScene(new Scene(root));
        stage.setUserData(e);
        stage.show();
    }


    private void setPieChart() {

        ObservableList<PieChart.Data> a = FXCollections.observableArrayList();

        Arrays.stream(Expense.MainCategory.values()).forEach(
                category -> {

                    if(!expenseDao.getExpenseByCategory(category).equals(0.0))
                        a.add(new PieChart.Data(String.valueOf(category), expenseDao.getExpenseByCategory(category)));
                });



        fPieChart.setData(a);

    }

    public void modifyEntry(MouseEvent mouseEvent) throws IOException {

        if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {


            if(FXTable.getSelectionModel().getSelectedItem() != null ) {
                Expense selected = FXTable.getSelectionModel().getSelectedItem();
                Stage stage =(Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                changeScene(stage, selected);
            }
        }
    }
}

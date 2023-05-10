package expense;

import com.google.inject.Guice;
import com.google.inject.Injector;
import expense.model.Expense;
import expense.model.ExpenseDao;
import guice.PersistenceModule;
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
import lombok.NonNull;
import net.bytebuddy.asm.Advice;

import javax.annotation.Nullable;
import java.io.IOException;
import java.time.LocalDate;

import java.util.List;
import java.util.stream.DoubleStream;


public class OverviewController {


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
    public TableView<Expense> FXQueryTable;

    @FXML
    public TableColumn<Expense, String> fTitle1;
    @FXML
    public TableColumn<Expense, Integer> fId1;
    @FXML
    public TableColumn<Expense, Expense.Type> fType1;
    @FXML
    public TableColumn<Expense, LocalDate> fDate1;
    @FXML
    public TableColumn<Expense, Double> fCost1;
    @FXML
    public TableColumn<Expense, Expense.MainCategory> fCategory1;

    public DatePicker fStartDate;
    public DatePicker fEndDate;
    public Button fSearchButton;
    public TextField fSearchBar;

    private ExpenseDao expenseDao;

    public void initialize() {

        expenseDao = initDB();


        ObservableList<Expense> list = getAllExpenses();
        setFXTable(list);
        // setFXQueryTable(list);

        fBalance.setText(String.valueOf(expenseDao.getBalance()));
        fSpent.setText(String.valueOf(expenseDao.getSumOfExpenses()));

        fStartDate.setValue(LocalDate.now().minusDays(7));
        fEndDate.setValue(LocalDate.now());

        setPieChart();

    }

    public static ExpenseDao initDB() {
        return DatabaseConnection.getInstance()
                .getInjector()
                .getInstance(ExpenseDao.class);

    }

    private ObservableList<Expense> getAllExpenses() {
        return FXCollections.observableArrayList(
                expenseDao.findAll()
        );
    }

    private void setFXTable(ObservableList<Expense> list) {
        setTableFields(fId, fCost, fTitle, fDate, fType, fCategory);
        FXTable.setItems(list);
    }

    private void setFXQueryTable(ObservableList<Expense> list) {
        setTableFields(fId1, fCost1, fTitle1, fDate1, fType1, fCategory1);
        FXQueryTable.setItems(list);
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


    public void runSearch(ActionEvent actionEvent) throws IOException {
        Button button = (Button) actionEvent.getSource();

        if (button.getId().equals("fSearchButton")) {
            ObservableList<Expense> list = FXCollections.observableArrayList(
                    expenseDao.findBetweenDates(fStartDate.getValue(), fEndDate.getValue()));
            setFXQueryTable(list);
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

        ObservableList<PieChart.Data> d = FXCollections.observableArrayList(
                new PieChart.Data("HOME", expenseDao.findAll()
                        .stream()
                        .filter(expense -> expense.getCategory()
                                .equals(Expense.MainCategory.HOME))
                        .mapToDouble(Expense::getCost)
                        .sum()
                        / expenseDao.findAll()
                        .stream()
                        .flatMapToDouble(expense -> DoubleStream.of(expense.getCost()))
                        .sum()

                ),
                new PieChart.Data("FOOD", expenseDao.findAll()
                        .stream()
                        .filter(expense -> expense.getCategory()
                                .equals(Expense.MainCategory.FOOD))
                        .mapToDouble(Expense::getCost)
                        .sum()
                        / expenseDao.findAll()
                        .stream()
                        .flatMapToDouble(expense -> DoubleStream.of(expense.getCost()))
                        .sum()
                )
        );
        fPieChart.setData(d);

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

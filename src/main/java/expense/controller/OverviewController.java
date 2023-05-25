package expense.controller;

import expense.model.Expense;
import expense.model.ExpenseDaoImpl;
import expense.model.SelectedDataModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.NonNull;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.time.LocalDate;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class OverviewController implements QueryTable {


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
    @FXML
    public TextField fBalance;
    @FXML
    public TextField fSpent;
    @FXML
    public PieChart fPieChart;

    @FXML
    public DatePicker fStartDate;
    @FXML
    public DatePicker fEndDate;
    @FXML
    public Button fSearchButton;
    @FXML
    public TextField fSearchBar;
    @FXML
    public Button fClearButton;
    @FXML
    public Button fAddNewExpenseButton;
    @FXML
    public CheckComboBox<Expense.MainCategory> fCombobox;

    private ExpenseDaoImpl expenseDaoImpl;

    public void initialize() {
        expenseDaoImpl = DatabaseConnection.initDB();

        ObservableList<Expense> allTransactions = getAllTransactions();
        setFXTable(allTransactions);

        ObservableList<Expense.MainCategory> categories = FXCollections.observableArrayList(
                Expense.MainCategory.values()
        );

        fCombobox.getItems().addAll(categories);
        fCombobox.getCheckModel().checkAll();
        fBalance.setText(String.valueOf(expenseDaoImpl.getBalance()));
        fSpent.setText(String.valueOf(expenseDaoImpl.getSumOfExpenses()));
        fStartDate.setValue(LocalDate.now().minusDays(7));
        fEndDate.setValue(LocalDate.now());

        setPieChart();

    }


    private ObservableList<Expense> getAllTransactions() {
        return FXCollections.observableArrayList(
                expenseDaoImpl.findAll()
        );
    }

    void setFXTable(ObservableList<Expense> tableContent) {
        setTableFields(fId, fCost, fTitle, fDate, fType, fCategory);
        FXTable.setItems(tableContent);
    }

    private void setTableFields(@NonNull TableColumn<Expense, Integer> fId, @NonNull TableColumn<Expense, Double> fCost, @NonNull TableColumn<Expense, String> fTitle,
                                @NonNull TableColumn<Expense, LocalDate> fDate, @NonNull TableColumn<Expense, Expense.Type> fType,
                                @NonNull TableColumn<Expense, Expense.MainCategory> fCategory) {

        fId.setCellValueFactory(new PropertyValueFactory<>("id"));
        fCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        fTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        fDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        fType.setCellValueFactory(new PropertyValueFactory<>("type"));
        fCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
    }


    public void onAction(@NonNull ActionEvent actionEvent) throws IOException {
        Button button = (Button) actionEvent.getSource();

        if (button.getId().equals("fSearchButton")) {

            ObservableList<Expense> queryList = FXCollections.observableArrayList(
                    expenseDaoImpl.getSearch(fSearchBar.getText(), fStartDate.getValue(), fEndDate.getValue(),
                            fCombobox.getCheckModel().getCheckedItems())
            );
            setFXTable(queryList);


        }
        if (button.getId().equals("fClearButton")) {
            ObservableList<Expense> list = FXCollections.observableArrayList(expenseDaoImpl.findAll());
            fCombobox.getCheckModel().checkAll();
            fStartDate.setValue(LocalDate.now().minusDays(7));
            fEndDate.setValue(LocalDate.now());
            fSearchBar.setText("");
            setFXTable(list);

        }

        if (button.getId().equals("fAddNewExpenseButton"))
            newExpenseModal();
    }


    void newExpenseModal() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewExpense.fxml"));
        Parent root = loader.load();
        Stage newExpenseStage = new Stage();
        newExpenseStage.initModality(Modality.APPLICATION_MODAL); // Set the modality to APPLICATION_MODAL
        newExpenseStage.setTitle("New Expense");
        newExpenseStage.setScene(new Scene(root));

        newExpenseStage.showAndWait();
        refreshData();

    }

    void refreshData() {


        ObservableList<Expense> allTransactions = getAllTransactions();
        setFXTable(allTransactions);
        FXTable.refresh();

        fBalance.setText(String.valueOf(expenseDaoImpl.getBalance()));
        fSpent.setText(String.valueOf(expenseDaoImpl.getSumOfExpenses()));
        setPieChart();


    }

    void setPieChart() {

        ObservableList<PieChart.Data> piechartData = FXCollections.observableArrayList();

        Arrays.stream(Expense.MainCategory.values()).forEach(
                category -> {

                    if (!expenseDaoImpl.getExpenseByCategory(category).equals(0.0))
                        piechartData.add(new PieChart.Data(String.valueOf(category), expenseDaoImpl.getExpenseByCategory(category)));
                });


        fPieChart.setData(piechartData);


    }

    public void modifyEntry(@NonNull MouseEvent mouseEvent) throws IOException {

        if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
            if (FXTable.getSelectionModel().getSelectedItem() != null) {
                Expense selected = FXTable.getSelectionModel().getSelectedItem();
                SelectedDataModel.setSelectedExpense(selected);
                newExpenseModal();

            }
        }
    }
}

package expense;

import com.google.inject.Guice;
import com.google.inject.Injector;
import expense.model.Expense;
import expense.model.ExpenseDao;
import guice.PersistenceModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.swing.text.html.parser.Entity;
import java.time.LocalDate;
import java.util.List;

public class TableController {


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

    public void initialize() {
        DatabaseConnection injector = DatabaseConnection.getInstance();
        ExpenseDao expenseDao = injector.getInjector().getInstance(ExpenseDao.class);

        List<Expense> list =  expenseDao.findAll();
        ObservableList<Expense> list2 = FXCollections.observableArrayList(
                list
        );

        fId.setCellValueFactory(new PropertyValueFactory<Expense, Integer>("id"));
        fCost.setCellValueFactory(new PropertyValueFactory<Expense, Double>("cost"));
        fTitle.setCellValueFactory(new PropertyValueFactory<Expense, String>("title"));
        fDate.setCellValueFactory(new PropertyValueFactory<Expense, LocalDate>("date"));
        fType.setCellValueFactory(new PropertyValueFactory<Expense, Expense.Type>("type"));





        FXTable.setItems(list2);
    }
}

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
    public TableColumn<Expense, String> fTitle;
    public TableColumn<Expense, Integer> fId;
    public TableColumn<Expense, Expense.Type> fType;
    public TableColumn<Expense, LocalDate> fDate;
    public TableColumn<Expense, Double> fCost;

    public void initialize() {
        Injector injector = Guice.createInjector(new PersistenceModule("ExpenseTracker"));
        ExpenseDao expenseDao = injector.getInstance(ExpenseDao.class);
        Expense e1 = Expense.builder()
                .title("elso")
                .cost(123.0)
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .build();
        Expense e2 = Expense.builder()
                .title("masodik")
                .cost(321.0)
                .date(LocalDate.of(2012, 8, 1))
                .type(Expense.Type.INCOME)
                .build();


        expenseDao.persist(e1);
        expenseDao.persist(e2);

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

package expense;

import com.google.inject.Guice;
import com.google.inject.Injector;
import expense.model.Expense;
import expense.model.ExpenseDao;
import guice.PersistenceModule;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.scene.control.Tab;

import java.time.LocalDate;


public class Main {
    public static void main(String[] args) {

        DatabaseConnection injector = DatabaseConnection.getInstance();
        ExpenseDao expenseDao = injector.getInjector().getInstance(ExpenseDao.class);


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




        Application.launch(ExpenseTrackerApplication.class, args);
    }
}
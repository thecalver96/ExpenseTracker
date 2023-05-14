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
import lombok.experimental.SuperBuilder;

import java.io.UncheckedIOException;
import java.time.LocalDate;


public class Main {
    public static void main(String[] args) {

        /* DB connection singleton */
        DatabaseConnection injector = DatabaseConnection.getInstance();
        ExpenseDao expenseDao = injector.getInjector().getInstance(ExpenseDao.class);

        /* Some mock data to check on */
        Expense e1 = Expense.builder()
                .title("elso")
                .cost(500.0)
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .category(Expense.MainCategory.HOME)
                .build();
        Expense e2 = Expense.builder()
                .title("masodik")
                .cost(500.0)
                .date(LocalDate.of(2012, 8, 1))
                .type(Expense.Type.EXPENSE)
                .category(Expense.MainCategory.FOOD)
                .build();

        for(int i = 0; i < 30; i++){
            Expense expense = Expense.builder()
                    .title("title" + i)
                    .cost(500.0 + i*10)
                    .date(LocalDate.of(2000+i, 8, 1))
                    .type(i%2==0?Expense.Type.EXPENSE: Expense.Type.INCOME)
                    .category(Expense.MainCategory.values()[i%8])
                    .build();

            expenseDao.persist(expense);
        }


        expenseDao.persist(e1);
        expenseDao.persist(e2);

        /* ********************* */

        /* Run */
        Application.launch(ExpenseTrackerApplication.class, args);
    }
}
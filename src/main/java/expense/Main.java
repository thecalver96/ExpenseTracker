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





        Application.launch(ExpenseTrackerApplication.class, args);
    }
}
package expense.controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import expense.model.Expense;
import expense.model.ExpenseDaoImpl;
import guice.PersistenceModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import org.mockito.MockitoAnnotations;
import org.testng.Assert;

import java.time.LocalDate;
import java.util.List;



public class OverviewControllerTest {


    @InjectMocks
    ExpenseDaoImpl expenseDao;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Injector injector = Guice.createInjector(new PersistenceModule("Test"));
        expenseDao = injector.getInstance(ExpenseDaoImpl.class);

    }

    @Test
    public void TestInitialize() {

        Expense expense1 = Expense.builder()

                .title("first")
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .cost(123.1123)
                .category(Expense.MainCategory.DRESSING)
                .build();
        Expense expense2 = Expense.builder()

                .title("second")
                .date(LocalDate.now().minusDays(12))
                .type(Expense.Type.INCOME)
                .cost(1999.1123)
                .category(Expense.MainCategory.ENTERTAINMENT)
                .build();
        List<Expense> expectedExpenses = List.of(expense1, expense2);
        expenseDao.persist(expense1);
        expenseDao.persist(expense2);


        ObservableList<Expense> allTransactions = FXCollections.observableArrayList(expenseDao.findAll());

        Assert.assertEquals(expectedExpenses, allTransactions);
    }

}
package expense.model;


import com.google.inject.Guice;
import com.google.inject.Injector;
import expense.controller.DatabaseConnection;
import guice.PersistenceModule;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.List;

import static org.mockito.Mockito.*;

public class ExpenseDaoImplTest {

    @InjectMocks
    ExpenseDaoImpl expenseDao;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new PersistenceModule("Test"));
        expenseDao = injector.getInstance(ExpenseDaoImpl.class);
    }


    @Test
    public void testPersistExpense() {
        // Arrange
        Expense expense = Expense.builder()
                .title("test")
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .cost(50.0)
                .category(Expense.MainCategory.OTHER)
                .build();

        // Act
        expenseDao.persist(expense);

        // Assert
        Expense retrievedExpense = expenseDao.find(expense.getId()).orElse(null);
        Assertions.assertEquals(expense, retrievedExpense);
    }


    @Test
    public void testUpdateExpense() {
        // Arrange
        Expense expense = Expense.builder()
                .title("test")
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .cost(50.0)
                .category(Expense.MainCategory.OTHER)
                .build();
        expenseDao.persist(expense);

        // Act
        expense.setTitle("updated test");
        expense.setCost(100.0);
        expenseDao.update(expense);

        // Assert
        Expense updatedExpense = expenseDao.find(expense.getId()).orElse(null);
        assert updatedExpense != null;
        Assertions.assertEquals("updated test", updatedExpense.getTitle());
        Assertions.assertEquals(100.0, updatedExpense.getCost());
    }

    @Test
    public void testDeleteExpense() {
        // Arrange
        Expense expense = Expense.builder()
                .title("test")
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .cost(50.0)
                .category(Expense.MainCategory.OTHER)
                .build();
        expenseDao.persist(expense);

        // Act
        expenseDao.remove(expense);

        // Assert
        Expense deletedExpense = expenseDao.find(expense.getId()).orElse(null);
        Assertions.assertNull(deletedExpense);
    }

    @Test
    public void testFindAllExpenses() {


        // Arrange
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
        // Act
        List<Expense> actualExpenses = expenseDao.findAll();

        // Assert
        Assertions.assertEquals(expectedExpenses.size(), actualExpenses.size());
        Assertions.assertEquals(expectedExpenses.get(0), actualExpenses.get(0));
        Assertions.assertEquals(expectedExpenses.get(1), actualExpenses.get(1));


    }

    @Test
    public void testFindExpensesBetweenDates() {
        // Arrange
        Expense expense1 = Expense.builder()
                .title("expense1")
                .date(LocalDate.now().minusDays(7))
                .type(Expense.Type.EXPENSE)
                .cost(100.0)
                .category(Expense.MainCategory.DRESSING)
                .build();
        Expense expense2 = Expense.builder()
                .title("expense2")
                .date(LocalDate.now().minusDays(2))
                .type(Expense.Type.INCOME)
                .cost(200.0)
                .category(Expense.MainCategory.ENTERTAINMENT)
                .build();
        expenseDao.persist(expense1);
        expenseDao.persist(expense2);

        // Act
        List<Expense> expenses = expenseDao.findBetweenDates(
                LocalDate.now().minusDays(5),
                LocalDate.now().minusDays(1)
        );

        // Assert
        Assertions.assertEquals(1, expenses.size());
        Assertions.assertEquals(expense2, expenses.get(0));
    }

    @Test
    public void testGetBalance() {
        // Arrange
        Expense expense1 = Expense.builder()
                .title("expense1")
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .cost(100.0)
                .category(Expense.MainCategory.DRESSING)
                .build();
        Expense expense2 = Expense.builder()
                .title("expense2")
                .date(LocalDate.now())
                .type(Expense.Type.INCOME)
                .cost(200.0)
                .category(Expense.MainCategory.ENTERTAINMENT)
                .build();
        expenseDao.persist(expense1);
        expenseDao.persist(expense2);

        // Act
        double balance = expenseDao.getBalance();

        // Assert
        Assertions.assertEquals(100.0, balance);
    }

    @Test
    public void testGetSumOfExpenses() {
        // Arrange
        Expense expense1 = Expense.builder()
                .title("expense1")
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .cost(100.0)
                .category(Expense.MainCategory.DRESSING)
                .build();
        Expense expense2 = Expense.builder()
                .title("expense2")
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .cost(200.0)
                .category(Expense.MainCategory.ENTERTAINMENT)
                .build();
        expenseDao.persist(expense1);
        expenseDao.persist(expense2);

        // Act
        double sum = expenseDao.getSumOfExpenses();

        // Assert
        Assertions.assertEquals(300.0, sum);
    }

    @Test
    public void testGetExpensesByCategory() {
        // Arrange
        Expense expense1 = Expense.builder()
                .title("expense1")
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .cost(100.0)
                .category(Expense.MainCategory.DRESSING)
                .build();
        Expense expense2 = Expense.builder()
                .title("expense2")
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .cost(200.0)
                .category(Expense.MainCategory.ENTERTAINMENT)
                .build();
        expenseDao.persist(expense1);
        expenseDao.persist(expense2);

        // Act
        Double expenseSumInCategory = expenseDao.getExpenseByCategory(Expense.MainCategory.DRESSING);

        // Assert
        Assertions.assertEquals(expense1.getCost(), expenseSumInCategory);

    }

    @Test
    public void testGetExpensesBySearch() {
        // Arrange
        Expense expense1 = Expense.builder()
                .title("expense1")
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .cost(100.0)
                .category(Expense.MainCategory.DRESSING)
                .build();
        Expense expense2 = Expense.builder()
                .title("expense2")
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .cost(200.0)
                .category(Expense.MainCategory.ENTERTAINMENT)
                .build();
        expenseDao.persist(expense1);
        expenseDao.persist(expense2);

        // Act
        List<Expense> expenses = expenseDao.getSearch("expense", LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1), Arrays.stream(Expense.MainCategory.values()).toList());

        // Assert
        Assertions.assertEquals(2, expenses.size());
        Assertions.assertEquals(expense1, expenses.get(0));
        Assertions.assertEquals(expense2, expenses.get(1));
    }
}

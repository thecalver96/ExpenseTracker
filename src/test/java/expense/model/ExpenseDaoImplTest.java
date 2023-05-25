package expense.model;


import com.google.inject.Guice;
import com.google.inject.Injector;
import guice.PersistenceModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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

        Expense expense = Expense.builder()
                .title("test")
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .cost(50.0)
                .category(Expense.MainCategory.OTHER)
                .build();

        expenseDao.persist(expense);

        Expense retrievedExpense = expenseDao.find(expense.getId()).orElse(null);
        Assertions.assertEquals(expense, retrievedExpense);
    }


    @Test
    public void testUpdateExpense() {
        Expense expense = Expense.builder()
                .title("test")
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .cost(50.0)
                .category(Expense.MainCategory.OTHER)
                .build();
        expenseDao.persist(expense);

        expense.setTitle("updated test");
        expense.setCost(100.0);
        expenseDao.update(expense);

        Expense updatedExpense = expenseDao.find(expense.getId()).orElse(null);
        assert updatedExpense != null;
        Assertions.assertEquals("updated test", updatedExpense.getTitle());
        Assertions.assertEquals(100.0, updatedExpense.getCost());
    }

    @Test
    public void testDeleteExpense() {
        Expense expense = Expense.builder()
                .title("test")
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .cost(50.0)
                .category(Expense.MainCategory.OTHER)
                .build();
        expenseDao.persist(expense);

        expenseDao.remove(expense);

        Expense deletedExpense = expenseDao.find(expense.getId()).orElse(null);
        Assertions.assertNull(deletedExpense);
    }

    @Test
    public void testFindAllExpenses() {


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

        List<Expense> actualExpenses = expenseDao.findAll();

        Assertions.assertEquals(expectedExpenses.size(), actualExpenses.size());
        Assertions.assertEquals(expectedExpenses.get(0), actualExpenses.get(0));
        Assertions.assertEquals(expectedExpenses.get(1), actualExpenses.get(1));


    }

    @Test
    public void testFindExpensesBetweenDates() {
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

        List<Expense> expenses = expenseDao.findBetweenDates(
                LocalDate.now().minusDays(5),
                LocalDate.now().minusDays(1)
        );

        Assertions.assertEquals(1, expenses.size());
        Assertions.assertEquals(expense2, expenses.get(0));
    }

    @Test
    public void testGetBalance() {

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


        double balance = expenseDao.getBalance();

        Assertions.assertEquals(100.0, balance);
    }

    @Test
    public void testGetSumOfExpenses() {
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


        double sum = expenseDao.getSumOfExpenses();

        Assertions.assertEquals(300.0, sum);
    }

    @Test
    public void testGetExpensesByCategory() {
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

        Double expenseSumInCategory = expenseDao.getExpenseByCategory(Expense.MainCategory.DRESSING);

        Assertions.assertEquals(expense1.getCost(), expenseSumInCategory);

    }

    @Test
    public void testGetExpensesBySearch() {
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

        List<Expense> expenses = expenseDao.getSearch("expense", LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1), Arrays.stream(Expense.MainCategory.values()).toList());

        Assertions.assertEquals(2, expenses.size());
        Assertions.assertEquals(expense1, expenses.get(0));
        Assertions.assertEquals(expense2, expenses.get(1));
    }
}

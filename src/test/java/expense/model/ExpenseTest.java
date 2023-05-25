package expense.model;

import org.junit.Test;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
public class ExpenseTest {
    @Test
    public void testExpenseInitialization() {
        Integer id = 1;
        String title = "Expense 1";
        double cost = 10.0;
        LocalDate date = LocalDate.now();
        Expense.Type type = Expense.Type.EXPENSE;
        Expense.MainCategory category = Expense.MainCategory.FOOD;

        Expense expense = new Expense(id, title, cost, date, type, category);

        Assertions.assertEquals(id, expense.getId());
        Assertions.assertEquals(title, expense.getTitle());
        Assertions.assertEquals(cost, expense.getCost(), 0.01); //kerekítés miatt
        Assertions.assertEquals(date, expense.getDate());
        Assertions.assertEquals(type, expense.getType());
        Assertions.assertEquals(category, expense.getCategory());

    }

    @Test
    public void testExpenseModification(){
        Integer id = 1;
        String title = "Expense 1";
        Double cost = 10.0;
        LocalDate date = LocalDate.now();
        Expense.Type type = Expense.Type.EXPENSE;
        Expense.MainCategory category = Expense.MainCategory.FOOD;

        Expense expense = new Expense(id, title, cost, date, type, category);

        expense.setId(100);
        expense.setTitle("Expense 1 Modified");
        expense.setCost(999.9);
        expense.setCategory(Expense.MainCategory.CAR);
        expense.setType(Expense.Type.INCOME);
        expense.setDate(LocalDate.of(2000,1,1));

        Assertions.assertEquals(100, expense.getId());
        Assertions.assertEquals("Expense 1 Modified", expense.getTitle());
        Assertions.assertEquals(999.9, expense.getCost(), 0.01); //kerekítés miatt
        Assertions.assertEquals(LocalDate.of(2000,1,1), expense.getDate());
        Assertions.assertEquals(Expense.Type.INCOME, expense.getType());
        Assertions.assertEquals(Expense.MainCategory.CAR, expense.getCategory());

    }
}

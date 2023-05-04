package expense;

import com.google.inject.Guice;
import com.google.inject.Injector;
import expense.model.Expense;
import expense.model.ExpenseDao;
import guice.PersistenceModule;
import java.time.LocalDate;


public class Main {
    public static void main(String[] args) {
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

        System.out.println(expenseDao.findAll());

        expenseDao.remove(e1);
        System.out.println(expenseDao.findAll());


    }
}
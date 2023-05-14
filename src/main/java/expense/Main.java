package expense;

import expense.controller.DatabaseConnection;
import expense.model.Expense;

import expense.model.ExpenseDaoImpl;
import javafx.application.Application;

import java.time.LocalDate;


public class Main {
    public static void main(String[] args) {

        /* DB connection singleton */

        DatabaseConnection injector = DatabaseConnection.getInstance();
       ExpenseDaoImpl expenseDaoImpl = injector.getInjector().getInstance(ExpenseDaoImpl.class);


        /* Some mock data to check on */

        for(int i = 0; i < 30; i++){
            Expense expense = Expense.builder()
                    .title("title" + i)
                    .cost(500.0 + i*10)
                    .date(LocalDate.of(2000+i, 8, 1))
                    .type(i%2==0?Expense.Type.EXPENSE: Expense.Type.INCOME)
                    .category(Expense.MainCategory.values()[i%8])
                    .build();

            expenseDaoImpl.persist(expense);
        }


        /* ********************* */

        /* Run */
        Application.launch(ExpenseTrackerApplication.class, args);
    }
}
package expense.model;

import com.google.inject.Guice;
import com.google.inject.Injector;
import guice.PersistenceModule;
import jpa.GenericJpaDao;
import lombok.NoArgsConstructor;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.DoubleStream;

public class ExpenseDaoImpl extends GenericJpaDao<Expense> implements ExpenseDao {


    public static ExpenseDaoImpl initDB(){

        Injector injector = Guice.createInjector(new PersistenceModule("ExpenseTracker"));
        return injector.getInstance(ExpenseDaoImpl.class);
    }

    @Override
    public List<Expense> findBetweenDates(LocalDate startDate, LocalDate endDate) {
        TypedQuery<Expense> typedQuery = (TypedQuery<Expense>) entityManager.createQuery(
                "SELECT e FROM " + entityClass.getSimpleName()
                        + " e WHERE e.date >=: startDate and e.date <=: endDate", entityClass);
        typedQuery.setParameter("startDate", startDate);
        typedQuery.setParameter("endDate", endDate);

        return typedQuery.getResultList();
    }

    @Override
    public Double getBalance() {

        return findAll()
                .stream()
                .filter(expense -> expense.getType().equals(Expense.Type.INCOME))
                .flatMapToDouble(expense -> DoubleStream.of(expense.getCost()))
                .sum()
                -
                findAll().stream().filter(expense -> expense.getType().equals(Expense.Type.EXPENSE))
                        .flatMapToDouble(expense -> DoubleStream.of(expense.getCost()))
                        .sum();
    }

    @Override
    public Double getSumOfExpenses() {
        return findBetweenDates(LocalDate.now().minusDays(30), LocalDate.now())
                .stream()
                .filter(expense -> expense.getType().equals(Expense.Type.EXPENSE))
                .mapToDouble(Expense::getCost)
                .sum();
    }

    @Override
    public Double getExpenseByCategory(Expense.MainCategory category) {
        return findAll().stream()
                .filter(expense -> expense.getType().equals(Expense.Type.EXPENSE)
                        &&
                        expense.getCategory().equals(category))
                .mapToDouble(Expense::getCost)
                .sum()
                ;

    }

    @Override
    public List<Expense> getSearch(String searchTerm, LocalDate startDate, LocalDate endDate, List<Expense.MainCategory> categories) {
        TypedQuery<Expense> typedQuery = (TypedQuery<Expense>) entityManager.createQuery(
                "SELECT e FROM " + entityClass.getSimpleName()
                        + " e WHERE e.title like '%" + searchTerm + "%' and ( e.date >=: startDate and e.date <=: endDate) and e.category IN :categories", entityClass);
        typedQuery.setParameter("startDate", startDate);
        typedQuery.setParameter("endDate", endDate);
        typedQuery.setParameter("categories", categories);


        return typedQuery.getResultList();
    }
}

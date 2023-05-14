package expense.model;

import com.google.inject.persist.Transactional;
import jpa.GenericJpaDao;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.DoubleStream;

public class ExpenseDao extends GenericJpaDao<Expense> implements ExpenseDaoInt{
    @Transactional
    public List<Expense> findBetweenDates(LocalDate startDate, LocalDate endDate){
        TypedQuery<Expense> typedQuery = (TypedQuery<Expense>) entityManager.createQuery(
                "SELECT e FROM " + entityClass.getSimpleName()
                        + " e WHERE e.date >=: startDate and e.date <=: endDate", entityClass);
        typedQuery.setParameter("startDate", startDate);
        typedQuery.setParameter("endDate", endDate);

        return typedQuery.getResultList();
    }

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

    public Double getSumOfExpenses(){
        return findBetweenDates(LocalDate.now().minusDays(30), LocalDate.now())
                .stream()
                .filter(expense -> expense.getType().equals(Expense.Type.EXPENSE))
                .mapToDouble(Expense::getCost)
                .sum();
    }


    public Double getExpenseByCategory(Expense.MainCategory category){
        return findAll().stream()
                .filter(expense -> expense.getType().equals(Expense.Type.EXPENSE)
                &&
                        expense.getCategory().equals(category))
                .mapToDouble(Expense::getCost)
                .sum()
                ;

    }
    @Transactional
    public List<Expense> getSearch(String searchTerm, LocalDate startDate, LocalDate endDate, List<Expense.MainCategory> categories){
        TypedQuery<Expense> typedQuery = (TypedQuery<Expense>) entityManager.createQuery(
                "SELECT e FROM " + entityClass.getSimpleName()
                        + " e WHERE e.title like '%" + searchTerm +"%' and ( e.date >=: startDate and e.date <=: endDate) and e.category IN :categories", entityClass);
        typedQuery.setParameter("startDate", startDate);
        typedQuery.setParameter("endDate", endDate);
        typedQuery.setParameter("categories", categories);


        return typedQuery.getResultList();
    }

}

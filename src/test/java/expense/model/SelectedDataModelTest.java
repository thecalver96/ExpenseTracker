package expense.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SelectedDataModelTest {

    @BeforeEach
    public void setUp() {
        SelectedDataModel.setSelectedExpense(null);
    }

    @Test
    public void testGetSelectedExpenseWhenNotSet() {
        Expense selectedExpense = SelectedDataModel.getSelectedExpense();

        Assertions.assertNull(selectedExpense);
    }

    @Test
    public void testSetSelectedExpenseAndGetSelectedExpense() {
        Expense expense = Expense.builder()
                .title("Test Expense")
                .build();

        SelectedDataModel.setSelectedExpense(expense);
        Expense selectedExpense = SelectedDataModel.getSelectedExpense();

        Assertions.assertEquals(expense, selectedExpense);
    }

    @Test
    public void testSetSelectedExpenseMultipleTimesAndGetSelectedExpense() {
        Expense expense1 = Expense.builder()
                .title("Expense 1")
                .build();
        Expense expense2 = Expense.builder()
                .title("Expense 2")
                .build();

        SelectedDataModel.setSelectedExpense(expense1);
        Expense selectedExpense1 = SelectedDataModel.getSelectedExpense();

        SelectedDataModel.setSelectedExpense(expense2);
        Expense selectedExpense2 = SelectedDataModel.getSelectedExpense();

        Assertions.assertEquals(expense1, selectedExpense1);
        Assertions.assertEquals(expense2, selectedExpense2);
    }
}
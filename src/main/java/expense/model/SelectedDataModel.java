package expense.model;

public class SelectedDataModel {


    private static Expense selectedExpense;

    public static Expense getSelectedExpense() {
        return selectedExpense;
    }

    public static void setSelectedExpense(Expense expense) {
        selectedExpense = expense;
    }


}

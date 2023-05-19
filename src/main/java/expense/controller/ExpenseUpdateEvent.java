package expense.controller;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class ExpenseUpdateEvent extends Event {
    public static final EventType<ExpenseUpdateEvent> UPDATE = new EventType<>(Event.ANY, "EXPENSE_UPDATE");

    public ExpenseUpdateEvent() {
        super(UPDATE);
    }
}

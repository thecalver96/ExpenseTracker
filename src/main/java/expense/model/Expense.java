package expense.model;
import javafx.collections.ObservableArray;
import lombok.*;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Expense{

    public enum Type{
        INCOME,
        EXPENSE
    }
    public enum MainCategory{
        HOME,
        FOOD,
        SHOPPING,
        CAR,
        DRESSING,
        ENTERTAINMENT,
        PERSONAL,
        OTHER
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Double cost;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private MainCategory category;


}

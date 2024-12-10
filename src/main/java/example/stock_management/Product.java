package example.stock_management;
import org.springframework.data.annotation.Id;
public record Product(@Id Long id, String name,Double price, String owner) {
}

package example.stock_management;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import java.io.IOException;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ProductJsonTest {
    @Autowired
    private JacksonTester<Product> json;
    @Autowired
    private JacksonTester<Product[]> jsonList;

    private Product[] products;

    @BeforeEach
    void setUp() {
        products = Arrays.array(
                new Product(99L,"product 99", 123.45, "sarah1"),
                new Product(100L,"product 100" ,100.00, "sarah1"),
                new Product(101L,"product 101" ,150.00, "sarah1"));
    }

    @Test
    void productSerializationTest() throws IOException {
        Product product = new Product(99L, "Product 99",123.45, "sarah1");
        assertThat(json.write(product)).isStrictlyEqualToJson("expected.json");
        assertThat(json.write(product)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(product)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(99);
        assertThat(json.write(product)).hasJsonPathNumberValue("@.price");
        assertThat(json.write(product)).extractingJsonPathNumberValue("@.price")
                .isEqualTo(123.45);
    }

    @Test
    void productDeserializationTest() throws IOException {
        String expected = """
                {
                    "id": 99,
                    "name": "Product 99",
                    "price": 123.45,
                    "owner": "sarah1"
                }
                """;
        assertThat(json.parse(expected))
                .isEqualTo(new Product(99L, "Product 99",123.45, "sarah1"));
        assertThat(json.parseObject(expected).id()).isEqualTo(99);
        assertThat(json.parseObject(expected).price()).isEqualTo(123.45);
    }

    @Test
    void productListSerializationTest() throws IOException {
        assertThat(jsonList.write(products)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void productListDeserializationTest() throws IOException {
        String expected="""
         [
            { "id": 99, "name":"product 99" , "price": 123.45, "owner": "sarah1" },
            { "id": 100, "name":"product 100" ,"price": 100.00, "owner": "sarah1" },
            { "id": 101, "name":"product 101" ,"price": 150.00, "owner": "sarah1" }
         ]
         """;
        assertThat(jsonList.parse(expected)).isEqualTo(products);
    }
}

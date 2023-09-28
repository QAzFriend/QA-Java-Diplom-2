import client.OrderClient;
import client.UserClient;
import model.Order;
import model.User;
import org.junit.Before;
import org.junit.After;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest {
    OrderClient orderClient = new OrderClient();
    UserClient userClient = new UserClient();
    User user = new User("TestUser07@test.ru","TestUser07","TestUser07");
    private String accessToken;
    List<String> ingredients = new ArrayList<>();
    List<String> ingredientsNull = new ArrayList<>();


    @Before
    public void setUp(){
        orderClient.setUp();
        userClient.createUser(user);
        ValidatableResponse loginResponse = userClient.loginUser(user);
        accessToken = loginResponse.extract().path("accessToken");
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("61c0c5a71d1f82001bdaaa72");
        ingredients.add("61c0c5a71d1f82001bdaaa73");
    }

    @Test
    @DisplayName("Check create a new order authorized user (200)")
    @Description("An order with valid data is created successfully")
    public void checkCreateOrderAuthorizedUserWithCorrectIngredients(){
        Order order = new Order(ingredients);
        ValidatableResponse createOrderResponse = orderClient.createOrder(order, accessToken);
        createOrderResponse
                .assertThat().body("success", equalTo(true))
                .assertThat().body("name", notNullValue())
                .assertThat().body("order.number", notNullValue())
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Check create a new order unauthorized user (200)")
    @Description("An order with valid data is created successfully")
    public void checkCreateOrderUnauthorizedUserWithCorrectIngredients(){
        Order order = new Order(ingredients);
        ValidatableResponse createOrderResponse = orderClient.createOrderUnauthorized(order);
        createOrderResponse
                .assertThat().body("success", equalTo(true))
                .assertThat().body("name", notNullValue())
                .assertThat().body("order.number", notNullValue())
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Check create a new order without ingredients")
    @Description("Create order without ingredients isn't possible")
    public void checkCreateOrderWithoutIngredients(){
        Order order = new Order(ingredientsNull);
        ValidatableResponse createOrderResponse = orderClient.createOrder(order, accessToken);
        createOrderResponse
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("Ingredient ids must be provided"))
                .and().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Check create a new order with incorrect ingredients (500)")
    @Description("Create order with incorrect ingredients isn't possible")
    public void checkCreateOrderWithWrongIngredients(){
        Order order = new Order();
        orderClient.createOrderWithWrongIngredients(order)
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserClient.deleteUser(accessToken);
        } }

}

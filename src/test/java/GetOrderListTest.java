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

public class GetOrderListTest {
    UserClient userClient = new UserClient();
    OrderClient orderClient = new OrderClient();
    private String accessToken;
    User user = new User("TestUser07@test.ru","TestUser07","TestUser07");
    List<String> ingredients = new ArrayList<>();

    @Before
    public void setUp() {
        orderClient.setUp();
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("61c0c5a71d1f82001bdaaa72");
        ingredients.add("61c0c5a71d1f82001bdaaa73");
    }
    @Test
    @DisplayName("Check get user order list (200)")
    @Description("Get  successfully")
    public void checkGetOrderList(){
        //Создали юзера и получили токен
        ValidatableResponse createUserResponse = userClient.createUser(user);
        accessToken = createUserResponse.extract().path("accessToken");
        // Создали заказ
        Order order = new Order(ingredients);
        orderClient.createOrder(order, accessToken);
        // Получаем данные о заказе
        ValidatableResponse getOrderResponse = orderClient.getUserOrdersList(accessToken);
        getOrderResponse
                .assertThat().body("success", equalTo(true))
                .assertThat().body("orders.number", notNullValue())
                .assertThat().body("orders.ingredients", notNullValue())
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Check get user order list without authorization (401)")
    @Description("Getting a list of orders is not possible")
    public void checkGetOrderListUnauthorizedUser(){
        //Создали юзера и получили токен
        ValidatableResponse createUserResponse = userClient.createUser(user);
        accessToken = createUserResponse.extract().path("accessToken");
        // Создали заказ
        Order order = new Order(ingredients);
        orderClient.createOrder(order, accessToken);

        // Получаем данные о заказе
        ValidatableResponse getOrderResponse = orderClient.getUserOrdersList("");
        getOrderResponse
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("You should be authorised"))
                .and().statusCode(SC_UNAUTHORIZED);

    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserClient.deleteUser(accessToken);
        } }


}


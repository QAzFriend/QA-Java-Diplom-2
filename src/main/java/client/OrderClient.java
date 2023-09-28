package client;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import model.Order;
import static io.restassured.RestAssured.given;

public class OrderClient extends ApiBase {

    private final static String CREATE_ORDER_ENDPOINT = "/api/orders";

    @Step("Create new order. POST /api/orders")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .and()
                .body(order)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then();
    }

    @Step("Create new order unauthorized user. POST /api/orders")
    public ValidatableResponse createOrderUnauthorized(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then();
    }

    @Step("Create new order unauthorized user. POST /api/orders")
    public ValidatableResponse createOrderWithWrongIngredients(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body("{\"ingredients\":[\"61c0c5awed1f82001bdaaa72\",\"61c0c5wed1f82001bdaaa71\",\"61c0c555d1f82001bdaaa6d\"]}")
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then();
    }
    @Step("Get user orders")
    public ValidatableResponse getUserOrdersList(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get(CREATE_ORDER_ENDPOINT)
                .then();
    }
}

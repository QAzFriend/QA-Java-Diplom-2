package client;
import io.restassured.RestAssured;
public class ApiBase {

    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }
}
import client.UserClient;
import model.User;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.Before;
import org.junit.After;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class LoginUserTest {
    UserClient userClient = new UserClient();

    User user;
    private String accessToken;

    @Before
    public void setUp(){
        userClient.setUp();
        user = ((new User("TestUser07@test.ru","TestUser07","TestUser07")));
    }

    @Test
    @DisplayName("Check successful authorization")
    @Description("Authorization with correct data is successful")
    public void checkLoginCourier(){
        userClient.createUser(user);
        ValidatableResponse loginResponse = userClient.loginUser(user);
        accessToken = loginResponse.extract().path("accessToken");
        loginResponse
                .assertThat().body("success", equalTo(true))
                .assertThat().body("user.email", IsEqualIgnoringCase.equalToIgnoringCase(user.getEmail()))
                .assertThat().body("user.name", IsEqualIgnoringCase.equalToIgnoringCase(user.getName()))
                .assertThat().body("accessToken", notNullValue())
                .assertThat().body("refreshToken", notNullValue())
                .and().statusCode(SC_OK);
    }
    @Test
    @DisplayName("Check authorization with incorrect login (401)")
    @Description("Authorization with incorrect login isn't possible")
    public void checkLoginWithIncorrectLogin() {
        userClient.createUser(user);
        ValidatableResponse loginResponse = userClient.loginUser(new User("IncorrectTestUser0701@test.ru", "TestUser07", "TestUser07"));
        loginResponse
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and().statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Check authorization with incorrect password (401)")
    @Description("Authorization with incorrect password isn't possible")
    public void checkLoginWithIncorrectPassword() {
        userClient.createUser(user);
        ValidatableResponse loginResponse = userClient.loginUser(new User("TestUser07@test.ru", "IncorrectTestUser0701", "TestUser07"));
        loginResponse
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and().statusCode(SC_UNAUTHORIZED);
    }


    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserClient.deleteUser(accessToken);
        } }

}


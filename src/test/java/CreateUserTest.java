import client.UserClient;
import model.User;
import org.junit.After;
import org.junit.Before;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;


import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CreateUserTest {
    UserClient userClient = new UserClient();
    User user;
    private String accessToken;

    @Before
    public void setUp(){
        userClient.setUp();
        user = (new User("TestUser07@test.ru", "TestUser07", "TestUser07"));
    }

    @Test
    @DisplayName("Check create new user (200)")
    @Description("Test success create user")
    public void checkCreateUser(){
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
        createResponse
                .assertThat().body("success", equalTo(true))
                .assertThat().body("accessToken", notNullValue())
                .assertThat().body("refreshToken", notNullValue())
                .and()
                .statusCode(SC_OK);
    }
    @Test
    @DisplayName("Check create duplicate user (403)")
    @Description("Creating a duplicate user is not possible")
    public void checkCreateDuplicateUser(){
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
        ValidatableResponse createResponseDuplicate =  userClient.createUser(user);
        createResponseDuplicate
                .assertThat().body("message", equalTo("User already exists"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Check create new user without Name (403)")
    @Description("Test success create courier without firstName")
    public void checkCreateCourierWithoutName() {
        ValidatableResponse createResponse =  userClient.createUser(new User("TestUser07@test.ru", "TestUser07", ""));
        createResponse
                .assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }
    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserClient.deleteUser(accessToken);
        } }
}


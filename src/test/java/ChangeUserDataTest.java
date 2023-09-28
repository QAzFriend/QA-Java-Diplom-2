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
public class ChangeUserDataTest {

    UserClient userClient = new UserClient();

    User user;
    private String accessToken;

    private final String newEmail = "newEmail@@test.ru";
    private final String newPassword = "newPassword";
    private final String newName = "newName";

    @Before
    public void setUp(){
        userClient.setUp();
        user = ((new User("TestUser07@test.ru","TestUser07","TestUser07")));
    }

    @Test
    @DisplayName("Check changing email of an authorized user (200)")
    @Description("Change email authorized user is successful")
    public void checkChangeUserLogin(){
        userClient.createUser(user); //
        ValidatableResponse loginResponse = userClient.loginUser(user);
        accessToken = loginResponse.extract().path("accessToken");
        ValidatableResponse changeDataResponse = userClient.changeDataUser(new User(newEmail,"TestUser07","TestUser07"), accessToken);
        changeDataResponse
                .assertThat().body("success", equalTo(true))
                .assertThat().body("user.email", IsEqualIgnoringCase.equalToIgnoringCase(newEmail))
                .assertThat().body("user.name", IsEqualIgnoringCase.equalToIgnoringCase(user.getName()))
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Check changing name of an authorized user (200)")
    @Description("Change name authorized user is successful")
    public void checkChangeUserName(){
        userClient.createUser(user);
        ValidatableResponse loginResponse = userClient.loginUser(user);
        accessToken = loginResponse.extract().path("accessToken");
        ValidatableResponse changeDataResponse = userClient.changeDataUser(new User("TestUser07@test.ru","TestUser07",newName), accessToken);
        changeDataResponse
                .assertThat().body("success", equalTo(true))
                .assertThat().body("user.email", IsEqualIgnoringCase.equalToIgnoringCase(user.getEmail()))
                .assertThat().body("user.name", IsEqualIgnoringCase.equalToIgnoringCase(newName))
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Check changing password of an authorized user (200)")
    @Description("Change password authorized user is successful")
    public void checkChangeUserPassword(){
        userClient.createUser(user);
        ValidatableResponse loginResponse = userClient.loginUser(user);
        accessToken = loginResponse.extract().path("accessToken");
        ValidatableResponse changeDataResponse = userClient.changeDataUser(new User("TestUser07@test.ru",newPassword,"TestUser07"), accessToken);
        changeDataResponse
                .assertThat().body("success", equalTo(true))
                .assertThat().body("user.email", IsEqualIgnoringCase.equalToIgnoringCase(user.getEmail()))
                .assertThat().body("user.name", IsEqualIgnoringCase.equalToIgnoringCase(user.getName()))
                .and().statusCode(SC_OK);
    }

// НЕГАТИВНЫЕ ПРОВЕРКИ

    @Test
    @DisplayName("Check changing email of an unauthorized user (401)")
    @Description("Change email unauthorized user is isn't possible")
    public void checkChangeUserLoginUnauthorized(){
        userClient.createUser(user);
        ValidatableResponse changeDataResponse = userClient.changeDataUser(new User(newEmail,"TestUser07","TestUser07"), "");
        changeDataResponse
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("You should be authorised"))
                .and().statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Check changing password of an unauthorized user (401)")
    @Description("Change password unauthorized user is isn't possible")
    public void checkChangeUserPasswordUnauthorized(){
        userClient.createUser(user);
        ValidatableResponse changeDataResponse = userClient.changeDataUser(new User("TestUser07@test.ru",newPassword,"TestUser07"), "");
        changeDataResponse
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("You should be authorised"))
                .and().statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Check changing name of an unauthorized user (401)")
    @Description("Change name unauthorized user is isn't possible")
    public void checkChangeUserNamedUnauthorized(){
        userClient.createUser(user);
        ValidatableResponse changeDataResponse = userClient.changeDataUser(new User("TestUser07@test.ru","TestUser07",newName), "");
        changeDataResponse
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


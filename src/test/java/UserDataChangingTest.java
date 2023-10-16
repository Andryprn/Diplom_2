import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.user.User;
import site.nomoreparties.stellarburgers.user.UserRandomizer;
import site.nomoreparties.stellarburgers.user.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static site.nomoreparties.stellarburgers.user.UserRandomizer.faker;

public class UserDataChangingTest {
    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private User user;
    private String accessToken;
    private String name;
    private String email;
    private String password;

    @Before
    public void setUp() {
        user = UserRandomizer.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    @Description("Создание пользователя, изменение логина и почты, проверка кода ответа 200")
    public void userDataChangingTestWithAuthorizationShouldUpdateUserData() {
        user.setName(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        response = userSteps.userProfileChanging(user, accessToken);
        user.setName(name);
        user.setEmail(email);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение пароля пользователя с авторизацией")
    @Description("Создание пользователя, изменение пароля, проверка кода ответа 200")
    public void userPasswordChangingTestWithAuthorizationShouldUpdatePassword() {
        user.setPassword(faker.internet().password());
        response = userSteps.userProfileChanging(user, accessToken);
        user.setPassword(password);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение пароля пользователя без авторизации")
    @Description("Создание пользователя, изменение пароля, проверка кода ответа 401")
    public void userPasswordChangingTestWithoutAuthorizationShouldReturnError() {
        user.setPassword(faker.internet().password());
        response = userSteps.userProfileChanging(user, "");
        user.setPassword(password);
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Создание пользователя, изменение логина и почты, проверка кода ответа 401")
    public void userDataChangingTestWithoutAuthorizationShouldReturnError() {
        user.setName(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        response = userSteps.userProfileChanging(user, "");
        user.setName(name);
        user.setEmail(email);
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}
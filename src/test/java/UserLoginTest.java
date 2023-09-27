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

public class UserLoginTest {
    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private User user;
    private String accessToken;

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
    @DisplayName("Авторизация под существующим пользователем")
    @Description("Создание рандомного пользователя и авторизация с валидными данными")
    public void loginUserMustBeSuccessful() {
        response = userSteps.userLoginWithEmailAndToken(user, accessToken);
        response
                .then().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Авторизация с неверным логином и паролем")
    @Description("Создание рандомного пользователя и авторизация с невалидными данными, проверяем код ответа 401")
    public void loginUserWithWrongPasswordAndEmailMustReturnError() {
        String email = user.getEmail();
        user.setEmail("wrongRandom@email.ru");
        String password = user.getPassword();
        user.setPassword("wrongRandomPassword");
        response = userSteps.userLoginWithEmailAndToken(user, accessToken);
        user.setEmail(email);
        user.setPassword(password);
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}
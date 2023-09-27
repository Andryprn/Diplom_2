import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import site.nomoreparties.stellarburgers.user.User;
import site.nomoreparties.stellarburgers.user.UserRandomizer;
import site.nomoreparties.stellarburgers.user.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserCreateTest {
    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private User user;
    private String accessToken;

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }

    @Test
    @DisplayName("Проверка создания нового пользователя с рандомными валидными данными")
    @Description("Проверяем, что токен не пустой")
    public void createUserTestMustBeSuccessful() {
        user = UserRandomizer.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response
                .then().body("accessToken", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Проверка создания нового пользователя с уже указанными ранее данными")
    @Description("Проверяем код ответа 403 и текст ошибки о существовании такого пользователя")
    public void createSameUserTestMustReturnError() {
        user = UserRandomizer.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .statusCode(200);
        Response responseWithExistUser = userSteps.userCreate(user);
        responseWithExistUser.then()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Проверка создания нового пользователя со всеми пустыми полями данных")
    @Description("Проверяем код ответа 403 и текст ошибки об обязательности всех полей")
    public void createNoDataUserTestMustReturnError() {
        user = UserRandomizer.createRandomNoDataUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Проверка создания нового пользователя с пустым полем имени")
    @Description("Проверяем код ответа 403 и текст ошибки об обязательности всех полей")
    public void createNoNameUserTestMustReturnError() {
        user = UserRandomizer.createRandomNoNameUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Проверка создания нового пользователя с пустым полем почты")
    @Description("Проверяем код ответа 403 и текст ошибки об обязательности всех полей")
    public void createNoEmailUserTestMustReturnError() {
        user = UserRandomizer.createRandomNoEmailUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Проверка создания нового пользователя с пустым полем пароля")
    @Description("Проверяем код ответа 403 и текст ошибки об обязательности всех полей")
    public void createNoPasswordUserTestMustReturnError() {
        user = UserRandomizer.createRandomNoPasswordUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }
}
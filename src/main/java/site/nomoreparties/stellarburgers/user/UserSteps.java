package site.nomoreparties.stellarburgers.user;

import site.nomoreparties.stellarburgers.constant.ApiEndpoints;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserSteps {
    public static RequestSpecification requestSpec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(ApiEndpoints.BASE_URL);
    }

    @Step("Регистрация нового пользователя")
    public Response userCreate(User user) {
        return requestSpec()
                .body(user)
                .post(ApiEndpoints.REGISTER);
    }

    @Step("Удаление профиля пользователя")
    public void userDelete(String token) {
        requestSpec()
                .header("Authorization", token)
                .delete(ApiEndpoints.USER);
    }

    @Step("Изменение профиля пользователя")
    public Response userProfileChanging(User newUser, String token) {
        return requestSpec()
                .header("Authorization", token)
                .body(newUser)
                .patch(ApiEndpoints.USER);
    }

    @Step("Авторизация пользователя по логину и токену")
    public Response userLoginWithEmailAndToken(User user, String token) {
        return requestSpec()
                .header("Authorization", token)
                .body(user)
                .post(ApiEndpoints.LOGIN);
    }

    @Step("Авторизация пользователя по логину")
    public Response userLogin(User user) {
        return requestSpec()
                .body(user)
                .post(ApiEndpoints.LOGIN);
    }

}

package site.nomoreparties.stellarburgers.order;

import site.nomoreparties.stellarburgers.constant.ApiEndpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("Создание заказа с авторизацией")
    public Response createOrderWithToken(Order order, String token) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .baseUri(ApiEndpoints.BASE_URL)
                .body(order)
                .post(ApiEndpoints.ORDERS);
    }
    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutToken(Order order) {
        return given()
                .header("Content-Type", "application/json")
                .baseUri(ApiEndpoints.BASE_URL)
                .body(order)
                .post(ApiEndpoints.ORDERS);
    }


    @Step("Запрос данных о заказах пользователя по токену")
    public Response getUserOrders(String accessToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(ApiEndpoints.BASE_URL)
                .get(ApiEndpoints.ORDERS);
    }

    @Step("Запрос данных об ингредиентах")
    public Response getIngredients() {
        return given()
                .header("Content-Type", "application/json")
                .baseUri(ApiEndpoints.BASE_URL)
                .get(ApiEndpoints.INGREDIENTS);
    }}
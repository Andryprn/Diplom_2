import site.nomoreparties.stellarburgers.constant.ApiEndpoints;
import site.nomoreparties.stellarburgers.ingredients.Ingredient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.order.Order;
import site.nomoreparties.stellarburgers.order.OrderSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.user.User;
import site.nomoreparties.stellarburgers.user.UserRandomizer;
import site.nomoreparties.stellarburgers.user.UserSteps;

import static site.nomoreparties.stellarburgers.ingredients.IngredientRequest.getIngredientFromArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static site.nomoreparties.stellarburgers.user.UserRandomizer.faker;

public class OrderGetTest {

    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();
    private Response response;
    private String accessToken;
    private Ingredient validIngredient;

    @Before
    public void setUp() {
        RestAssured.baseURI = ApiEndpoints.BASE_URL;
        validIngredient = getIngredientFromArray();
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersWithAuthorizedUserShouldReturnOk() {
        User user = UserRandomizer.createNewRandomUser();
        Order order = new Order(validIngredient);
        response = userSteps.userCreate(user);
        accessToken = response.then().extract().body().path("accessToken");
        response = orderSteps.createOrderWithToken(order, accessToken);
        response = orderSteps.getUserOrders(accessToken);
        response.then()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getOrdersWithUnauthorizedUserShouldReturnError() {
        Order order = new Order(validIngredient);
        response = orderSteps.createOrderWithToken(order, String.valueOf(faker.random().hashCode()));
        response = orderSteps.getUserOrders(String.valueOf(faker.random().hashCode()));
        response.then()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }
}
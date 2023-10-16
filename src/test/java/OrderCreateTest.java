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

public class OrderCreateTest {
    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();
    private String accessToken;
    private Response response;
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
    @DisplayName("Создание заказа с авторизацией пользователя и с валидным хэшем ингредиентов")
    public void createOrderWithUserLoginAndCorrectIngHashShouldReturnOk() {
        User user = UserRandomizer.createNewRandomUser();
        Order order = new Order(validIngredient);
        response = userSteps.userCreate(user);
        accessToken = response.then().extract().body().path("accessToken");
        response = orderSteps.createOrderWithToken(order, accessToken);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа без авторизации пользователя и с пустым хэшем ингредиентов")
    public void createOrderWithoutUserLoginAndEmptyIngHashShouldReturnOk() {
        Order order = new Order(validIngredient);
        response = orderSteps.createOrderWithToken(order, "");
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа без хэша ингредиентов")
    public void createOrderWithoutUserLoginAndWithoutIngHashShouldReturnError() {
        User user = UserRandomizer.createNewRandomUser();
        Order order = new Order();
        response = userSteps.userCreate(user);
        response = orderSteps.createOrderWithoutToken(order);
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Создание заказа с пустым списком ингредиентов")
    public void createOrderWithoutIngredientsShouldReturnError() {
        User user = UserRandomizer.createNewRandomUser();
        Order order = new Order();
        response = userSteps.userCreate(user);
        response = orderSteps.createOrderWithoutToken(order);
        response.then()
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithWrongIngredientHashReturnError() {
        validIngredient.set_id("MutantIngredientTokenWussHere.");
        Order order = new Order(validIngredient);
        User user = UserRandomizer.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response.then().extract().body().path("accessToken");
        response = orderSteps.createOrderWithToken(order, accessToken);
        response.then().
                statusCode(500);
    }

    @Test
    @DisplayName("Проверка наличия ингредиентов в базе")
    public void getStatusOfIngredients() {
        response = orderSteps.getIngredients();
        response.then()
                .body("success", equalTo(true))
                .and()
                .body("data", notNullValue())
                .and()
                .statusCode(200);
    }

}
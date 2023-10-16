package site.nomoreparties.stellarburgers.ingredients;

import site.nomoreparties.stellarburgers.constant.ApiEndpoints;

import static io.restassured.RestAssured.given;

public class IngredientRequest {

    public static Ingredient[] getIngredientsArray() {
        return getIngredientResponse().getIngredients();
    }

    public static IngredientResponse getIngredientResponse() {
        return given()
                .get(ApiEndpoints.INGREDIENTS)
                .as(IngredientResponse.class);
    }

    public static Ingredient getIngredientFromArray() {
        return getIngredientsArray()[0];
    }
}

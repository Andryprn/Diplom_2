package site.nomoreparties.stellarburgers.order;

import site.nomoreparties.stellarburgers.ingredients.Ingredient;

public class Order {
    private String[] ingredients;
    private String _id;
    private String status;
    private String number;

    public Order() {
    }

    public Order(Ingredient ingredient) {
        this.ingredients = new String[]{
                ingredient.get_id()
        };
    }

}

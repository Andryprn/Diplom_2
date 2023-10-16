package site.nomoreparties.stellarburgers.user;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class UserRandomizer {

    public static Faker faker = new Faker();

    @Step("Создание нового пользователя с рандомными данными")
    public static User createNewRandomUser() {
        return new User(
                faker.name().firstName(),
                faker.internet().emailAddress(),
                faker.internet().password());
    }
    @Step("Создание пользователя с рандомными данными без логина")
    public static User createRandomNoNameUser() {
        return new User(
                "",
                faker.internet().emailAddress(),
                faker.internet().password());
    }
    @Step("Создание пользователя с рандомными данными без почты")
    public static User createRandomNoEmailUser() {
        return new User(
                faker.name().firstName(),
                "",
                faker.internet().password());
    }
    @Step("Создание пользователя с рандомными данными без пароля")
    public static User createRandomNoPasswordUser() {
        return new User(
                faker.name().firstName(),
                faker.internet().emailAddress(),
                "");
    }
    @Step("Создание пользователя с пустыми данными")
    public static User createRandomNoDataUser() {
        return new User(
                "",
                "",
                "");
    }
}

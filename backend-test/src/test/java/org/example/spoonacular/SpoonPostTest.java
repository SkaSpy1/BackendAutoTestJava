package org.example.spoonacular;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;


import static io.restassured.RestAssured.given;

public class SpoonPostTest extends AbstractTest {
    private static final String API_KEY = "ed6729b7677842cabc721f7e4bcb5641";
    private static RequestSpecification BASE_SPEC;

    @BeforeAll
    static void beforeAll() {

        RestAssured.baseURI = "https://api.spoonacular.com";
        BASE_SPEC = new RequestSpecBuilder()
                .addQueryParam("apiKey", API_KEY)
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    public void postRequest() {

        Response response = given()
                .spec(BASE_SPEC)
                .queryParam("title", "Homemade dumplings")
                .queryParam("ingredientList", "flour")
                .header("Content-type", "application/json")
                .and()
                .when()
                .post("recipes/cuisine")
                .then()
                .log()
                .all()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());

    }

    @Test
    public void postRequest2() {

        Response response = given()
                .spec(BASE_SPEC)
                .queryParam("title", "KuraMura")
                .queryParam("ingredientList", "chicken \\ cheese \\ apple \\ purring")
                .header("Content-type", "application/json")
                .and()
                .when()
                .post("recipes/cuisine")
                .then()
                .log()
                .all()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());

    }

    @Test
    public void postRequest3() {

        File jsonData = new File
                ("C:\\Users\\antik\\IdeaProjects\\backend-test\\src\\test\\resources\\org\\example\\spoonacular\\1.json");
        Response response = given()
                .spec(BASE_SPEC)
                .queryParam("username", "SkaSpy")
                .queryParam("hash", "a76077e2cec479fcef00e05e2dd96ea120506b6b")
                .header("Content-type", "application/json")
                .body(jsonData)
                .and()
                .when()
                .post("mealplanner/tat88/items")
                .then()
                .log()
                .all()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());

    }

}



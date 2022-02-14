package org.example.spoonacular;

import io.restassured.RestAssured;
import org.example.AbstractTest;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class SpoonacularAuthorTest extends AbstractTest {
    private static String API_KEY = "ed6729b7677842cabc721f7e4bcb5641";

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://api.spoonacular.com";
    }

    @Test
    void testGetRecipesComplexSearch() throws IOException, JSONException {

        String actual = given()
                .param("apiKey", API_KEY)
                .param("author", "coffeebean")
                .param("diet", "vegetarian")
                .param("number", "3")
                .log()
                .all()
                .expect()
                .statusCode(200)
                .time(Matchers.lessThan(3000l))
                .body("offset", Matchers.is(0))
                .body("number", Matchers.is(3))
                .appendRootPath("id")
                .appendRootPath("1042324")
                .log()
                .all()
                .when()
                .get("recipes/complexSearch")
                .body()
                .asPrettyString();
        String expected = getResourceAsString("expected2.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
        System.out.println(actual);
    }
}


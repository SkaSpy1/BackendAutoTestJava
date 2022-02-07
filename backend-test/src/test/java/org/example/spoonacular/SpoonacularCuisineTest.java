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

public class SpoonacularCuisineTest  extends AbstractTest {
    private static String API_KEY = "ed6729b7677842cabc721f7e4bcb5641";

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://api.spoonacular.com";
    }

    @Test
    void testGetRecipesComplexSearch() throws IOException, JSONException {

        String actual = given()
                .param("apiKey", API_KEY)
                .param("cuisine", "italian")
                .param("diet", "vegetarian")
                .log()
                .all()
                .expect()
                .statusCode(200)
                .time(Matchers.lessThan(3000l))
                .body("offset", Matchers.is(0))
                .body("number", Matchers.is(10))
                .log()
                .all()
                .when()
                .get("recipes/complexSearch")
                .body()
                .asPrettyString();
        String expected = getResourceAsString("expected1.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
        System.out.println(actual);
    }
}

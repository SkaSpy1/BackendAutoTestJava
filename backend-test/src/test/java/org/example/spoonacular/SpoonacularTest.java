package org.example.spoonacular;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestLogSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import net.javacrumbs.jsonunit.JsonAssert;
import okhttp3.internal.io.FileSystem;
import org.example.AbstractTest;
import org.example.ImageClassifierResponse;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.hamcrest.Matchers.is;

public class SpoonacularTest extends AbstractTest {
    private static String API_KEY = "ed6729b7677842cabc721f7e4bcb5641";
    private static RequestSpecification BASE_SPEC;
    private static ResponseSpecification RESPONSE_SPEC;


    @BeforeAll
    static void beforeAll() {

        RestAssured.baseURI = "https://api.spoonacular.com";
        BASE_SPEC = new RequestSpecBuilder()
                .addQueryParam("apiKey", API_KEY)
                .log(LogDetail.ALL)
                .build();
        RESPONSE_SPEC = new ResponseSpecBuilder()
                .log(LogDetail.BODY)
                .build();

    }

    public static Stream<Arguments> testImageClassificationData() {
        return Stream.of(
                Arguments.of("burger", "burger.jpg"),
                Arguments.of("pasta", "pasta.jpg"),
                Arguments.of("pizza", "pizza.jpg")
        );
    }

    @Test
    void testGetRecipesComplexSearch() throws IOException, JSONException {


        String actual = given()
                .spec(BASE_SPEC)
                //.param("apiKey", API_KEY)
                .param("query", "pasta")
                .param("number", "3")
                //    .log()
                //     .all()
                .expect()
                .statusCode(200)
                .time(Matchers.lessThan(5000l))
                .body("offset", is(0))
                .body("number", is(3))
                .spec(RESPONSE_SPEC)
                .log()
                .all()
                .when()
                .get("recipes/complexSearch")
                .body()
                .asPrettyString();
        String expected = getResourceAsString("expected.json");

        JsonAssert.assertJsonEquals(expected, actual, JsonAssert.when(IGNORING_ARRAY_ORDER));
        System.out.println(actual);
    }

    @ParameterizedTest
    @MethodSource("testImageClassificationData")
    void testImageClassification(String dir, String resource) throws IOException {

        String separator = FileSystems.getDefault().getSeparator();
        File file = getFile("images" + "/" + dir + "/" + resource);

        //   String actual =
        ImageClassifierResponse response = given()
                .spec(BASE_SPEC)
                //.queryParam("apiKey", API_KEY)
                .multiPart("file", file)
                //       .log()
                //       .all()
                .expect()
                .body("status", is("success"))
                .body("category", is(dir))
                .body("probability", Matchers.greaterThan(0.9f))
                .spec(RESPONSE_SPEC)
                .log()
                .all()
                .when()
                .post("food/images/classify")
                .as(ImageClassifierResponse.class);

        ImageClassifierResponse expected = ImageClassifierResponse.builder()

                .status("success")
                .category(dir)

                .build();


        Assertions.assertEquals(expected.getStatus(), response.getStatus());


//                .body()
//                .asPrettyString();
        //      String expected = getResourceAsString("images" + separator + dir + separator + resource+"expected.json");

        //      JsonAssert.assertJsonEquals(
        //              expected,
        //              actual,
        //              JsonAssert.when(IGNORING_ARRAY_ORDER));
    }

}

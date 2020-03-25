package controllers;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseApiTest {
    @BeforeAll
    public static void setup() {
        RestAssured.useRelaxedHTTPSValidation();
    }
}

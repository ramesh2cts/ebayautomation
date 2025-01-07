package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CoinDeskApiTest {

    @Test
    public void verifyBpiUSDGBP() {
        // Send GET request to the CoinDesk API
        RestAssured.baseURI = "https://api.coindesk.com/v1/bpi/currentprice.json";
        Response response = given().get();

        // Verify the response contains 3 BPIs (USD, GBP, EUR)
        response.then().statusCode(200);

        // Verify that the response contains USD, GBP, and EUR
        assertThat(response.jsonPath().get("bpi.USD"), notNullValue());
        assertThat(response.jsonPath().get("bpi.GBP"), notNullValue());
        assertThat(response.jsonPath().get("bpi.EUR"), notNullValue());

        // Verify that the 'description' for GBP equals "British Pound Sterling"
        String gbpDescription = response.jsonPath().getString("bpi.GBP.description");
        assertThat(gbpDescription, equalTo("British Pound Sterling"));

        System.out.println("USD BPI: " + response.jsonPath().getString("bpi.USD.rate"));
        System.out.println("GBP BPI: " + response.jsonPath().getString("bpi.GBP.rate"));
        System.out.println("EUR BPI: " + response.jsonPath().getString("bpi.EUR.rate"));
    }
}
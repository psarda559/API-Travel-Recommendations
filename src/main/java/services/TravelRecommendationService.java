package services;

import base.BaseClass;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Optional;

public class TravelRecommendationService extends BaseClass {

    static String baseUrl = prop.getProperty("baseURL");
    static String travelRecommendationPATH = prop.getProperty("travelRecommendationsPath");

    public static Response getTravelRecommendations(@Optional String cityCodes, @Optional String travelerCountryCode,
                                                    @Optional String destinationCountryCodes) {

        return RestAssured.given()
                .baseUri(baseUrl)
                .basePath(travelRecommendationPATH)
                .header("Authorization", "Bearer " + BaseClass.getToken())
                .queryParam("cityCodes", cityCodes)
                .queryParam("travelerCountryCode", travelerCountryCode)
                .queryParam("destinationCountryCodes", destinationCountryCodes)
                .get().then().log().all().extract().response();
    }

    public static Response getTravelRecommendationsWithTravelerCountryCode(@Optional String cityCodes, @Optional String travelerCountryCode) {

        return RestAssured.given()
                .baseUri(baseUrl)
                .basePath(travelRecommendationPATH)
                .header("Authorization", "Bearer " + BaseClass.getToken())
                .queryParam("cityCodes", cityCodes)
                .queryParam("travelerCountryCode", travelerCountryCode)
                .get().then().log().all().extract().response();
    }

    public static Response getTravelRecommendationsWithDestinationCountryCodes(@Optional String cityCodes, @Optional String destinationCountryCodes) {

        return RestAssured.given()
                .baseUri(baseUrl)
                .basePath(travelRecommendationPATH)
                .header("Authorization", "Bearer " + BaseClass.getToken())
                .queryParam("cityCodes", cityCodes)
                .queryParam("destinationCountryCodes", destinationCountryCodes)
                .get().then().log().all().extract().response();
    }

    public static Response getTravelRecommendations(@Optional String cityCodes) {

        return RestAssured.given()
                .baseUri(baseUrl)
                .basePath(travelRecommendationPATH)
                .header("Authorization", "Bearer " + BaseClass.getToken())
                .queryParam("cityCodes", cityCodes)
                .get().then().log().all().extract().response();
    }

    public static Response getTravelRecommendationsWithInvalidToken(@Optional String cityCodes,
                                                                    @Optional String travelerCountryCode,
                                                                    @Optional String destinationCountryCodes) {

        return RestAssured.given()
                .baseUri(baseUrl)
                .basePath(travelRecommendationPATH)
                .header("Authorization", "Bearer " + BaseClass.getToken() + "InvalidToken")
                .queryParam("cityCodes", cityCodes)
                .queryParam("travelerCountryCode", travelerCountryCode)
                .queryParam("destinationCountryCodes", destinationCountryCodes)
                .get().then().log().all().extract().response();
    }

    public static Response getTravelRecommendationsWithInvalidParam(@Optional String cityCodes,
                                                                    @Optional String travelerCountryCode,
                                                                    @Optional String destinationCountryCodes) {

        return RestAssured.given()
                .baseUri(baseUrl)
                .basePath(travelRecommendationPATH)
                .header("Authorization", "Bearer " + BaseClass.getToken())
                .queryParam("cityCodes", cityCodes)
                .queryParam("travelerCountryCode", travelerCountryCode)
                .queryParam("destinationCountryCodes", destinationCountryCodes)
                .queryParam("invalidParam", "InvalidParam")
                .get().then().log().all().extract().response();
    }

    public static Response getInternalServerError(@Optional String cityCodes,
                                                  @Optional String travelerCountryCode,
                                                  @Optional String destinationCountryCodes) {

        return RestAssured.given()
                .baseUri(baseUrl)
                .basePath(travelRecommendationPATH)
                .header("Authorization", "Bearer " + BaseClass.getToken())
                .queryParam("cityCodes", cityCodes)
                .queryParam("travelerCountryCode", travelerCountryCode)
                .queryParam("destinationCountryCodes", destinationCountryCodes)
                .body("{ \"key1\": \"value1\" }")
                .get().then().log().all().extract().response();
    }
}

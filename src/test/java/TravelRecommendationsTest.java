import base.BaseClass;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import services.TravelRecommendationService;
import utils.ExcelUtils;

import java.io.IOException;

public class TravelRecommendationsTest extends BaseClass {

    public static String cityCodes;
    public static String travelerCountryCode;
    public static String destinationCountryCodes;

    @BeforeSuite(alwaysRun = true)
    public static void beforeSuite() {
        cityCodes = prop.getProperty("cityCodes");
        travelerCountryCode = prop.getProperty("travelerCountryCode");
        destinationCountryCodes = prop.getProperty("destinationCountryCodes");
    }

    @DataProvider(name = "excelData")
    public Object[][] getData() throws IOException {
        String excelFilePath = System.getProperty("user.dir")+"\\src\\main\\resources\\travelRecommendationsTestData.xlsx";
        return ExcelUtils.getExcelData(excelFilePath).toArray(new Object[0][0]);
    }

    @Test(description = "Verify 200 Status", priority = 1, dataProvider = "excelData")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description : Verify travel recommendations for multiple valid input combinations of city codes, traveler country code and destination country codes")
    public void testGetTravelRecommendations(String cityCodes,String travelerCountryCode, String destinationCountryCodes) {
        Response response = TravelRecommendationService.getTravelRecommendations(cityCodes, travelerCountryCode, destinationCountryCodes);
        int statusCode = response.getStatusCode();
        JsonPath jsonPath = this.getJsonPath(response);
        Assert.assertNotNull(jsonPath.getString("data[0].name"));
        Assert.assertEquals(jsonPath.getString("data[0].subtype"),"CITY");
        Assert.assertEquals(statusCode, 200);
    }

    @Test(description = "Verify getting travel recommendations with cityCode param", priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description : Verify travel recommendations based on city codes")
    public void testGetTravelRecommendationsWithCityCode() {
        Response response = TravelRecommendationService.getTravelRecommendations(cityCodes);
        int statusCode = response.getStatusCode();
        JsonPath jsonPath = this.getJsonPath(response);
        Assert.assertNotNull(jsonPath.getString("data[0].name"));
        Assert.assertEquals(jsonPath.getString("data[0].subtype"),"CITY");
        Assert.assertEquals(statusCode, 200);
    }

    @Test(description = "Verify getting travel recommendations with cityCode and travelerCountryCode params", priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description : Verify travel recommendations based on city codes and traveler country code")
    public void testGetTravelRecommendationsWithCityCodeAndTravelerCountryCode() {
        Response response = TravelRecommendationService
                .getTravelRecommendationsWithTravelerCountryCode(cityCodes, travelerCountryCode);
        int statusCode = response.getStatusCode();
        JsonPath jsonPath = this.getJsonPath(response);
        Assert.assertNotNull(jsonPath.getString("data[0].name"));
        Assert.assertEquals(jsonPath.getString("data[0].subtype"),"CITY");
        Assert.assertEquals(statusCode, 200);
    }

    @Test(description = "Verify getting travel recommendations with cityCode and destinationCountryCodes params", priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description : Verify travel recommendations based on city code and destination country codes")
    public void testGetTravelRecommendationsWithCityCodeAndDestinationCountryCode() {
        Response response = TravelRecommendationService
                .getTravelRecommendationsWithDestinationCountryCodes(cityCodes, destinationCountryCodes);
        int statusCode = response.getStatusCode();
        JsonPath jsonPath = this.getJsonPath(response);
        Assert.assertNotNull(jsonPath.getString("data[0].name"));
        Assert.assertEquals(jsonPath.getString("data[0].subtype"),"CITY");
        Assert.assertEquals(statusCode, 200);
    }

    @Test(description = "Verify warnings", priority = 3)
    @Severity(SeverityLevel.TRIVIAL)
    @Description("Test Description : Verify warnings when traveler country code is not supported")
    public void testGetTravelRecommendationsWhenTravelerCountryCodeIsNotSupported() {
        Response response = TravelRecommendationService
                .getTravelRecommendations(cityCodes, "IN", destinationCountryCodes);
        int statusCode = response.getStatusCode();
        JsonPath jsonPath = this.getJsonPath(response);
        Assert.assertNull(jsonPath.getString("data"));
        Assert.assertNotNull(jsonPath.getString("warnings"));
        Assert.assertEquals(statusCode, 200);
    }

    @Test(description = "Verify 400 Status when city codes are missing", priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description : Verify city codes are mandatory to get travel recommendations")
    public void testGetTravelRecommendationsWithoutCityCodeParameter(){
        Response response = TravelRecommendationService.getTravelRecommendations(null,travelerCountryCode, destinationCountryCodes );
        int statusCode = response.getStatusCode();
        JsonPath jsonPath = this.getJsonPath(response);
        Assert.assertEquals(jsonPath.getString("errors[0].title"),"MANDATORY DATA MISSING");
        Assert.assertEquals(jsonPath.getString("errors[0].detail"),"Missing cityCodes parameter");
        Assert.assertEquals(jsonPath.getString("errors[0].code"),"32171");
        Assert.assertEquals(statusCode, 400);
    }

    @Test(description = "Verify multiple invalid formats", priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description : Verify getting travel recommendations in case of invalid city codes, traveler country code and destination country codes")
    public void testGetTravelRecommendationsWithInvalidParameters(){
        Response response = TravelRecommendationService.getTravelRecommendations
                ("INVALID_CITY","INVALID_TRAVELER_COUNTRY", "INVALID_DESTINATION_COUNTRY" );
        int statusCode = response.getStatusCode();
        JsonPath jsonPath = this.getJsonPath(response);
        Assert.assertEquals(jsonPath.getString("errors[0].title"),"INVALID FORMAT");
        Assert.assertEquals(jsonPath.getString("errors[0].detail"),"Invalid cityCodes format");
        Assert.assertEquals(jsonPath.getString("errors[0].code"),"477");
        Assert.assertEquals(jsonPath.getString("errors[1].title"),"INVALID FORMAT");
        Assert.assertEquals(jsonPath.getString("errors[1].detail"),"Invalid travelerCountryCode format");
        Assert.assertEquals(jsonPath.getString("errors[1].code"),"477");
        Assert.assertEquals(jsonPath.getString("errors[2].title"),"INVALID FORMAT");
        Assert.assertEquals(jsonPath.getString("errors[2].detail"),"Invalid destinationCountryCodes format");
        Assert.assertEquals(jsonPath.getString("errors[2].code"),"477");
        Assert.assertEquals(statusCode, 400);
    }

    @Test(description = "Verify invalid query param", priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description : Verify getting travel recommendations in case of invalid option in query param")
    public void testGetTravelRecommendationsWithInvalidQueryParam(){
        Response response = TravelRecommendationService
                .getTravelRecommendationsWithInvalidParam(cityCodes, travelerCountryCode, destinationCountryCodes);
        int statusCode = response.getStatusCode();
        JsonPath jsonPath = this.getJsonPath(response);
        Assert.assertEquals(jsonPath.getString("errors[0].title"),"INVALID OPTION");
        Assert.assertEquals(jsonPath.getString("errors[0].detail"),"Invalid parameter in query");
        Assert.assertEquals(jsonPath.getString("errors[0].code"),"572");
        Assert.assertEquals(statusCode, 400);
    }

    @Test(description = "Verify 500 Status", priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description : Verify 500 status code in case of internal server error")
    public void testGetInternalServerError(){
        Response response = TravelRecommendationService
                .getInternalServerError(cityCodes,travelerCountryCode, destinationCountryCodes );
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 500);
    }

    @Test(description = "Verify 401 Status", priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Description : Verify 401 status code in case of invalid token")
    public void testGetTravelRecommendationsWithInvalidToken() {
        Response response = TravelRecommendationService.getTravelRecommendationsWithInvalidToken(cityCodes,travelerCountryCode, destinationCountryCodes);
        int statusCode = response.getStatusCode();
        JsonPath jsonPath = this.getJsonPath(response);
        Assert.assertEquals(statusCode, 401);
        Assert.assertEquals(jsonPath.getString("errors[0].title"),"Invalid access token");
    }

}
package basqar.HomeWorks;

import basqar.HomeWorks.POJO._01_BankAcc_POJO;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;


public class _01_BasqarRestAssured {

    private Cookies cookies;
    private String id;

    @BeforeClass
    public void init() {
        baseURI = "https://test.basqar.techno.study";

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "daulet2030@gmail.com");
        credentials.put("password", "TechnoStudy123@");

        cookies = given()
                .body(credentials)
                .contentType(ContentType.JSON)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().response().getDetailedCookies();
    }

    @Test
    public void createBankAcc() {
        _01_BankAcc_POJO bankAcc_pojo = new _01_BankAcc_POJO();
        bankAcc_pojo.setName(randomDataGenerator("name"));
        bankAcc_pojo.setIban(randomDataGenerator("iban"));
        bankAcc_pojo.setIntegrationCode(randomDataGenerator("integrationCode"));
        bankAcc_pojo.setCurrency("USD");
        bankAcc_pojo.setActive(true);
        bankAcc_pojo.setSchoolId("5c5aa8551ad17423a4f6ef1d");

        id = given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(bankAcc_pojo)
                .when()
                .post("/school-service/api/bank-accounts")
                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")
        ;
    }

    @Test(dependsOnMethods = "createBankAcc")
    public void deleteBankAcc(){

        given()
                .cookies(cookies)
                .when()
                .delete("/school-service/api/bank-accounts/" + id)
                .then()
                .statusCode(200)
        ;
    }

    public String randomDataGenerator(String data) {

        switch (data) {
            case "name":
                data = RandomStringUtils.randomAlphabetic(2).toUpperCase() + "Bank";
                break;
            case "iban":
                data = RandomStringUtils.randomAlphabetic(2) + RandomStringUtils.randomNumeric(20);
                break;
            case "integrationCode":
                data = RandomStringUtils.randomAlphanumeric(4).toUpperCase();
                break;
        }

        return data;
    }
}

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class LoginCourierTest extends DeleteAndCreateMethods {
    @Before
    public void setUp(){
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        createAccount();
    }

    @DisplayName("Check status code 200 and id value in successful login ")
    @Test
    public void testRequestSuccessfulLogin(){
        CourierClient courierClient = new CourierClient();
        ValidatableResponse dataCourier = courierClient.getLoginResponse(
                new Login(existingLogin, existingLoginPassword));
        dataCourier
                .statusCode(200);
        MatcherAssert.assertThat("id", notNullValue());
    }

    @DisplayName("Check status code 400 and error message in request without login field")
    @Test
    public void testRequestWithoutLogin(){
        CourierClient courierClient = new CourierClient();
        ValidatableResponse dataCourierWithoutLogin = courierClient.getLoginResponse(
                new Login(null, existingLoginPassword));
        dataCourierWithoutLogin
                .statusCode(400)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @DisplayName("Check status code 400 and error message in request without password field")
    @Test
    public void testRequestWithoutPassword(){
        CourierClient courierClient = new CourierClient();
        ValidatableResponse dataCourierWithoutLogin = courierClient.getLoginResponse(
                new Login(existingLogin, null));
        dataCourierWithoutLogin
                .statusCode(400)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @DisplayName("Check status code 404 and error message in non-existent data")
    @Test
    public void testRequestNonExistentData(){
        CourierClient courierClient = new CourierClient();
        ValidatableResponse nonExistentData  = courierClient.getLoginResponse(
                Login.getRandomLoginData());
        nonExistentData
                .statusCode(404)
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void tearDown(){
        deleteLogin();
    }
}

package pl.codepride.dailyadvisor.functionaltest.common;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import pl.codepride.dailyadvisor.functionaltest.common.pojo.LoginRequest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

/**
 * Class reponsible for sharing http client state between test classes.
 */
@Data
public class HttpHelper {

    /**
     * Backend url root.
     */
    private String backendUrl = ConfigManager.loadStringProperty("backend.url.root");

    /**
     * Crsf cookie name.
     */
    private String csrfTokenCookieName = ConfigManager.loadStringProperty("backend.csrf.cookie.name");

    /**
     * Csrf header name.
     */
    private String csrfTokenHeaderName = ConfigManager.loadStringProperty("backend.csrf.header.name");

    /**
     * Jwt token cookie name.
     */
    private String jwtTokenCookieName = ConfigManager.loadStringProperty("backend.jwt.cookie.name");

    /**
     * Jwt token value.
     */
    private String jwtValue;

    /**
     * Csrf token value.
     */
    private String csrfValue;

    /**
     * HttpSteps request.
     */
    private RequestSpecification request;

    /**
     * HttpSteps response.
     */
    private ValidatableResponse response;

    /**
     * Populate.
     */
    public void populate() {
        given()
                .when()
                .get(backendUrl + "populate")
                .then()
                .statusCode(200);
    }

    /**
     * Log in to the backend.
     * @param username Username.
     * @param password Password.
     */
    public void logIn(String username, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("m@m.mm");
        loginRequest.setPassword("111111");
        createCsrfRequest();

        this.jwtValue = this.request
                .body(loginRequest)
                .when()
                .post(backendUrl + "login")
                .then()
                .statusCode(200)
                .and()
                .cookie(jwtTokenCookieName)
                .and()
                .extract()
                .cookie(jwtTokenCookieName);
    }

    /**
     * Create request with user data.
     */
    public void createSecureRequest() {
        createCsrfRequest();
        this.request.cookie(this.jwtTokenCookieName, this.jwtValue);
    }

    /**
     * Create request with csrf token.
     */
    public void createCsrfRequest() {
        if(this.csrfValue == null) {
            this.csrfValue = given()
                    .when()
                    .get(backendUrl + "csrf")
                    .then()
                    .statusCode(200)
                    .and()
                    .cookie(csrfTokenCookieName)
                    .and()
                    .extract()
                    .cookie(csrfTokenCookieName);
        }

        this.request = createEmptyRequest()
                .cookie(csrfTokenCookieName, this.csrfValue)
                .header(csrfTokenHeaderName, this.csrfValue);
    }

    /**
     * Create empty request with no security.
     * @return Created request.
     */
    public RequestSpecification createEmptyRequest() {
        this.request = given().baseUri(backendUrl);
        return this.request;
    }

    /**
     * Perform post request.
     * @return Response.
     */
    public ValidatableResponse performPostReqest() {
        assertNotNull(this.request);
        this.response = this.request.post().then();
        return this.response;
    }

    /**
     * Perform get reuqest.
     * @return Response.
     */
    public ValidatableResponse performGetReqest() {
        assertNotNull(this.request);
        this.response = this.request.get().then();
        return this.response;
    }
}

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
     * Frontend url root.
     */
    private String frontendUrl = ConfigManager.loadStringProperty("frontend.url.root");

    /**
     * Security root url.
     */
    private String securityRoot = ConfigManager.loadStringProperty("backend.security.root");

    /**
     * Current service root. Default none.
     */
    private String serviceRoot = "";

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
                .get(backendUrl + "api/populate")
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
                .post(backendUrl + securityRoot + "login")
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
                    .get(backendUrl + securityRoot + "csrf")
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
     * Create request with cors headers.
     */
    public void createCorsRequest() {
        this.request = createEmptyRequest();
        this.request.header("Origin", frontendUrl);
        this.request.header("Access-Control-Request-Headers", "content-type");
        this.request.header("Access-Control-Request-Method", "POST");
    }

    /**
     * Create empty request with no security.
     * @return Created request.
     */
    public RequestSpecification createEmptyRequest() {
        this.request = given().baseUri(backendUrl + serviceRoot);
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
     * Perform get request.
     * @return Response.
     */
    public ValidatableResponse performGetReqest() {
        assertNotNull(this.request);
        this.response = this.request.get().then();
        return this.response;
    }

    /**
     * Perform options request.
     * @return Response.
     */
    public ValidatableResponse performOptionsReqest() {
        assertNotNull(this.request);
        this.response = this.request.options().then();
        return this.response;
    }
}

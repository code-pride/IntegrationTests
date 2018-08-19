package pl.codepride.dailyadvisor.functionaltest.feature.http;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.Data;
import pl.codepride.dailyadvisor.functionaltest.common.HttpHelper;

@Data
public class HttpSteps {

    private HttpHelper httpHelper;

    public HttpSteps(HttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    @And("^The base path is \"([^\"]*)\"$")
    public void the_base_path_is(String targetUrl) {
        this.httpHelper.getRequest().basePath(targetUrl);
    }

    @And("^The payload is clean$")
    public void the_payload_is_clean() {
        this.httpHelper.getRequest().body("");
    }

    @When("^The post request is passed$")
    public void the_post_request_is_passed() {
        this.httpHelper.performPostReqest();
    }

    @When("^The get request is passed$")
    public void the_get_request_is_passed() {
        this.httpHelper.performGetReqest();
    }

    @Then("Response code is (\\d+)$")
    public void response_code_is(int code) {
        this.httpHelper.getResponse().statusCode(code);
    }

    @Then("^Response cooke is \"([^\"]*)\" and has value \"([^\"]*)\"$")
    public void cookie_exits_and_has_value(String cookieName, String cookieValue) {
        this.httpHelper.getResponse().cookie(cookieName, cookieValue);
    }

    @Then("^Response cooke is \"([^\"]*)\"$")
    public void cookie_exits(String cookieName) {
        this.httpHelper.getResponse().cookie(cookieName);
    }
}

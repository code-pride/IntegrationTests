package pl.codepride.dailyadvisor.functionaltest.feature.security;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import pl.codepride.dailyadvisor.functionaltest.common.HttpHelper;
import pl.codepride.dailyadvisor.functionaltest.common.pojo.LoginRequest;
import pl.codepride.dailyadvisor.functionaltest.feature.http.HttpSteps;

public class SecuritySteps {

    private HttpHelper httpHelper;

    public SecuritySteps(HttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    @Given("^I have csrf cookie and populate is done$")
    public void i_have_csrf_cookie_and_populate_is_done() {
        this.httpHelper.populate();
        this.httpHelper.createCsrfRequest();
    }

    @Given("^I have empty request$")
    public void i_have_empty_reuqest() {
        this.httpHelper.createEmptyRequest();
    }

    @And("^The login request payload is set$")
    public void the_login_request_payload_is_set() {
        this.httpHelper.getRequest().body(LoginRequest.builder().username("m@m.mm").password("111111").build());
    }
}

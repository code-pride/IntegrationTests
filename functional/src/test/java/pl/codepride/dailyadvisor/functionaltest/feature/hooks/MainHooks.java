package pl.codepride.dailyadvisor.functionaltest.feature.hooks;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import pl.codepride.dailyadvisor.functionaltest.common.HttpHelper;

/**
 * Main cucumber hooks class.
 */
public class MainHooks {

    /**
     * Http helper.
     */
    HttpHelper httpHelper;

    public MainHooks(HttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    @Before(value = "@LoggedAsUser", order = 1)
    public void beforeLoggedAsUser() {
        httpHelper.populate();
        httpHelper.logIn("m@m.mm", "111111");
        httpHelper.createSecureRequest();
    }

    @Before(value = "@LoggedAsCoach", order = 1)
    public void beforeLoggedAsCoach() {
        httpHelper.populate();
        httpHelper.logIn("m@m.mm", "111111");
        httpHelper.createSecureRequest();
    }

    @Before(value = "@Csrf", order = 1)
    public void beforeCsrf() {
        httpHelper.createCsrfRequest();
    }

    @Before(value = "@Unsecured", order = 1)
    public void beforeUnsecured() {
        httpHelper.createEmptyRequest();
    }

    @Before(value = "@Cors", order = 0)
    public void beforeCors() {
        httpHelper.createCorsRequest();
    }

}

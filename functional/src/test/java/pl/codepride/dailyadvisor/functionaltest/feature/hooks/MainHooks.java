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

    @Before("@LoggedAsUser")
    public void beforeLoggedAsUser() {
        httpHelper.populate();
        httpHelper.logIn("m@m.mm", "111111");
        httpHelper.createSecureRequest();
    }

    @Before("@LoggedAsCoach")
    public void beforeLoggedAsCoach() {
        httpHelper.populate();
        httpHelper.logIn("m@m.mm", "111111");
        httpHelper.createSecureRequest();
    }

    @Before("@Csrf")
    public void beforeCsrf() {
        httpHelper.createCsrfRequest();
    }

    @Before("@Unsecured")
    public void beforeUnsecured() {
        httpHelper.createEmptyRequest();
    }

}

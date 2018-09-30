package pl.codepride.dailyadvisor.functionaltest.feature.hooks;

import cucumber.api.java.Before;
import pl.codepride.dailyadvisor.functionaltest.common.HttpHelper;

/**
 * Hooks for the particular microservices.
 */
public class ServicePathHooks {

    HttpHelper httpHelper;

    public ServicePathHooks(HttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    @Before(value = "@UserService", order = 0)
    public void userService() {
        httpHelper.setServiceRoot("users");
    }

    @Before(value = "@Api", order = 0)
    public void apiService() {
        httpHelper.setServiceRoot("api");
    }
}

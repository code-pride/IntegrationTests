package pl.codepride.dailyadvisor.functionaltest.common.mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.*;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import lombok.Data;
import org.junit.Rule;
import pl.codepride.dailyadvisor.functionaltest.common.ConfigManager;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Oauth2 provider mock.
 */
@Data
public class Oauth2Mock {

    /**
     * Mock bind port.
     */
    private int port = ConfigManager.loadIntProperty("oauth2.mock.port");

    /**
     * Mock bind host.
     */
    private String host = ConfigManager.loadStringProperty("oauth2.mock.host");

    /**
     * Backend url root.
     */
    private String backendUrl = ConfigManager.loadStringProperty("backend.url.root");

    /**
     * Backend url root.
     */
    private String loginUrl = ConfigManager.loadStringProperty("oauth2.mock.login.url");

    /**
     * Wiremock configuration.
     */
    private WireMockConfiguration wireMockConfiguration;

    /**
     * WireMock junit rule.
     */
    @Rule
    public WireMockRule wireMockRule;

    /**
     * Default constructor.
     */
    public Oauth2Mock() {
        this.wireMockConfiguration = new WireMockConfiguration();
        this.wireMockConfiguration.bindAddress(host);
        this.wireMockConfiguration.port(port);
        this.wireMockConfiguration.extensions(new CaptureStateTransformer());

        this.wireMockRule = new WireMockRule(wireMockConfiguration);
    }

    /**
     * Initialize mock logic. The WireMock server has to be started.
     */
    public void initializeStub() {
        configureFor(this.host, this.port);

        stubFor(get(urlPathMatching("/oauth/authorize?.*"))
                .willReturn(aResponse()
                        .withStatus(302)
                        .withHeader("Location",  backendUrl + loginUrl + "?code=oauth_code&state=${state-key}")
                        .withTransformers("CaptureStateTransformer")));

        stubFor(post(urlEqualTo("/oauth/token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\":\"my-fun-token\"}")));

        stubFor(get(urlPathEqualTo("/userinfo"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"user_id\":\"my-id\",\"user_name\":\"bwatkins\",\"email\":\"bwatkins@test.com\"}")));
    }

    /**
     * Main function to launch mock as standalone application.
     * @param args Program arguments.
     */
    public static void main(String [] args) {

        Oauth2Mock oauth2Mock = new Oauth2Mock();

        WireMockServer wireMockServer = new WireMockServer(oauth2Mock.getWireMockConfiguration());
        wireMockServer.start();

        oauth2Mock.initializeStub();
    }

    /**
     * Custom transformer to attach given state to the response.
     */
    private class CaptureStateTransformer extends ResponseTransformer {
        private String state = null;

        @Override
        public boolean applyGlobally() {
            return false;
        }

        @Override
        public Response transform(Request request, Response responseDef, FileSource files, Parameters parameters) {
            // Capture the state parameter from the /oauth/authorize request
            if (state == null && request.queryParameter("state") != null) {
                state = request.queryParameter("state").firstValue();
            }

            // Add the state parameter to the /login redirect
            if (responseDef.getHeaders().getHeader("Location").isPresent()) {
                String redirectLocation = responseDef.getHeaders().getHeader("Location").firstValue();
                return Response.response()
                        .headers(HttpHeaders
                                .noHeaders()
                                .plus(HttpHeader
                                        .httpHeader("Location", redirectLocation.replace("${state-key}", this.state))))
                        .status(302)
                        .build();
            }

            return responseDef;
        }

        @Override
        public String getName() {
            return "CaptureStateTransformer";
        }
    }
}

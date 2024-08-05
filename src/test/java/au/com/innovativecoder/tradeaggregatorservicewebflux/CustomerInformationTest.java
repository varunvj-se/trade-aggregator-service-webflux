package au.com.innovativecoder.tradeaggregatorservicewebflux;

import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class CustomerInformationTest extends AbstractIntegrationTest {


    @Test
    void testCustomerInformation() {

        var responseBody = """
                {
                    "name": "sam"
                }
                """;

        this.mockServerClient.when(HttpRequest.request("/customers/1"))
                .respond(HttpResponse.response(responseBody)
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                );

        this.client.get()
                .uri("/customers/1")
                .exchange()
                .expectBody();
    }
}

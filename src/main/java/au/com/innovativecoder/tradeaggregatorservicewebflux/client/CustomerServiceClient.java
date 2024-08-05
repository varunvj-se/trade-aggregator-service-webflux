package au.com.innovativecoder.tradeaggregatorservicewebflux.client;

import au.com.innovativecoder.tradeaggregatorservicewebflux.dto.CustomerInformation;
import au.com.innovativecoder.tradeaggregatorservicewebflux.dto.StockTradeRequest;
import au.com.innovativecoder.tradeaggregatorservicewebflux.dto.StockTradeResponse;
import au.com.innovativecoder.tradeaggregatorservicewebflux.exceptions.ApplicationExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class CustomerServiceClient {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceClient.class);

    private final WebClient client;

    /**
     * Constructs a new CustomerServiceClient with the given WebClient.
     *
     * @param client the WebClient to use for making requests to the customer service
     */
    public CustomerServiceClient(WebClient client) {
        this.client = client;
    }

    /**
     * Retrieves customer information for the given customer ID.
     *
     * This method sends a GET request to the `/customers/{customerId}` endpoint,
     * expecting a response containing the customer information.
     *
     * @param customerId the ID of the customer
     * @return a Mono of `CustomerInformation` containing the customer information
     */
    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return this.client.get()
                .uri("/customers/{customerId}", customerId)
                .retrieve()
                .bodyToMono(CustomerInformation.class)
                // Handles the case where the customer is not found.
                // This method resumes the Mono with a custom exception when a WebClientResponseException.NotFound
                // is encountered, indicating that the customer with the given ID was not found.
                // @param ex the WebClientResponseException.NotFound exception
                // @return a Mono error with a custom exception indicating the customer was not found
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> ApplicationExceptions.customerNotFound(customerId));
    }

    /**
     * Executes a stock trade for the given customer ID and trade request.
     *
     * This method sends a POST request to the `/customers/{customerId}/trade` endpoint,
     * with the trade request as the request body, expecting a response containing the trade result.
     *
     * @param customerId the ID of the customer
     * @param stockTradeRequest the trade request details
     * @return a Mono of `StockTradeResponse` containing the trade result
     */
    public Mono<StockTradeResponse> trade(Integer customerId, StockTradeRequest stockTradeRequest) {
        return this.client.post()
                .uri("/customers/{customerId}/trade", customerId)
                .bodyValue(stockTradeRequest)
                .retrieve()
                .bodyToMono(StockTradeResponse.class)
                // Handles the case where the customer is not found.
                // This method resumes the Mono with a custom exception when a WebClientResponseException.NotFound
                // is encountered, indicating that the customer with the given ID was not found.
                // @param ex the WebClientResponseException.NotFound exception
                // @return a Mono error with a custom exception indicating the customer was not found
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> ApplicationExceptions.customerNotFound(customerId))
                // Handles exceptions for bad requests.
                // This method resumes the Mono with a custom exception when a WebClientResponseException.BadRequest
                // is encountered, indicating that the request was invalid.
                // @param exception the WebClientResponseException.BadRequest exception
                // @return a Mono error with a custom exception indicating the request was invalid
                .onErrorResume(WebClientResponseException.BadRequest.class, this::handleException);
    }

    /**
     * Handles exceptions for bad requests.
     *
     * This method processes the `WebClientResponseException.BadRequest` exception,
     * extracting the problem detail and logging it, then returning an appropriate error response.
     *
     * @param exception the bad request exception
     * @param <T> the type of the response
     * @return a Mono containing the error response
     */
    private <T> Mono<T> handleException(WebClientResponseException.BadRequest exception) {
        var pd = exception.getResponseBodyAs(ProblemDetail.class);
        var message = Objects.nonNull(pd) ? pd.getDetail() : exception.getMessage();
        log.error("customer service problem detail : {}", pd);
        return ApplicationExceptions.invalidTradeRequest(message);
    }
}

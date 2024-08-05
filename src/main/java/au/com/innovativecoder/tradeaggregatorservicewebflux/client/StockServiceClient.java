package au.com.innovativecoder.tradeaggregatorservicewebflux.client;

import au.com.innovativecoder.tradeaggregatorservicewebflux.domain.Ticker;
import au.com.innovativecoder.tradeaggregatorservicewebflux.dto.PriceUpdate;
import au.com.innovativecoder.tradeaggregatorservicewebflux.dto.StockPriceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;

public class StockServiceClient {

    private static final Logger log = LoggerFactory.getLogger(StockServiceClient.class);

    private final WebClient client;
    private Flux<PriceUpdate> flux;

    /**
     * Constructs a new StockServiceClient with the given WebClient.
     *
     * @param client the WebClient to use for making requests to the stock service
     */
    public StockServiceClient(WebClient client) {
        this.client = client;
    }

    /**
     * Retrieves the current stock price for the given ticker.
     *
     * This method sends a GET request to the `/stock/{ticker}` endpoint,
     * expecting a response containing the stock price information.
     *
     * @param ticker the ticker symbol of the stock
     * @return a Mono of `StockPriceResponse` containing the stock price information
     */
    public Mono<StockPriceResponse> getStockPrice(Ticker ticker) {
        return this.client.get()
                .uri("/stock/{ticker}", ticker)
                .retrieve()
                .bodyToMono(StockPriceResponse.class);
    }

    /**
     * Retrieves a stream of price updates from the stock service.
     *
     * This method checks if the flux is null and initializes it by calling
     * `getPriceUpdates()` if necessary. It returns the cached flux of price updates.
     *
     * @return a Flux of `PriceUpdate` objects representing the price updates
     */
    public Flux<PriceUpdate> priceUpdatesStream() {
        if(Objects.isNull(this.flux)) {
            this.flux = this.getPriceUpdates();
        }
        return this.flux;
    }

    /**
     * Retrieves a stream of price updates from the stock service.
     *
     * This method sends a GET request to the `/stock/price-stream` endpoint,
     * expecting a stream of price updates in NDJSON format. The response is
     * converted to a Flux of `PriceUpdate` objects, retried on failure, and cached with a size of 1.
     * It is cached to make it a hot publisher so that it does not need to have multiple instantiations of stock service
     * Retry is implemented because if the stock service is restarted , then it should not throw errors from this service.
     *
     * @return a Flux of `PriceUpdate` objects representing the price updates
     */
    private Flux<PriceUpdate> getPriceUpdates() {
        return this.client.get()
                .uri("/stock/price-stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(PriceUpdate.class)
                .retryWhen(retry())
                .cache(1);
    }

    /**
     * Configures the retry mechanism for the price updates stream.
     *
     * This method sets up a fixed delay retry strategy with a maximum of 100 retries,
     * each delayed by 1 second. It logs an error message before each retry attempt.
     *
     * @return a Retry object configured with the retry strategy
     */
    private Retry retry() {
        return Retry.fixedDelay(100, Duration.ofSeconds(1))
                .doBeforeRetry(rs -> log.error("stock service price stream failed. retrying {}", rs.failure().getMessage()));
    }
}

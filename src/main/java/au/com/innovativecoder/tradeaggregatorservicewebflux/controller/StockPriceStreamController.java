package au.com.innovativecoder.tradeaggregatorservicewebflux.controller;

import au.com.innovativecoder.tradeaggregatorservicewebflux.client.StockServiceClient;
import au.com.innovativecoder.tradeaggregatorservicewebflux.dto.PriceUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/stock")
public class StockPriceStreamController {

    private static final Logger log = LoggerFactory.getLogger(StockPriceStreamController.class);

    private final StockServiceClient stockServiceClient;

    public StockPriceStreamController(StockServiceClient stockServiceClient) {
        this.stockServiceClient = stockServiceClient;
    }

    @GetMapping(value = "/price-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PriceUpdate> priceUpdateStream() {
        return this.stockServiceClient.priceUpdatesStream();
    }
}

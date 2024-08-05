package au.com.innovativecoder.tradeaggregatorservicewebflux.controller;

import au.com.innovativecoder.tradeaggregatorservicewebflux.dto.CustomerInformation;
import au.com.innovativecoder.tradeaggregatorservicewebflux.dto.StockTradeRequest;
import au.com.innovativecoder.tradeaggregatorservicewebflux.dto.StockTradeResponse;
import au.com.innovativecoder.tradeaggregatorservicewebflux.dto.TradeRequest;
import au.com.innovativecoder.tradeaggregatorservicewebflux.service.CustomerPortfolioService;
import au.com.innovativecoder.tradeaggregatorservicewebflux.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
public class CustomerPortfolioController {

    private static final Logger log = LoggerFactory.getLogger(CustomerPortfolioController.class);

    private final CustomerPortfolioService customerPortfolioService;

    public CustomerPortfolioController(CustomerPortfolioService customerPortfolioService) {
        this.customerPortfolioService = customerPortfolioService;
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(@PathVariable("customerId") Integer customerId) {
        return customerPortfolioService.getCustomerInformation(customerId);
    }

    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> trade(@PathVariable("customerId") Integer customerId, @RequestBody Mono<TradeRequest> tradeRequestMono) {
        return tradeRequestMono.transform(RequestValidator.validate())
                .flatMap(req -> this.customerPortfolioService.trade(customerId, req));
    }
}

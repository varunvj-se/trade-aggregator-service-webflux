package au.com.innovativecoder.tradeaggregatorservicewebflux.service;

import au.com.innovativecoder.tradeaggregatorservicewebflux.client.CustomerServiceClient;
import au.com.innovativecoder.tradeaggregatorservicewebflux.client.StockServiceClient;
import au.com.innovativecoder.tradeaggregatorservicewebflux.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerPortfolioService {

    private static final Logger log = LoggerFactory.getLogger(CustomerPortfolioService.class);

    private final StockServiceClient stockServiceClient;
    private final CustomerServiceClient customerServiceClient;

    /**
     * Constructs a new CustomerPortfolioService with the given StockServiceClient and CustomerServiceClient.
     *
     * @param stockServiceClient the client to use for stock service requests
     * @param customerServiceClient the client to use for customer service requests
     */
    public CustomerPortfolioService(StockServiceClient stockServiceClient, CustomerServiceClient customerServiceClient) {
        this.stockServiceClient = stockServiceClient;
        this.customerServiceClient = customerServiceClient;
    }

    /**
     * Retrieves customer information for the given customer ID.
     *
     * This method delegates the request to the CustomerServiceClient.
     *
     * @param customerId the ID of the customer
     * @return a Mono of `CustomerInformation` containing the customer information
     */
    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return this.customerServiceClient.getCustomerInformation(customerId);
    }

    /**
     * Executes a stock trade for the given customer ID and trade request.
     *
     * This method retrieves the current stock price for the given ticker, constructs a StockTradeRequest,
     * and delegates the trade execution to the CustomerServiceClient.
     *
     * @param customerId the ID of the customer
     * @param tradeRequest the trade request details
     * @return a Mono of `StockTradeResponse` containing the trade result
     */
    public Mono<StockTradeResponse> trade(Integer customerId, TradeRequest tradeRequest) {
        //First, the method retrieves the current stock price for the ticker symbol specified in the tradeRequest by calling the getStockPrice method of the StockServiceClient:
        return this.stockServiceClient.getStockPrice(tradeRequest.ticker())
                // The result of this call is a Mono<StockPriceResponse>, which is a reactive type representing a single asynchronous value.
                // The method then extracts the price from the StockPriceResponse using the map operator:
                .map(StockPriceResponse::price)
                // Next, the method converts the TradeRequest to a StockTradeRequest by calling the toStockTradeRequest method, passing the extracted price as an argument:
                .map(price -> this.toStockTradeRequest(tradeRequest, price))
                // Finally, the method delegates the trade execution to the CustomerServiceClient by calling its trade method, passing the customer ID and the constructed StockTradeRequest.
                // This is done using the flatMap operator, which allows for asynchronous composition of the resulting Mono:
                .flatMap(req -> this.customerServiceClient.trade(customerId, req));
    }

    /**
     * Converts a TradeRequest to a StockTradeRequest with the given price.
     *
     * @param tradeRequest the trade request details
     * @param price the current stock price
     * @return a StockTradeRequest containing the trade details and price
     */
    private StockTradeRequest toStockTradeRequest(TradeRequest tradeRequest, Integer price) {
        return new StockTradeRequest(tradeRequest.ticker(), price, tradeRequest.quantity(), tradeRequest.action());
    }
}

package au.com.innovativecoder.tradeaggregatorservicewebflux.dto;


import au.com.innovativecoder.tradeaggregatorservicewebflux.domain.Ticker;
import au.com.innovativecoder.tradeaggregatorservicewebflux.domain.TradeAction;

public record StockTradeResponse(Integer customer, Ticker ticker, Integer price, Integer quantity, TradeAction action, Integer totalPrice, Integer balance) {
}

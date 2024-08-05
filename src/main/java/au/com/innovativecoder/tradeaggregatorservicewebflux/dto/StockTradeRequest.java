package au.com.innovativecoder.tradeaggregatorservicewebflux.dto;


import au.com.innovativecoder.tradeaggregatorservicewebflux.domain.Ticker;
import au.com.innovativecoder.tradeaggregatorservicewebflux.domain.TradeAction;

public record StockTradeRequest(Ticker ticker, Integer price, Integer quantity, TradeAction action) {
}

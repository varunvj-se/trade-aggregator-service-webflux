package au.com.innovativecoder.tradeaggregatorservicewebflux.dto;

import au.com.innovativecoder.tradeaggregatorservicewebflux.domain.Ticker;
import au.com.innovativecoder.tradeaggregatorservicewebflux.domain.TradeAction;

public record TradeRequest(Ticker ticker, TradeAction action, Integer quantity) {
}

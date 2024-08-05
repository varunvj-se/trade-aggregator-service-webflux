package au.com.innovativecoder.tradeaggregatorservicewebflux.dto;

import au.com.innovativecoder.tradeaggregatorservicewebflux.domain.Ticker;

public record Holding(Ticker ticker, Integer quantity) {
}

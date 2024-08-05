package au.com.innovativecoder.tradeaggregatorservicewebflux.dto;

import au.com.innovativecoder.tradeaggregatorservicewebflux.domain.Ticker;


public record StockPriceResponse(Ticker ticker, Integer price) {
}

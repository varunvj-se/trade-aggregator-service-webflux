package au.com.innovativecoder.tradeaggregatorservicewebflux.dto;

import au.com.innovativecoder.tradeaggregatorservicewebflux.domain.Ticker;

import java.time.LocalDateTime;

public record PriceUpdate(Ticker ticker, Integer price, LocalDateTime time) {
}

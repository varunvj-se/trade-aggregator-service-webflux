package au.com.innovativecoder.tradeaggregatorservicewebflux.exceptions;

public class InvalidTradeRequestException extends  RuntimeException {

    public InvalidTradeRequestException(String message) {
        super(message);
    }
}

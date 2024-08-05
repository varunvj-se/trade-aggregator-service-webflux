package au.com.innovativecoder.tradeaggregatorservicewebflux.config;

import au.com.innovativecoder.tradeaggregatorservicewebflux.client.CustomerServiceClient;
import au.com.innovativecoder.tradeaggregatorservicewebflux.client.StockServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ServiceClientsConfig {

    private static final Logger log = LoggerFactory.getLogger(ServiceClientsConfig.class);

    @Bean
    public CustomerServiceClient customerServiceClient(@Value("${customer.service.url}") String baseUrl) {
        return new CustomerServiceClient(this.createWebClient("Customer Service", baseUrl));
    }

    @Bean
    public StockServiceClient stockServiceClient(@Value("${stock.service.url}") String baseUrl) {
        return new StockServiceClient(this.createWebClient("Stock Service",baseUrl));
    }

    private WebClient createWebClient(String type, String baseUrl) {
        log.info("{} baseUrl : {}", type, baseUrl);
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


}

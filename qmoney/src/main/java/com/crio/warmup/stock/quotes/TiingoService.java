
package com.crio.warmup.stock.quotes;

import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TiingoService implements StockQuotesService {

  private RestTemplate restTemplate;
  private String APIKEY = "6c21aa3c03472563ee2d32f510b246153166db27";

  protected TiingoService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }


  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Implement getStockQuote method below that was also declared in the interface.

  // Note:
  // 1. You can move the code from PortfolioManagerImpl#getStockQuote inside newly created method.
  // 2. Run the tests using command below and make sure it passes.
  //    ./gradlew test --tests TiingoServiceTest

  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to) throws JsonProcessingException {

    String generateURL = buildUri(symbol, from, to);
    // Make a GET request and retrieve the response as a ResponseEntity
    String responseEntity = restTemplate.getForObject(generateURL, String.class);
    // Extract the response body from the ResponseEntity
    // String responseBody = responseEntity.getBody();
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    return Arrays.asList(mapper.readValue(responseEntity, TiingoCandle[].class));
  }

  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Write a method to create appropriate url to call the Tiingo API.

    protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
    String uriTemplate = String.format("https://api.tiingo.com/tiingo/daily/%s/prices?"
        + "startDate=%s&endDate=%s&token=%s", symbol, startDate, endDate, APIKEY);
    return uriTemplate;
  }

}

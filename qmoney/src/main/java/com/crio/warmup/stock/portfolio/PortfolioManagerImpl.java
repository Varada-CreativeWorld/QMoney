
package com.crio.warmup.stock.portfolio;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.crio.warmup.stock.quotes.StockQuotesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {



  private String APIKEY = "6c21aa3c03472563ee2d32f510b246153166db27";
  private RestTemplate restTemplate;
  private StockQuotesService stockQuotesService;

  // Caution: Do not delete or modify the constructor, or else your build will break!
  // This is absolutely necessary for backward compatibility
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }


  //TODO: CRIO_TASK_MODULE_REFACTOR
  // 1. Now we want to convert our code into a module, so we will not call it from main anymore.
  //    Copy your code from Module#3 PortfolioManagerApplication#calculateAnnualizedReturn
  //    into #calculateAnnualizedReturn function here and ensure it follows the method signature.
  // 2. Logic to read Json file and convert them into Objects will not be required further as our
  //    clients will take care of it, going forward.

  // Note:
  // Make sure to exercise the tests inside PortfolioManagerTest using command below:
  // ./gradlew test --tests PortfolioManagerTest

  //CHECKSTYLE:OFF

  public PortfolioManagerImpl(StockQuotesService stockQuotesService) {
    this.stockQuotesService = stockQuotesService;
  }


  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades, LocalDate endDate){
    List<AnnualizedReturn> results = new ArrayList<>();
    for (PortfolioTrade trade : portfolioTrades) {

      try{

        List<Candle> listOfCandles = getStockQuote(trade.getSymbol(), trade.getPurchaseDate(), endDate);
        
        // Extract a specific key-value pai
        Double close = listOfCandles.get(listOfCandles.size()-1).getClose();
        Double open = listOfCandles.get(0).getOpen();
        Double totalReturn = (double)(close - open) / open;
        Double yearDifference = calculateYearDifference(trade.getPurchaseDate(),endDate);
        Double annualizedReturns = Math.pow((1+totalReturn), ((double)1/yearDifference)) - 1;
        AnnualizedReturn tempObj = new AnnualizedReturn(trade.getSymbol(), annualizedReturns, totalReturn);
        results.add(tempObj);

      } catch(Exception e){
        // throw new RuntimeException("This is a runtime exception!");
        System.out.println(e);
      }
    }
    // sortByAnnualReturn(results);
    Collections.sort(results, getComparator());
    return results;
  }

  private double calculateYearDifference(LocalDate date1, LocalDate date2) {
    // Calculate the difference in days using ChronoUnit.DAYS.between
    long daysDifference = ChronoUnit.DAYS.between(date1, date2);

    // Convert days to years (considering an average of 365.25 days per year)
    double yearsDifference = daysDifference / 365.25;

    return Math.abs(yearsDifference); // Take the absolute value to get a positive result
}






  private Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }

  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Extract the logic to call Tiingo third-party APIs to a separate function.
  //  Remember to fill out the buildUri function and use that.


  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException {

        return stockQuotesService.getStockQuote(symbol, from, to);
  }

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
       String uriTemplate = String.format("https://api.tiingo.com/tiingo/daily/%s/prices?"
            + "startDate=%s&endDate=%s&token=%s", symbol, startDate, endDate, APIKEY);
       return uriTemplate;
  }



}

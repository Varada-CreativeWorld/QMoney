
package com.crio.warmup.stock;


import com.crio.warmup.stock.dto.*;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import com.crio.warmup.stock.portfolio.PortfolioManager;
import com.crio.warmup.stock.portfolio.PortfolioManagerFactory;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;
import java.util.*;


// GithubPojo gitPojo = new RestTemplate().getForObject("https://api.github.com/users/crio-do", GithubPojo.class);

public class PortfolioManagerApplication {

  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Task:
  //       - Read the json file provided in the argument[0], The file is available in the classpath.
  //       - Go through all of the trades in the given file,
  //       - Prepare the list of all symbols a portfolio has.
  //       - if "trades.json" has trades like
  //         [{ "symbol": "MSFT"}, { "symbol": "AAPL"}, { "symbol": "GOOGL"}]
  //         Then you should return ["MSFT", "AAPL", "GOOGL"]
  //  Hints:
  //    1. Go through two functions provided - #resolveFileFromResources() and #getObjectMapper
  //       Check if they are of any help to you.
  //    2. Return the list of all symbols in the same order as provided in json.

  //  Note:
  //  1. There can be few unused imports, you will need to fix them to make the build pass.
  //  2. You can use "./gradlew build" to check if your code builds successfully.

  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {

    ObjectMapper objectMapper = getObjectMapper();
    List<String> results = new ArrayList<>();
    File filename = resolveFileFromResources(args[0]);
    Trade[] trades = objectMapper.readValue(filename, Trade[].class);
    System.out.println("Deserialized(read) successfully");
    for (Trade trade : trades) {
      results.add(trade.symbol);
    }
    return results;
  }


  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.



  // TODO: CRIO_TASK_MODULE_REST_API
  //  Find out the closing price of each stock on the end_date and return the list
  //  of all symbols in ascending order by its close value on end date.

  // Note:
  // 1. You may have to register on Tiingo to get the api_token.
  // 2. Look at args parameter and the module instructions carefully.
  // 2. You can copy relevant code from #mainReadFile to parse the Json.
  // 3. Use RestTemplate#getForObject in order to call the API,
  //    and deserialize the results in List<Candle>



  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    return Paths.get(
        Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return objectMapper;
  }


  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Follow the instructions provided in the task documentation and fill up the correct values for
  //  the variables provided. First value is provided for your reference.
  //  A. Put a breakpoint on the first line inside mainReadFile() which says
  //    return Collections.emptyList();
  //  B. Then Debug the test #mainReadFile provided in PortfoliomanagerApplicationTest.java
  //  following the instructions to run the test.
  //  Once you are able to run the test, perform following tasks and record the output as a
  //  String in the function below.
  //  Use this link to see how to evaluate expressions -
  //  https://code.visualstudio.com/docs/editor/debugging#_data-inspection
  //  1. evaluate the value of "args[0]" and set the value
  //     to the variable named valueOfArgument0 (This is implemented for your reference.)
  //  2. In the same window, evaluate the value of expression below and set it
  //  to resultOfResolveFilePathArgs0
  //     expression ==> resolveFileFromResources(args[0])
  //  3. In the same window, evaluate the value of expression below and set it
  //  to toStringOfObjectMapper.
  //  You might see some garbage numbers in the output. Dont worry, its expected.
  //    expression ==> getObjectMapper().toString()
  //  4. Now Go to the debug window and open stack trace. Put the name of the function you see at
  //  second place from top to variable functionNameFromTestFileInStackTrace
  //  5. In the same window, you will see the line number of the function in the stack trace window.
  //  assign the same to lineNumberFromTestFileInStackTrace
  //  Once you are done with above, just run the corresponding test and
  //  make sure its working as expected. use below command to do the same.
  //  ./gradlew test --tests PortfolioManagerApplicationTest.testDebugValues

  public static List<String> debugOutputs() {

     String valueOfArgument0 = "trades.json";
     String resultOfResolveFilePathArgs0 = "trades.json";
     String toStringOfObjectMapper = "ObjectMapper";
     String functionNameFromTestFileInStackTrace = "mainReadFile";
     String lineNumberFromTestFileInStackTrace = "";


    return Arrays.asList(new String[]{valueOfArgument0, resultOfResolveFilePathArgs0,
        toStringOfObjectMapper, functionNameFromTestFileInStackTrace,
        lineNumberFromTestFileInStackTrace});
  }


  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.
  private static Map<Double, String> sortByKey(Map<Double, String> unsortedMap) {
    // Create a TreeMap to automatically sort the entries by keys
    Map<Double, String> sortedMap = new TreeMap<>(unsortedMap);

    return sortedMap;
  }

  public static List<String> mainReadQuotes(String[] args) throws RuntimeException, IOException, URISyntaxException {
    
    List<PortfolioTrade> results = readTradesFromJson(args[0]);
    List<String> finalOutput = new ArrayList<>();
    LocalDate localDate = LocalDate.parse(args[1]);
    Map<Double, String> unsortedMap = new HashMap<>();

    for(PortfolioTrade trade: results){
      String generateURL = prepareUrl(trade, localDate, getToken());
      URL url = new URL(generateURL);
          // Send Get request and fetch data
      try{

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String output;
        StringBuilder jsonResponse = new StringBuilder();
        while ((output = br.readLine()) != null) {
          jsonResponse.append(output);
        }
        // Parse the JSON string
        ObjectMapper objectMapperTest = new ObjectMapper();
        // System.out.println(jsonResponse.toString());
        JsonNode jsonNode = objectMapperTest.readTree(jsonResponse.toString());
        // Extract a specific key-value pai
        Double close = jsonNode.get(0).get("close").asDouble();
        unsortedMap.put(close, trade.getSymbol());
        conn.disconnect();

      } catch(Exception e){
        throw new RuntimeException("This is a runtime exception!");
      }
      
    }
    Map<Double, String> sortedMap = sortByKey(unsortedMap);
    for (String value : sortedMap.values()) {
      finalOutput.add(value);
    }
    return finalOutput;
  }

  // TODO:
  //  After refactor, make sure that the tests pass by using these two commands
  //  ./gradlew test --tests PortfolioManagerApplicationTest.readTradesFromJson
  //  ./gradlew test --tests PortfolioManagerApplicationTest.mainReadFile
  public static List<PortfolioTrade> readTradesFromJson(String filename) throws IOException, URISyntaxException {
    List<PortfolioTrade> results = new ArrayList<>();
    File file = resolveFileFromResources(filename);
    ObjectMapper objectMapper = getObjectMapper();
    PortfolioTrade[] trades = objectMapper.readValue(file, PortfolioTrade[].class);
    System.out.println("Deserialized(read) successfully");
    for (PortfolioTrade trade : trades) {
      results.add(trade);
    }
    return results;
  }


  // TODO:
  //  Build the Url using given parameters and use this function in your code to cann the API.
  public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {  
    String generateURL = String.format("https://api.tiingo.com/tiingo/daily/%s/prices?startDate=%s&endDate=%s&token=%s",trade.getSymbol(), trade.getPurchaseDate(), endDate, token);
    return generateURL;
  }

  // TODO:
  //  Ensure all tests are passing using below command
  //  ./gradlew test --tests ModuleThreeRefactorTest
  static Double getOpeningPriceOnStartDate(List<Candle> candles) {
    for(Candle candle: candles){
      return (Double) candle.getOpen();
    }
    return null;
  }


  public static Double getClosingPriceOnEndDate(List<Candle> candles) {
    Collections.reverse(candles);
    for(Candle candle: candles){
      return (Double) candle.getClose();
    }
    return null;
  }


  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
    String generateURL = prepareUrl(trade, endDate, token);
    List<Candle> results = new ArrayList<>();
    ObjectMapper objectMapper = getObjectMapper();
    try {
      URL url = new URL(generateURL);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
      String output;
      StringBuilder jsonResponse = new StringBuilder();
      while ((output = br.readLine()) != null) {
        jsonResponse.append(output);
      }
      return Arrays.asList(objectMapper.readValue(jsonResponse.toString(), TiingoCandle[].class));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return results;
  }

  public static String getToken() {
    return "6c21aa3c03472563ee2d32f510b246153166db27";
 }

  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args) throws IOException, URISyntaxException {
    List<PortfolioTrade> trades = readTradesFromJson(args[0]);
    List<AnnualizedReturn> results = new ArrayList<>();
    LocalDate localDate = LocalDate.parse(args[1]);
    for (PortfolioTrade trade : trades) {
      String generateURL = prepareUrl(trade, localDate, getToken());
      URL url = new URL(generateURL);
          // Send Get request and fetch data
      try{

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String output;
        StringBuilder jsonResponse = new StringBuilder();
        while ((output = br.readLine()) != null) {
          jsonResponse.append(output);
        }
        // Parse the JSON string
        ObjectMapper objectMapperTest = new ObjectMapper();
        // System.out.println(jsonResponse.toString());
        JsonNode jsonNode = objectMapperTest.readTree(jsonResponse.toString());
        // Extract a specific key-value pai
        Double close = jsonNode.get(jsonNode.size()-1).get("close").asDouble();
        Double open = jsonNode.get(0).get("open").asDouble();
        Double totalReturn = (double)(close - open) / open;
        Double yearDifference = calculateYearDifference(trade.getPurchaseDate(),localDate);
        Double annualizedReturns = Math.pow((1+totalReturn), ((double)1/yearDifference)) - 1;
        AnnualizedReturn tempObj = new AnnualizedReturn(trade.getSymbol(), annualizedReturns, totalReturn);
        results.add(tempObj);
        conn.disconnect();

      } catch(Exception e){
        throw new RuntimeException("This is a runtime exception!");
      }
    }
    sortByAnnualReturn(results);
    return results;
  }

  private static void sortByAnnualReturn(List<AnnualizedReturn> annualList) {
    // Using Comparator.comparingInt for comparing by age
    Collections.sort(annualList, Comparator.comparingDouble(AnnualizedReturn::getAnnualizedReturn).reversed());
  }

  public static double calculateYearDifference(LocalDate date1, LocalDate date2) {
        // Calculate the difference in days using ChronoUnit.DAYS.between
        long daysDifference = ChronoUnit.DAYS.between(date1, date2);

        // Convert days to years (considering an average of 365.25 days per year)
        double yearsDifference = daysDifference / 365.25;

        return Math.abs(yearsDifference); // Take the absolute value to get a positive result
  }

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Return the populated list of AnnualizedReturn for all stocks.
  //  Annualized returns should be calculated in two steps:
  //   1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
  //      1.1 Store the same as totalReturns
  //   2. Calculate extrapolated annualized returns by scaling the same in years span.
  //      The formula is:
  //      annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //      2.1 Store the same as annualized_returns
  //  Test the same using below specified command. The build should be successful.
  //     ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate, PortfolioTrade trade, Double buyPrice, Double sellPrice){
    Double totalReturn = (double)(sellPrice - buyPrice) / buyPrice;
    Double yearDifference = calculateYearDifference(trade.getPurchaseDate(),endDate);
    Double annualizedReturns = Math.pow((1+totalReturn), ((double)1/yearDifference)) - 1;
    return new AnnualizedReturn(trade.getSymbol(), annualizedReturns, totalReturn);

  }



  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Once you are done with the implementation inside PortfolioManagerImpl and
  //  PortfolioManagerFactory, create PortfolioManager using PortfolioManagerFactory.
  //  Refer to the code from previous modules to get the List<PortfolioTrades> and endDate, and
  //  call the newly implemented method in PortfolioManager to calculate the annualized returns.

  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.

  public static List<AnnualizedReturn> mainCalculateReturnsAfterRefactor(String[] args)
      throws Exception {
       String file = args[0];
       LocalDate endDate = LocalDate.parse(args[1]);
       String contents = readFileAsString(file);
       ObjectMapper objectMapper = getObjectMapper();
       return portfolioManager.calculateAnnualizedReturn(Arrays.asList(portfolioTrades), endDate);
  }


  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());
    printJsonObject(mainCalculateReturnsAfterRefactor(args));
  }


}


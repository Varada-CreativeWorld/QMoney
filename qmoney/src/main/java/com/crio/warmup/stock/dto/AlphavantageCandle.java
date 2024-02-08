
package com.crio.warmup.stock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlphavantageCandle implements Candle {

  @JsonProperty(value = "Time Series (Daily)")
  private Map<LocalDate, AlphavantageCandle> candles;

  @JsonProperty("1. open")
  private Double open;
  
  @JsonProperty("2. high")
  private Double high;

  @JsonProperty("3. low")
  private Double low;

  @JsonProperty("4. close")
  private Double close;
  
  private LocalDate date;

  // public Map<LocalDate, AlphavantageCandle> getCandles() {
  //   return candles;
  // }

  // public void setCandles(Map<LocalDate, AlphavantageCandle> candles) {
  //   this.candles = candles;
  // }

  @Override
  public Double getOpen() {
    return open;
  }

  public void setOpen(Double open) {
    this.open = open;
  }

  @Override
  public Double getClose() {
    return close;
  }

  public void setClose(Double close) {
    this.close = close;
  }

  @Override
  public Double getHigh() {
    return high;
  }

  public void setHigh(Double high) {
    this.high = high;
  }

  @Override
  public Double getLow() {
    return low;
  }

  public void setLow(Double low) {
    this.low = low;
  }

  @Override
  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate timeStamp) {
    this.date = timeStamp;
  }

  @Override
  public String toString() {
    return "AlphavantageCandle{"
            + "open=" + open
            + ", close=" + close
            + ", high=" + high
            + ", low=" + low
            + ", date=" + date
            + '}';
  }
}
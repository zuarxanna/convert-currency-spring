package com.kursconvertion;

import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiController {

  @Autowired
  private RestTemplate restTemplate;

  private static final String url =
      "https://api.kurs.web.id/api/v1?bank=bca&token=li1Q14HuYsII508BqLMKkugPzY1Zw3m62dSau6R0&matauang=usd";

  @GetMapping("/convert")
  public ResponseEntity<?> converter(
      @RequestParam(
          value = "currency",
          required = true,
          defaultValue = "usd"
      ) String currency,
      @RequestParam(
          value = "type",
          required = true,
          defaultValue = "sell"
      ) String type,
      @RequestParam(
          value = "amount",
          required = true,
          defaultValue = "1"
      ) int amount
  ) {
    if(Objects.equals(type, "sell")) {
      Object exchangeRate = restTemplate.getForObject(url, Object.class);
      int sell = (int) ((LinkedHashMap<?, ?>) exchangeRate).get("jual");
      Map<Object, Object> responseData = new HashMap<>();
      responseData.put("result", sell * amount);
      responseData.put("amount", amount);
      responseData.put("type", type);
      responseData.put("exchange", "usd-to-idr, rate : Rp." + sell);
      responseData.put("date", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());

      return ResponseHandler.generateResponse(
       "Successfully convert currency!",
        HttpStatus.OK,
        responseData
       );
    } else if (Objects.equals(type, "buy")) {
      Object exchangeRate = restTemplate.getForObject(url, Object.class);
      int buy = (int) ((LinkedHashMap<?, ?>) exchangeRate).get("beli");
      Map<Object, Object> responseData = new HashMap<>();
      responseData.put("result", buy * amount);
      responseData.put("amount", amount);
      responseData.put("type", type);
      responseData.put("exchange", "usd-to-idr, rate : Rp." + buy);
      responseData.put("date", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());

      return ResponseHandler.generateResponse(
        "Successfully convert currency!",
        HttpStatus.OK,
        responseData
      );
    } else {
      return ResponseHandler.generateResponse(
          "type param value must be sell or buy!",
          HttpStatus.BAD_REQUEST,
          null
      );
    }
  }
}

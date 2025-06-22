package org.kungnection;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class Judge0Service {
  private final WebClient webClient;

  public Judge0Service(@Value("${X-RapidAPI-Key}") String apiKey) {
    // if (apiKey == null || apiKey.isEmpty()) {
    //   throw new IllegalStateException("X_RAPIDAPI_KEY environment variable is not set");
    // }
    System.out.println("apikey: "+apiKey);
    this.webClient = WebClient.builder()
        .baseUrl("https://judge0-ce.p.rapidapi.com")
        .defaultHeader("x-rapidapi-key", apiKey)
        .defaultHeader("x-rapidapi-host", "judge0-ce.p.rapidapi.com")
        .build();
  }

  public String submitCode(String code, int languageId) {
    Map<String, Object> payload = Map.of(
        "source_code", code,
        "language_id", languageId);
    return webClient.post()
        .uri("/submissions?base64_encoded=false&wait=true")
        .bodyValue(payload)
        .retrieve()
        .bodyToMono(String.class)
        .block();
  }
}
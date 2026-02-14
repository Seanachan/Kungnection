package org.kungnection;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service for executing code submissions via the Judge0 API.
 */
@Service
public class Judge0Service {
    private static final Logger log = LoggerFactory.getLogger(Judge0Service.class);
    private final WebClient webClient;

    public Judge0Service(@Value("${X-RapidAPI-Key}") String apiKey) {
        log.info("Initializing Judge0Service");
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
package org.kungnection;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/judge0")
public class Judge0Controller {

  private final Judge0Service judge0Service;

  public Judge0Controller(Judge0Service judge0Service) {
    this.judge0Service = judge0Service;
  }

  @PostMapping("/submit")
  public ResponseEntity<String> submitCode(@RequestBody CodeSubmissionRequest request) {
    try {
      String result = judge0Service.submitCode(request.getSourceCode(), request.getLanguageId());
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
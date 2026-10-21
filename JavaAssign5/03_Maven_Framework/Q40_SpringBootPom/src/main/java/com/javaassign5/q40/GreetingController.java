package com.javaassign5.q40;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Q40: a small REST controller - everything is configured with annotations.
 *
 *   GET  http://localhost:8080/hello?name=Shrey
 *   POST http://localhost:8080/students   body: {"name":"Shrey","year":1}
 */
@RestController
public class GreetingController {

    /** Request body, validated with Bean Validation annotations. */
    public record StudentRequest(
            @NotBlank @Size(min = 2, max = 40) String name,
            @Min(1) @Max(5) int year) { }

    @GetMapping("/hello")
    public Map<String, String> hello(@RequestParam(defaultValue = "World") String name) {
        return Map.of("message", "Hello, " + name + "!");
    }

    @PostMapping("/students")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody StudentRequest req) {
        // @Valid -> Spring returns HTTP 400 automatically if a constraint fails
        return ResponseEntity.ok(Map.of("registered", req.name(), "year", req.year()));
    }
}

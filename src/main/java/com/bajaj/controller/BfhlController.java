package com.bajaj.controller;

import com.bajaj.dto.BfhlRequest;
import com.bajaj.dto.BfhlResponse;
import com.bajaj.service.BfhlService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller exposing the /bfhl endpoint.
 */
@RestController
@RequestMapping("/bfhl")
public class BfhlController {

    private static final Logger log = LoggerFactory.getLogger(BfhlController.class);

    private final BfhlService bfhlService;

    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    /**
     * GET /bfhl/health
     * Simple liveness check — useful for hosting platforms (Render, Railway)
     * that ping a health URL before routing traffic.
     *
     * @return 200 OK with {"status":"UP","service":"bajaj-finserv-api"}
     */
    @GetMapping("/health")
    public ResponseEntity<java.util.Map<String, String>> health() {
        log.info("GET /bfhl/health → UP");
        java.util.Map<String, String> body = new java.util.LinkedHashMap<>();
        body.put("status", "UP");
        body.put("service", "bajaj-finserv-api");
        return ResponseEntity.ok(body);
    }

    /**
     * POST /bfhl
     * Accepts a JSON body with a "data" array and returns processed results.
     *
     * @param request validated request body
     * @return 200 OK with BfhlResponse
     */
    @PostMapping
    public ResponseEntity<BfhlResponse> processData(@Valid @RequestBody BfhlRequest request) {
        log.info("POST /bfhl → received data: {}", request.getData());

        BfhlResponse response = bfhlService.processData(request);

        log.info("POST /bfhl → responding with is_success={}, user_id={}",
                response.isSuccess(), response.getUserId());

        return ResponseEntity.ok(response);
    }
}

package com.bajaj.controller;

import com.bajaj.dto.BfhlRequest;
import com.bajaj.dto.BfhlResponse;
import com.bajaj.service.BfhlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMvc integration-style tests for BfhlController.
 * The service layer is mocked; only controller + validation wiring is exercised.
 */
@WebMvcTest(BfhlController.class)
class BfhlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BfhlService bfhlService;

    // ── Happy path ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /bfhl returns HTTP 200 with is_success=true on valid input")
    void postBfhl_validRequest_returns200() throws Exception {
        BfhlResponse mockResponse = BfhlResponse.builder()
                .success(true)
                .userId("john_doe_17091999")
                .email("john@xyz.com")
                .rollNumber("ABCD123")
                .oddNumbers(List.of("1"))
                .evenNumbers(List.of("334", "4"))
                .alphabets(List.of("A", "R"))
                .specialCharacters(List.of("$"))
                .sum("339")
                .concatString("Ra")
                .build();

        Mockito.when(bfhlService.processData(any(BfhlRequest.class)))
               .thenReturn(mockResponse);

        String requestBody = """
                {
                  "data": ["a", "1", "334", "4", "R", "$"]
                }
                """;

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.user_id").value("john_doe_17091999"))
                .andExpect(jsonPath("$.email").value("john@xyz.com"))
                .andExpect(jsonPath("$.roll_number").value("ABCD123"))
                .andExpect(jsonPath("$.odd_numbers[0]").value("1"))
                .andExpect(jsonPath("$.sum").value("339"))
                .andExpect(jsonPath("$.concat_string").value("Ra"));
    }

    // ── Validation errors ─────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /bfhl returns 400 when 'data' field is missing")
    void postBfhl_missingDataField_returns400() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl returns 400 for malformed JSON")
    void postBfhl_malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalid json }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl returns 400 for completely empty body")
    void postBfhl_emptyBody_returns400() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }
}

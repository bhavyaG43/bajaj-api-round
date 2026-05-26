package com.bajaj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request DTO for the BFHL API endpoint.
 * Carries the input data array from the client.
 */
public class BfhlRequest {

    @NotNull(message = "data field must not be null")
    @JsonProperty("data")
    private List<String> data;

    // ── Constructors ──────────────────────────────────────────────────────────

    public BfhlRequest() {}

    public BfhlRequest(List<String> data) {
        this.data = data;
    }

    // ── Getter / Setter ───────────────────────────────────────────────────────

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BfhlRequest{data=" + data + "}";
    }
}

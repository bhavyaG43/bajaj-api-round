package com.bajaj.service;

import com.bajaj.dto.BfhlRequest;
import com.bajaj.dto.BfhlResponse;

/**
 * Service interface for BFHL data processing operations.
 * Follows the interface-based service pattern for clean abstraction.
 */
public interface BfhlService {

    /**
     * Processes the incoming data array and returns a structured response
     * containing categorized numbers, alphabets, special characters,
     * their sum, and the alternating-caps concatenated string.
     *
     * @param request the incoming request containing the data array
     * @return BfhlResponse with all computed fields
     */
    BfhlResponse processData(BfhlRequest request);
}

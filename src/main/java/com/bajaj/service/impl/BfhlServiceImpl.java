package com.bajaj.service.impl;

import com.bajaj.dto.BfhlRequest;
import com.bajaj.dto.BfhlResponse;
import com.bajaj.service.BfhlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of BfhlService.
 * Reads user identity from application.properties and contains
 * all data classification + transformation logic.
 */
@Service
public class BfhlServiceImpl implements BfhlService {

    private static final Logger log = LoggerFactory.getLogger(BfhlServiceImpl.class);

    // ── Injected from application.properties ──────────────────────────────────

    @Value("${app.user.full-name}")
    private String fullName;

    @Value("${app.user.dob}")
    private String dob;

    @Value("${app.user.email}")
    private String email;

    @Value("${app.user.roll-number}")
    private String rollNumber;

    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public BfhlResponse processData(BfhlRequest request) {
        List<String> data = request.getData();
        log.debug("Processing {} element(s): {}", data.size(), data);

        List<String>   oddNumbers        = new ArrayList<>();
        List<String>   evenNumbers       = new ArrayList<>();
        List<String>   alphabets         = new ArrayList<>();
        List<String>   specialChars      = new ArrayList<>();
        long           numericalSum      = 0;
        List<Character> allAlphaChars    = new ArrayList<>();

        for (String token : data) {
            if (isNumber(token)) {
                long value = Long.parseLong(token);
                if (value % 2 == 0) {
                    evenNumbers.add(token);
                } else {
                    oddNumbers.add(token);
                }
                numericalSum += value;

            } else if (isAlphaOnly(token)) {
                alphabets.add(token.toUpperCase());
                for (char c : token.toCharArray()) {
                    if (Character.isLetter(c)) {
                        allAlphaChars.add(c);
                    }
                }
            } else {
                specialChars.add(token);
            }
        }

        String concatString = buildConcatString(allAlphaChars);
        String userId       = buildUserId();

        log.debug("Computed → userId={}, sum={}, concat={}", userId, numericalSum, concatString);

        return BfhlResponse.builder()
                .success(true)
                .userId(userId)
                .email(email)
                .rollNumber(rollNumber)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialChars)
                .sum(String.valueOf(numericalSum))
                .concatString(concatString)
                .build();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Builds user_id from application.properties.
     * Format: {full_name_ddmmyyyy}  e.g. "bhavya_gupta_04032005"
     */
    private String buildUserId() {
        String normalised = fullName.trim().toLowerCase().replaceAll("\\s+", "_");
        return normalised + "_" + dob;
    }

    /** Returns true if every character in the token is a digit (whole number). */
    private boolean isNumber(String token) {
        if (token == null || token.isEmpty()) return false;
        int start = (token.charAt(0) == '-') ? 1 : 0;
        if (start == token.length()) return false;
        for (int i = start; i < token.length(); i++) {
            if (!Character.isDigit(token.charAt(i))) return false;
        }
        return true;
    }

    /** Returns true if every character in the token is an alphabetic letter. */
    private boolean isAlphaOnly(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isLetter(c)) return false;
        }
        return true;
    }

    /**
     * Builds the alternating-caps reverse-concatenated string.
     *
     * Steps:
     *  1. Collect all letters from all alpha tokens in input order.
     *  2. Reverse the collected list.
     *  3. Apply alternating case: index 0 → UPPER, index 1 → lower, …
     *
     * Verified against all three spec examples:
     *  A: [a,R]      → reversed [R,a]    → "Ra"       ✓
     *  B: [a,y,b]    → reversed [b,y,a]  → "ByA"      ✓
     *  C: [A,ABCD,DOE] chars [A,A,B,C,D,D,O,E]
     *                → reversed [E,O,D,D,C,B,A,A] → "EoDdCbAa" ✓
     */
    private String buildConcatString(List<Character> chars) {
        if (chars.isEmpty()) return "";

        List<Character> reversed = new ArrayList<>(chars);
        Collections.reverse(reversed);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < reversed.size(); i++) {
            char c = reversed.get(i);
            sb.append((i % 2 == 0)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c));
        }
        return sb.toString();
    }
}

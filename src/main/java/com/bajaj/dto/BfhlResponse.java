package com.bajaj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response DTO for the BFHL API endpoint.
 * Uses a static inner Builder for fluent construction without Lombok.
 */
public class BfhlResponse {

    @JsonProperty("is_success")
    private boolean success;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("roll_number")
    private String rollNumber;

    @JsonProperty("odd_numbers")
    private List<String> oddNumbers;

    @JsonProperty("even_numbers")
    private List<String> evenNumbers;

    @JsonProperty("alphabets")
    private List<String> alphabets;

    @JsonProperty("special_characters")
    private List<String> specialCharacters;

    @JsonProperty("sum")
    private String sum;

    @JsonProperty("concat_string")
    private String concatString;

    // ── Constructors ──────────────────────────────────────────────────────────

    public BfhlResponse() {}

    private BfhlResponse(Builder builder) {
        this.success          = builder.success;
        this.userId           = builder.userId;
        this.email            = builder.email;
        this.rollNumber       = builder.rollNumber;
        this.oddNumbers       = builder.oddNumbers;
        this.evenNumbers      = builder.evenNumbers;
        this.alphabets        = builder.alphabets;
        this.specialCharacters = builder.specialCharacters;
        this.sum              = builder.sum;
        this.concatString     = builder.concatString;
    }

    public static Builder builder() {
        return new Builder();
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public boolean isSuccess()                          { return success; }
    public void    setSuccess(boolean success)          { this.success = success; }

    public String  getUserId()                          { return userId; }
    public void    setUserId(String userId)             { this.userId = userId; }

    public String  getEmail()                           { return email; }
    public void    setEmail(String email)               { this.email = email; }

    public String  getRollNumber()                      { return rollNumber; }
    public void    setRollNumber(String rollNumber)     { this.rollNumber = rollNumber; }

    public List<String> getOddNumbers()                           { return oddNumbers; }
    public void         setOddNumbers(List<String> oddNumbers)    { this.oddNumbers = oddNumbers; }

    public List<String> getEvenNumbers()                          { return evenNumbers; }
    public void         setEvenNumbers(List<String> evenNumbers)  { this.evenNumbers = evenNumbers; }

    public List<String> getAlphabets()                            { return alphabets; }
    public void         setAlphabets(List<String> alphabets)      { this.alphabets = alphabets; }

    public List<String> getSpecialCharacters()                              { return specialCharacters; }
    public void         setSpecialCharacters(List<String> specialCharacters){ this.specialCharacters = specialCharacters; }

    public String  getSum()                     { return sum; }
    public void    setSum(String sum)           { this.sum = sum; }

    public String  getConcatString()                    { return concatString; }
    public void    setConcatString(String concatString) { this.concatString = concatString; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static final class Builder {
        private boolean      success;
        private String       userId;
        private String       email;
        private String       rollNumber;
        private List<String> oddNumbers;
        private List<String> evenNumbers;
        private List<String> alphabets;
        private List<String> specialCharacters;
        private String       sum;
        private String       concatString;

        private Builder() {}

        public Builder success(boolean success)                        { this.success = success;                   return this; }
        public Builder userId(String userId)                           { this.userId = userId;                     return this; }
        public Builder email(String email)                             { this.email = email;                       return this; }
        public Builder rollNumber(String rollNumber)                   { this.rollNumber = rollNumber;             return this; }
        public Builder oddNumbers(List<String> oddNumbers)             { this.oddNumbers = oddNumbers;             return this; }
        public Builder evenNumbers(List<String> evenNumbers)           { this.evenNumbers = evenNumbers;           return this; }
        public Builder alphabets(List<String> alphabets)               { this.alphabets = alphabets;               return this; }
        public Builder specialCharacters(List<String> specialCharacters){ this.specialCharacters = specialCharacters; return this; }
        public Builder sum(String sum)                                 { this.sum = sum;                           return this; }
        public Builder concatString(String concatString)               { this.concatString = concatString;         return this; }

        public BfhlResponse build() { return new BfhlResponse(this); }
    }
}

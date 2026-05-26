package com.bajaj.service;

import com.bajaj.dto.BfhlRequest;
import com.bajaj.dto.BfhlResponse;
import com.bajaj.service.impl.BfhlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for BfhlServiceImpl covering all three specification examples
 * plus edge cases (empty data, only specials, zero, string type check).
 *
 * Uses ReflectionTestUtils to inject @Value fields without spinning up
 * a full Spring context — keeps tests fast and focused.
 */
class BfhlServiceImplTest {

    private BfhlServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BfhlServiceImpl();
        ReflectionTestUtils.setField(service, "fullName",   "john doe");
        ReflectionTestUtils.setField(service, "dob",        "17091999");
        ReflectionTestUtils.setField(service, "email",      "john@xyz.com");
        ReflectionTestUtils.setField(service, "rollNumber", "ABCD123");
    }

    // ── user_id format ────────────────────────────────────────────────────────

    @Test
    @DisplayName("user_id is built as full_name_ddmmyyyy (lowercase, underscores)")
    void userId_isCorrectlyFormatted() {
        BfhlResponse r = process("1");
        assertThat(r.getUserId()).isEqualTo("john_doe_17091999");
    }

    // ── Spec Example A ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Example A: [a, 1, 334, 4, R, $]")
    void exampleA() {
        BfhlResponse r = process("a", "1", "334", "4", "R", "$");

        assertThat(r.isSuccess()).isTrue();
        assertThat(r.getOddNumbers()).containsExactly("1");
        assertThat(r.getEvenNumbers()).containsExactlyInAnyOrder("334", "4");
        assertThat(r.getAlphabets()).containsExactlyInAnyOrder("A", "R");
        assertThat(r.getSpecialCharacters()).containsExactly("$");
        assertThat(r.getSum()).isEqualTo("339");
        assertThat(r.getConcatString()).isEqualTo("Ra");
    }

    // ── Spec Example B ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Example B: [2, a, y, 4, &, -, *, 5, 92, b]")
    void exampleB() {
        BfhlResponse r = process("2", "a", "y", "&", "-", "*", "5", "92", "b", "4");

        assertThat(r.getOddNumbers()).containsExactly("5");
        assertThat(r.getEvenNumbers()).containsExactlyInAnyOrder("2", "4", "92");
        assertThat(r.getAlphabets()).containsExactlyInAnyOrder("A", "Y", "B");
        assertThat(r.getSpecialCharacters()).containsExactlyInAnyOrder("&", "-", "*");
        assertThat(r.getSum()).isEqualTo("103");
        assertThat(r.getConcatString()).isEqualTo("ByA");
    }

    // ── Spec Example C ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Example C: [A, ABCD, DOE] — multi-char alpha tokens")
    void exampleC() {
        BfhlResponse r = process("A", "ABCD", "DOE");

        assertThat(r.getOddNumbers()).isEmpty();
        assertThat(r.getEvenNumbers()).isEmpty();
        assertThat(r.getAlphabets()).containsExactly("A", "ABCD", "DOE");
        assertThat(r.getSpecialCharacters()).isEmpty();
        assertThat(r.getSum()).isEqualTo("0");
        assertThat(r.getConcatString()).isEqualTo("EoDdCbAa");
    }

    // ── Edge cases ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Empty data list → zeroed-out response, is_success=true")
    void emptyData_returnsDefaults() {
        BfhlResponse r = process();
        assertThat(r.isSuccess()).isTrue();
        assertThat(r.getSum()).isEqualTo("0");
        assertThat(r.getConcatString()).isEmpty();
        assertThat(r.getOddNumbers()).isEmpty();
        assertThat(r.getEvenNumbers()).isEmpty();
        assertThat(r.getAlphabets()).isEmpty();
        assertThat(r.getSpecialCharacters()).isEmpty();
    }

    @Test
    @DisplayName("Zero (\"0\") is classified as even")
    void zero_isEven() {
        BfhlResponse r = process("0");
        assertThat(r.getEvenNumbers()).containsExactly("0");
        assertThat(r.getOddNumbers()).isEmpty();
        assertThat(r.getSum()).isEqualTo("0");
    }

    @Test
    @DisplayName("All special characters → alphabets/numbers empty")
    void allSpecialChars() {
        BfhlResponse r = process("@", "#", "!");
        assertThat(r.getSpecialCharacters()).containsExactlyInAnyOrder("@", "#", "!");
        assertThat(r.getConcatString()).isEmpty();
        assertThat(r.getSum()).isEqualTo("0");
    }

    @Test
    @DisplayName("Numbers returned as String instances (not int/long)")
    void numbersReturnedAsStrings() {
        BfhlResponse r = process("1", "2");
        List<String> all = new java.util.ArrayList<>();
        all.addAll(r.getOddNumbers());
        all.addAll(r.getEvenNumbers());
        for (String s : all) {
            assertThat(s).isInstanceOf(String.class);
        }
    }

    @Test
    @DisplayName("email and roll_number are populated from injected config")
    void emailAndRollNumber_fromConfig() {
        BfhlResponse r = process("1");
        assertThat(r.getEmail()).isEqualTo("john@xyz.com");
        assertThat(r.getRollNumber()).isEqualTo("ABCD123");
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private BfhlResponse process(String... tokens) {
        List<String> data = (tokens.length == 0)
                ? Collections.emptyList()
                : Arrays.asList(tokens);
        return service.processData(new BfhlRequest(data));
    }
}

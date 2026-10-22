package mcoe.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeValidatorTest {
    @Test
    void acceptsValidEmployee() {
        assertTrue(EmployeeValidator.validate("Asha Patil", "asha@example.com", "Engineering").isEmpty());
    }

    @Test
    void rejectsMissingAndMalformedValues() {
        assertFalse(EmployeeValidator.validate("", "not-an-email", "").isEmpty());
    }

    @Test
    void rejectsValuesOverDatabaseLimits() {
        assertFalse(EmployeeValidator.validate("a".repeat(101), "a@example.com", "Engineering").isEmpty());
    }
}

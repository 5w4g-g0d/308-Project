import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DriverTest {

    @Test
    public void testValidEmail() {
        assertTrue(Driver.valid_email("test@example.com"));
        assertFalse(Driver.valid_email("invalid_email"));
    }

    @Test
    public void testValidDOBFormat() {
        assertTrue(Driver.valid_dob_format("2023-01-01"));
        assertFalse(Driver.valid_dob_format("01-01-2023"));
        assertFalse(Driver.valid_dob_format("2023/01/01"));
    }

    @Test
    public void testIsLeapYear() {
        assertTrue(Driver.is_leap_year(2000));
        assertTrue(Driver.is_leap_year(2024));
        assertFalse(Driver.is_leap_year(1900));
        assertFalse(Driver.is_leap_year(2023));
    }
}



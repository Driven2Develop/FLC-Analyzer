package helpers;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for DateUtils
 *
 * @author Bowen Cheng
 */
public class DateUtilTest {

    @Test
    public void formatDateStringTest() {
        assertEquals("Mar. 01 2022", DateUtil.formatDateString(LocalDate.of(2022, 3, 1)));
    }
}

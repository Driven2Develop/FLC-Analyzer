package helpers;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for DateUtils
 *
 * @author Bowen Cheng
 */
public class DateUtilTest {

    /**
     * test static method <code>formatDateString</code> of class <code>DateUtil</code>
     *
     * @author Bowen Cheng
     */
    @Test
    public void testFormatDateString() {
        assertEquals("01/03/2022", DateUtil.formatDateString(LocalDate.of(2022, 3, 1)));
    }

    /**
     * test static method <code>parseDate</code> of class <code>DateUtil</code>
     *
     * @author Mengqi Liu
     */
    @Test
    public void testParseDate() {
        assertEquals(LocalDate.of(2005, Month.MARCH, 17), new DateUtil().parseDate(1111111111L));
    }

}

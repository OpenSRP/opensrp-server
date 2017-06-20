package org.opensrp.common.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;


public class DateUtilTest {
    DateUtil dateUtil;

    @Test
    public void testMillis() throws Exception {
        LocalDate today = LocalDate.now();
        long expected = today.toDate().getTime();
        long localDate = DateUtil.millis();
        assertNotSame(expected, localDate);
    }

    @Test
    public void testMillisFromMockDate() throws Exception {
        LocalDate today = LocalDate.now();
        long expectedTime = today.toDate().getTime();
        MockDate mockDate = new MockDate(today);
        long localDate = mockDate.millis();

        assertEquals(expectedTime, localDate);
    }



    @Test
    public void testIsDateWithinGivenPeriodBeforeToday() throws Exception {
        LocalDate referenceDateForSchedule = LocalDate.parse("2017-05-27");
        Period period = Period.days(3);
        assertEquals(false, DateUtil.isDateWithinGivenPeriodBeforeToday(referenceDateForSchedule, period));
    }

    @Test
    public void testParseDate() throws Exception {
        String yyyyMMdd = "2017-05-30";
        String yyyyMMddHHmmss = "2012-07-10 14:58:00";
        String yyyyMMddTHHmmssSSSZ = "2017-06-01T14:29:27.845+0000";


        assertEquals(yyyyMMdd, DateUtil.yyyyMMdd.format(DateUtil.parseDate(yyyyMMdd).toDate()));
        assertEquals(yyyyMMddHHmmss, DateUtil.yyyyMMddHHmmss.format(DateUtil.parseDate(yyyyMMddHHmmss).toDate()));

        DateTime dateInSystemTimeZone = DateUtil.parseDate(yyyyMMddTHHmmssSSSZ);
        DateUtil.yyyyMMddTHHmmssSSSZ.setTimeZone(TimeZone.getTimeZone("GMT"));
        assertEquals(yyyyMMddTHHmmssSSSZ, DateUtil.yyyyMMddTHHmmssSSSZ.format(dateInSystemTimeZone.toDate()));

    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws Exception{
        String invalidDate = "dfasfasdfas";
        DateUtil.parseDate(invalidDate);
    }

    @Test
    public void testGetTodayAsString() throws Exception {
        LocalDate today = LocalDate.now();
        String formattedDate = today.toString("yyyy-MM-dd");
        assertEquals(formattedDate, DateUtil.getTodayAsString());
    }

    @Test
    public void testTryParse() {
        String value = "1994-04-03";
        LocalDate tested = LocalDate.parse(value);
        LocalDate defaultvalue = LocalDate.now();
        assertEquals(tested, DateUtil.tryParse(value, defaultvalue));
        assertNotSame(tested, DateUtil.tryParse("x", defaultvalue));
    }

    @Test
    public void testGetDateFromString() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String dateString = "2017-05-30T14:19:01.232+0600";
        String temp = formatter.format(new Date());
        Date date = null;
        date = formatter.parse(dateString);
        assertEquals(date, DateUtil.getDateFromString(dateString));

        String s = "abc invalid date";
        assertEquals(null, DateUtil.getDateFromString(s));
    }
}

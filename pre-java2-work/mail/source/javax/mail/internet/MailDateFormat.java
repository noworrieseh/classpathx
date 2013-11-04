/*
 * MailDateFormat.java
 * Copyright (C) 2001 dog <dog@dog.net.uk>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package javax.mail.internet;

import java.io.PrintStream;
import java.text.*;
import java.util.*;

/**
 * Formats and parses date specification based on the
 * draft-ietf-drums-msg-fmt-08 dated January 26, 2000.
 * This is a followup spec to RFC822.
 * <p>
 * This class does not take pattern strings.
 * It always formats the date based on the specification below.
 * <p>
 * 3.3 Date and Time Specification
 * <p>
 * Date and time occur in several header fields of a message. This section
 * specifies the syntax for a full date and time specification. Though folding
 * whitespace is permitted throughout the date-time specification, it is
 * recommended that only a single space be used where FWS is required and 
 * no space be used where FWS is optional in the date-time specification;
 * some older implementations may not interpret other occurrences of folding 
 * whitespace correctly.
 * <pre>
date-time = [ day-of-week "," ] date FWS time [CFWS]

day-of-week = ([FWS] day-name) / obs-day-of-week

day-name = "Mon" / "Tue" / "Wed" / "Thu" / "Fri" / "Sat" / "Sun"

date = day month year

year = 4*DIGIT / obs-year

month = (FWS month-name FWS) / obs-month

month-name = "Jan" / "Feb" / "Mar" / "Apr" /
             "May" / "Jun" / "Jul" / "Aug" /
             "Sep" / "Oct" / "Nov" / "Dec"


day = ([FWS] 1*2DIGIT) / obs-day

time = time-of-day FWS zone

time-of-day = hour ":" minute [ ":" second ]

hour = 2DIGIT / obs-hour

minute = 2DIGIT / obs-minute

second = 2DIGIT / obs-second

zone = (( "+" / "-" ) 4DIGIT) / obs-zone
</pre>
 * <p>
 * The day is the numeric day of the month.
 * The year is any numeric year in the common era.
 * <p>
 * The time-of-day specifies the number of hours, minutes, and optionally 
 * seconds since midnight of the date indicated.
 * <p>
 * The date and time-of-day SHOULD express local time.
 * <p>
 * The zone specifies the offset from Coordinated Universal Time (UTC,
 * formerly referred to as "Greenwich Mean Time") that the date and 
 * time-of-day represent.
 * The "+" or "-" indicates whether the time-of-day is ahead of or behind
 * Universal Time. The first two digits indicate the number of hours'
 * difference from Universal Time, and the last two digits indicate the 
 * number of minutes' difference from Universal Time. (Hence, +hhmm means 
 * +(hh * 60 + mm) minutes, and -hhmm means -(hh * 60 + mm) minutes).
 * The form "+0000" SHOULD be used to indicate a time zone at Universal Time.
 * Though "-0000" also indicates Universal Time, it is used to indicate that 
 * the time was generated on a system that may be in a local time zone other 
 * than Universal Time.
 * <p>
 * A date-time specification MUST be semantically valid.
 * That is, the day-of-the week (if included) MUST be the day implied by 
 * the date, the numeric day-of-month MUST be between 1 and the number of 
 * days allowed for the specified month (in the specified year), the 
 * time-of-day MUST be in the range 00:00:00 through 23:59:60 (the number 
 * of seconds allowing for a leap second; see [STD-12]), and the zone MUST 
 * be within the range -9959 through +9959.
 */
public class MailDateFormat
  extends SimpleDateFormat
{

  public MailDateFormat()
  {
    super("EEE, d MMM yyyy HH:mm:ss 'ZZZZZ'", Locale.US);
    //super("EEE, d MMM yyyy HH:mm:ss 'ZZZZZ' (z)", Locale.US);
    calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
  }

  /**
   * Formats the given date in the format specified by
   * draft-ietf-drums-msg-fmt-08 in the current TimeZone.
   * @param date the Date object
   * @param dateStrBuf the formatted string
   * @param fieldPosition the current field position
   */
  public StringBuffer format(Date date, StringBuffer dateStrBuf,
      FieldPosition fieldPosition)
  {
    // Use super.format to do all but the pseudo time zone.
    int offset = dateStrBuf.length();
    super.format(date, dateStrBuf, fieldPosition);
    // Advance pos to the beginning of the pseudo time zone field ZZZZZ
    int pos = offset+25;
    while (dateStrBuf.charAt(pos)!='Z')
      pos++;
    
    // get the time zone offset (in minutes)
    calendar.clear();
    calendar.setTime(date);
    int zoneOffset = (calendar.get(Calendar.ZONE_OFFSET)+
      calendar.get(Calendar.DST_OFFSET))/60000;

    // apply + or - appropriately
    if (zoneOffset<0)
    {
      zoneOffset = -zoneOffset;
      dateStrBuf.setCharAt(pos++, '-');
    }
    else
      dateStrBuf.setCharAt(pos++, '+');

    // set the 2 2-char fields as specified above
    int hours = zoneOffset/60;
    dateStrBuf.setCharAt(pos++, Character.forDigit(hours/10, 10));
    dateStrBuf.setCharAt(pos++, Character.forDigit(hours%10, 10));
    int minutes = zoneOffset%60;
    dateStrBuf.setCharAt(pos++, Character.forDigit(minutes/10, 10));
    dateStrBuf.setCharAt(pos++, Character.forDigit(minutes%10, 10));
    
    return dateStrBuf;
  }

  /**
   * Parses the given date in the format specified by
   * draft-ietf-drums-msg-fmt-08 in the current TimeZone.
   * @param text the formatted date to be parsed
   * @param pos the current parse position
   */
  public Date parse(String text, ParsePosition pos)
  {
    // strip out the timezone (ZZZZZ) field if it exists
    // there can't be a + or - until that field
    int zoneOffset = 0;
    int start = text.indexOf('+');
    if (start<0)
      start = text.indexOf('-');
    if (start>-1)
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append(text.substring(0, start));
      int end = text.indexOf(' ', start+1);
      if (end==(start+5))
      {
        buffer.append(text.substring(end));
        char pm = text.charAt(start++);
        zoneOffset += 600*Character.digit(text.charAt(start++), 10);
        zoneOffset += 60*Character.digit(text.charAt(start++), 10);
        zoneOffset += 10*Character.digit(text.charAt(start++), 10);
        zoneOffset += Character.digit(text.charAt(start++), 10);
        zoneOffset *= 60000;
        if ('-'==pm)
          zoneOffset = -zoneOffset;
      }
      text = buffer.toString();
    }
    Date date = super.parse(text, pos);
    // Use the zoneOffset to compensate as raw offset
    // as there is no way to determine the DST part
    if (date!=null)
    {
      calendar.clear();
      calendar.setTime(date);
      calendar.set(Calendar.ZONE_OFFSET, zoneOffset);
      date = calendar.getTime();
    }
    return date;
  }

  /**
   * Don't allow setting the calendar.
   */
  public void setCalendar(Calendar newCalendar)
  {
    throw new RuntimeException("Method not available");
  }

  /**
   * Don't allow setting the NumberFormat.
   */
  public void setNumberFormat(NumberFormat newNumberFormat)
  {
    throw new RuntimeException("Method not available");
  }

}

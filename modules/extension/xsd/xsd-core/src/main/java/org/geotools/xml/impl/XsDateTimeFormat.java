/*
 * Copyright 2004  The Apache Software Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.geotools.xml.impl;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;


/** <p>An instance of {@link java.text.Format}, which may be used
 * to parse and format <code>xs:dateTime</code> values.</p>
 *
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 *
 *
 * @source $URL$
 */
public class XsDateTimeFormat extends Format {
    final boolean parseDate;
    final boolean parseTime;

    XsDateTimeFormat(boolean pParseDate, boolean pParseTime) {
        parseDate = pParseDate;
        parseTime = pParseTime;
    }

    /** Creates a new instance.
     */
    public XsDateTimeFormat() {
        this(true, true);
    }

    private int parseInt(String pString, int pOffset, StringBuffer pDigits) {
        int length = pString.length();
        pDigits.setLength(0);
        while (pOffset < length) {
            char c = pString.charAt(pOffset);
            if (Character.isDigit(c)) {
                pDigits.append(c);
                ++pOffset;
            } else {
                break;
            }
        }
        return pOffset;
    }

    public Object parseObject(String pString, ParsePosition pParsePosition) {
        if (pString == null) {
            throw new NullPointerException("The String argument must not be null.");
        }
        if (pParsePosition == null) {
            throw new NullPointerException("The ParsePosition argument must not be null.");
        }
        int offset = pParsePosition.getIndex();
        int length = pString.length();

        boolean isMinus = false;
        StringBuffer digits = new StringBuffer();
        int year, month, mday;
        if (parseDate) {
            // Sign
            if (offset < length) {
                char c = pString.charAt(offset);
                if (c == '+') {
                    ++offset;
                } else if (c == '-') {
                    ++offset;
                    isMinus = true;
                }
            }

	        offset = parseInt(pString, offset, digits);
	        if (digits.length() < 4) {
	            pParsePosition.setErrorIndex(offset);
	            return null;
	        }
	        year = Integer.parseInt(digits.toString());
	
	        if (offset < length  &&  pString.charAt(offset) == '-') {
	            ++offset;
	        } else {
	            pParsePosition.setErrorIndex(offset);
	            return null;
	        }
	
	        offset = parseInt(pString, offset, digits);
	        if (digits.length() != 2) {
	            pParsePosition.setErrorIndex(offset);
	            return null;
	        }
	        month = Integer.parseInt(digits.toString());
	
	        if (offset < length &&  pString.charAt(offset) == '-') {
	            ++offset;
	        } else {
	            pParsePosition.setErrorIndex(offset);
	            return null;
	        }
	
	        offset = parseInt(pString, offset, digits);
	        if (digits.length() != 2) {
	            pParsePosition.setErrorIndex(offset);
	            return null;
	        }
	        mday = Integer.parseInt(digits.toString());

	        if (parseTime) {
	            if (offset < length  &&  pString.charAt(offset) == 'T') {
	                ++offset;
	            } else {
	                pParsePosition.setErrorIndex(offset);
	                return null;
	            }
	        }
        } else {
            year = month = mday = 0;
        }

        int hour, minute, second, millis;
        if (parseTime) {
	        offset = parseInt(pString, offset, digits);
	        if (digits.length() != 2) {
	            pParsePosition.setErrorIndex(offset);
	            return null;
	        }
	        hour = Integer.parseInt(digits.toString());
	
	        if (offset < length  &&  pString.charAt(offset) == ':') {
	            ++offset;
	        } else {
	            pParsePosition.setErrorIndex(offset);
	            return null;
	        }
	
	        offset = parseInt(pString, offset, digits);
	        if (digits.length() != 2) {
	            pParsePosition.setErrorIndex(offset);
	            return null;
	        }
	        minute = Integer.parseInt(digits.toString());
	
	        if (offset < length  &&  pString.charAt(offset) == ':') {
	            ++offset;
	        } else {
	            pParsePosition.setErrorIndex(offset);
	            return null;
	        }
	
	        offset = parseInt(pString, offset, digits);
	        if (digits.length() != 2) {
	            pParsePosition.setErrorIndex(offset);
	            return null;
	        }
	        second = Integer.parseInt(digits.toString());
	
	        if (offset < length  &&  pString.charAt(offset) == '.') {
	            ++offset;
	            offset = parseInt(pString, offset, digits);
	            if (digits.length() > 0) {
	                millis = Integer.parseInt(digits.toString());
                    if (millis > 999) {
                        pParsePosition.setErrorIndex(offset);
                        return null;
                    }
                    for (int i = digits.length();  i < 3;  i++) {
                        millis *= 10;
                    }
	            } else {
	                millis = 0;
	            }
	        } else {
	            millis = 0;
	        }
        } else {
            hour = minute = second = millis = 0;
        }

        digits.setLength(0);
        digits.append("GMT");
        if (offset < length) {
            char c = pString.charAt(offset);
            if (c == 'Z') {
                // Ignore UTC, it is the default
                ++offset;
            } else if (c == '+' || c == '-') {
                digits.append(c);
                ++offset;
                for (int i = 0;  i < 5;  i++) {
                    if (offset >= length) {
                        pParsePosition.setErrorIndex(offset);
                        return null;
                    }
                    c = pString.charAt(offset);
                    if ((i != 2  &&  Character.isDigit(c))  ||
                        (i == 2  &&  c == ':')) {
                        digits.append(c);
                    } else {
                        pParsePosition.setErrorIndex(offset);
                        return null;
                    }
                    ++offset;
                }
            }
        }

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(digits.toString()));
        cal.set(isMinus ? -year : year, parseDate ? month-1 : month, mday, hour, minute, second);
        cal.set(Calendar.MILLISECOND, millis);
        pParsePosition.setIndex(offset);
        return cal;
    }

    private void append(StringBuffer pBuffer, int pNum, int pMinLen) {
        String s = Integer.toString(pNum);
        for (int i = s.length();  i < pMinLen;  i++) {
            pBuffer.append('0');
        }
        pBuffer.append(s);
    }

    public StringBuffer format(Object pCalendar, StringBuffer pBuffer, FieldPosition pPos) {
        if (pCalendar == null) {
            throw new NullPointerException("The Calendar argument must not be null.");
        }
        if (pBuffer == null) {
            throw new NullPointerException("The StringBuffer argument must not be null.");
        }
        if (pPos == null) {
            throw new NullPointerException("The FieldPosition argument must not be null.");
        }

        Calendar cal = (Calendar) pCalendar;
        if (parseDate) {
	        int year = cal.get(Calendar.YEAR);
	        if (year < 0) {
	            pBuffer.append('-');
	            year = -year;
	        }
	        append(pBuffer, year, 4);
	        pBuffer.append('-');
	        append(pBuffer, cal.get(Calendar.MONTH)+1, 2);
	        pBuffer.append('-');
	        append(pBuffer, cal.get(Calendar.DAY_OF_MONTH), 2);
	        if (parseTime) {
	            pBuffer.append('T');
	        }
        }
        if (parseTime) {
	        append(pBuffer, cal.get(Calendar.HOUR_OF_DAY), 2);
	        pBuffer.append(':');
	        append(pBuffer, cal.get(Calendar.MINUTE), 2);
	        pBuffer.append(':');
	        append(pBuffer, cal.get(Calendar.SECOND), 2);
	        int millis = cal.get(Calendar.MILLISECOND);
	        if (millis > 0) {
	            pBuffer.append('.');
	            append(pBuffer, millis, 3);
	        }
        }
        TimeZone tz = cal.getTimeZone();
        // JDK 1.4: int offset = tz.getOffset(cal.getTimeInMillis());
        int offset = cal.get(Calendar.ZONE_OFFSET);
        if (tz.inDaylightTime(cal.getTime())) {
        	offset += cal.get(Calendar.DST_OFFSET);
        }
        if (offset == 0) {
            pBuffer.append('Z');
        } else {
            if (offset < 0) {
                pBuffer.append('-');
                offset = -offset;
            } else {
                pBuffer.append('+');
            }
            int minutes = offset / (60*1000);
            int hours = minutes / 60;
            minutes -= hours * 60;
            append(pBuffer, hours, 2);
            pBuffer.append(':');
            append(pBuffer, minutes, 2);
        }
        return pBuffer;
    }
}

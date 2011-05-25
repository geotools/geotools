/*
 * Copyright 2003, 2004  The Apache Software Foundation
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

import java.io.Serializable;


/** <p>Implementation of xs:duration.</p>
 *
 *
 * @source $URL$
 */
public class Duration implements Serializable, Comparable {
	private static final long serialVersionUID = 3257001055736117303L;
	private final boolean isNegative;
    private final int years, months, days, hours, minutes, seconds;
    private final long millis;

	/** Creates a new instance with the given values.
	 */
	public Duration(boolean pNegative, int pYears, int pMonths, int pDays, int pHours, int pMinutes, int pSeconds, long pMillis) {
        isNegative = pNegative;
        years = pYears;
        months = pMonths;
        days = pDays;
        hours = pHours;
        minutes = pMinutes;
        seconds = pSeconds;
        millis = pMillis;
    }
  
    /** <p>Returns the number of years.</p>
     */
    public int getYears() {
        return years;
    }

    /** <p>Returns the number of months.</p>
     */
    public int getMonths() {
        return months;
    }

    /** <p>Returns the number of days.</p>
     */
    public int getDays() {
        return days;
    }

    /** <p>Returns the number of hours.</p>
     */
    public int getHours() {
        return hours;
    }

    /** <p>Returns the number of minutes.</p>
     */
    public int getMinutes() {
        return minutes;
    }

    /** <p>Returns the number of seconds.</p>
     */
    public int getSeconds() {
        return seconds;
    }

    /** <p>Returns the number of milliseconds.</p>
     */
    public long getMillis() {
        return millis;
    }

    /** <p>Returns a string representation of this Duration.</p>
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append('P');
        sb.append(getYears());
        sb.append('Y');
        sb.append(getMonths());
        sb.append('M');
        sb.append(getDays());
        sb.append("DT");
        sb.append(getHours());
        sb.append('H');
        sb.append(getMinutes());
        sb.append('M');
        sb.append(getSeconds());
        long m = getMillis();
        if (m != 0) {
            sb.append('.');
            sb.append(m);
        }
        sb.append('S');
        return sb.toString();
    }

    /** <p>Converts the given String representation into an instance of
     * Duration.</p>
     * @throws IllegalArgumentException The String could not be parsed.
     */
    public static Duration valueOf(String pValue) {
        if (pValue == null) {
            throw new NullPointerException("The duration value must not be null.");
        }
        int len = pValue.length();
        int offset = 0;
        boolean isNegative;
        if (len > 0) {
            char c = pValue.charAt(0);
            if (c == '-') {
                isNegative = true;
                ++offset;
            } else if (c == '+') {
                isNegative = false;
                ++offset;
            } else {
                isNegative = false;
            }
        } else {
            throw new IllegalArgumentException("Invalid duration: Empty string");
        }
        
        if (len == 0  ||  pValue.charAt(offset) != 'P') {
            throw new IllegalArgumentException("Invalid duration: " + pValue + " (must start with P, +P, or -P)");
        } else {
            ++offset;
        }
        
        int years = -1, months = -1, daysOfMonth = -1, hours = -1, minutes = -1, seconds = -1;
        long millis = -1;
        int preDecimalPoint = -1;
        boolean separatorSeen = false;
        StringBuffer digits = new StringBuffer();
        while (offset < len) {
            char c = pValue.charAt(offset);
            if (Character.isDigit(c)) {
                digits.append(c);
            } else if (c == 'T') {
                if (separatorSeen) {
                    throw new IllegalArgumentException("Invalid duration: " + pValue
                            + " (date/time separator 'T' used twice)");
                } else {
                    separatorSeen = true;
                }
            } else {
                long l;
                if (digits.length() == 0) {
                    l = 0;
                } else {
                    try {
                        l = Long.parseLong(digits.toString());
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid duration: "  + pValue
                                + " (max long value exceeded by " + digits + ")");
                    }
                    digits.setLength(0);
                }
                if (preDecimalPoint >= 0) {
                    if (c == 'S') {
                        if (!separatorSeen) {
                            throw new IllegalArgumentException("Invalid duration: " + pValue
                                    + "(seconds specified before date/time separator 'T' seen)");
                        }
                        if (seconds != -1) {
                            throw new IllegalArgumentException("Invalid duration: " + pValue
                                    + " (seconds specified twice)");
                        }
                        seconds = preDecimalPoint;
                        millis = l;
                        preDecimalPoint = -1;
                    } else {
                        throw new IllegalArgumentException("Invalid duration: " + pValue
                                + " (decimal point not allowed here: "
                                + preDecimalPoint + "." + digits + c + ")");
                    }
                } else if (l > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("Invalid duration: " + pValue
                            + " (max integer value exceeded by " + digits + ")");
                } else {
                    int i = (int) l;
                    if (c == '.') {
                        preDecimalPoint = i;
                    } else if (separatorSeen) {
                        if (c == 'Y'  ||  c == 'D') {
                            throw new IllegalArgumentException("Invalid duration: " + pValue
                                    + " (years or days of month specified after date/time separator 'T' seen)");
                        } else if (c == 'S') {
                            if (seconds != -1) {
                                throw new IllegalArgumentException("Invalid duration: " + pValue
                                        + " (seconds specified twice)");
                            }
                            seconds = i;
                            millis = 0;
                        } else if (c == 'M') {
                            if (minutes != -1) {
                                throw new IllegalArgumentException("Invalid duration: " + pValue
                                        + " (minutes specified twice)");
                            } else if (seconds != -1) {
                                throw new IllegalArgumentException("Invalid duration: " + pValue
                                        + " (minutes specified after seconds)");
                            }
                            minutes = i;
                        } else if (c == 'H') {
                            if (hours != -1) {
                                throw new IllegalArgumentException("Invalid duration: " + pValue
                                        + " (hours specified twice)");
                           } else if (minutes != -1) {
                               throw new IllegalArgumentException("Invalid duration: " + pValue
                                       + " (hours specified after minutes)");
                           } else if (seconds != -1) {
                               throw new IllegalArgumentException("Invalid duration: " + pValue
                                       + " (seconds specified after minutes)");
                           }
                           hours = i;
                        }
                    } else {
                        if (c == 'H'  ||  c == 'S') {
                            throw new IllegalArgumentException("Invalid duration: " + pValue
                                    + " (hours or seconds specified before date/time separator 'T' seen)");
                        } else if (c == 'Y') {
                            if (years != -1) {
                                throw new IllegalArgumentException("Invalid duration: " + pValue
                                        + " (years specified twice)");
                            } else if (months != -1) {
                                throw new IllegalArgumentException("Invalid duration: " + pValue
                                        + " (years specified after months)");
                            } else if (daysOfMonth != -1) {
                                throw new IllegalArgumentException("Invalid duration: " + pValue
                                        + " (years specified after days of month)");
                            }
                            years = i;
                        } else if (c == 'M') {
                            if (months != -1) {
                                throw new IllegalArgumentException("Invalid duration: " + pValue
                                        + " (months specified twice)");
                            } else if (daysOfMonth != -1) {
                                throw new IllegalArgumentException("Invalid duration: " + pValue
                                        + " (days of month specified after months)");
                            }
                            months = i;
                        } else if (c == 'D') {
                            if (daysOfMonth != -1) {
                                throw new IllegalArgumentException("Invalid duration: " + pValue
                                                                   + " (days of month specified twice)");
                            }
                            daysOfMonth = i;
                        }
                    }
                }
            }
            ++offset;
        }
        return new Duration(isNegative,
                			years == -1 ? 0 : years,
							months == -1 ? 0 : months,
							daysOfMonth == -1 ? 0 :daysOfMonth,
							hours == -1 ? 0 : hours,
							minutes == -1 ? 0 : minutes,
							seconds == -1 ? 0 : seconds,
							millis == -1 ? 0 : millis);
    }

    public boolean equals(Object o) {
        if (o == null  ||  !(o instanceof Duration)) {
            return false;
        }
        return compareTo((Duration) o) == 0;
    }

    public int compareTo(Object o) {
        return compareTo((Duration) o);
    }

	/** Actual implementation of {@link #compareTo(Object)}.
	 */
	public int compareTo(Duration d) {
        if (isNegative != d.isNegative) {
            return isNegative ? -1 : 1;
        }
        if (years != d.years) { return years - d.years; }
        if (months != d.months) { return months - d.months; }
        if (days != d.days) { return days - d.days; }
        if (hours != d.hours) { return hours - d.hours; }
        if (minutes != d.minutes) { return minutes - d.minutes; }
        if (seconds != d.seconds) { return seconds - d.seconds; }
        if (millis > d.millis) {
            return 1;
        } else if (millis < d.millis) {
            return -1;
        } else {
            return 0;
        }
    }

    public int hashCode() {
        return isNegative ? 1 : 0 + years + months + days + hours + minutes + seconds + (int) millis;
    }
}

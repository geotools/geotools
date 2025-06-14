/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
/*
 * Copyright 2003-2016  The Apache Software Foundation
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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;
import java.util.Calendar;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import org.geotools.util.factory.Hints;

/** @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a> */
public class DatatypeConverterImpl implements DatatypeConverterInterface {

    private static DatatypeConverterImpl instance = new DatatypeConverterImpl();

    public static DatatypeConverterImpl getInstance() {
        return instance;
    }

    @Override
    public String parseString(String arg0) {
        return arg0;
    }

    @Override
    public BigInteger parseInteger(String arg0) {
        return new BigInteger(arg0);
    }

    @Override
    public int parseInt(String arg0) {
        return Integer.parseInt(arg0);
    }

    @Override
    public long parseLong(String arg0) {
        return Long.parseLong(arg0);
    }

    @Override
    public short parseShort(String arg0) {
        return Short.parseShort(arg0);
    }

    @Override
    public BigDecimal parseDecimal(String arg0) {
        return new BigDecimal(arg0);
    }

    @Override
    public float parseFloat(String arg0) {
        return Float.parseFloat(arg0);
    }

    @Override
    public double parseDouble(String arg0) {
        return Double.parseDouble(arg0);
    }

    @Override
    public boolean parseBoolean(String arg0) {
        return Boolean.parseBoolean(arg0);
    }

    @Override
    public byte parseByte(String arg0) {
        return Byte.parseByte(arg0);
    }

    @Override
    public QName parseQName(String arg0, NamespaceContext arg1) {
        int offset = arg0.indexOf(':');
        String uri;
        String localName;
        switch (offset) {
            case -1:
                localName = arg0;
                uri = arg1.getNamespaceURI("");
                if (uri == null) {
                    // Should not happen, indicates an error in the NamespaceContext implementation
                    throw new IllegalArgumentException("The default prefix is not bound.");
                }
                break;
            case 0:
                throw new IllegalArgumentException("Default prefix must be indicated by not using a colon: " + arg0);
            default:
                String prefix = arg0.substring(0, offset);
                localName = arg0.substring(offset + 1);
                uri = arg1.getNamespaceURI(prefix);
                if (uri == null) {
                    throw new IllegalArgumentException("The prefix " + prefix + " is not bound.");
                }
        }
        return new QName(uri, localName);
    }

    /**
     * Parses the lexical representation of the given dateTime value and converts it into an instance of
     * {@link java.util.Calendar}. Valid lexical representations of a dateTime value include
     *
     * <pre>
     *   YYYY-MM-DDThh:mm:ss
     *   YYYY-MM-DDThh:mm:ss.sss
     *   YYYY-MM-DDThh:mm:ssZ
     *   YYYY-MM-DDThh:mm:ss-01:00
     * </pre>
     *
     * The former examples are all specified in UTC time. The last example uses a negative offset of one hour to UTC.
     *
     * @param arg0 The input string being parsed.
     * @param lenient parameter used for allowing lenient parsing
     * @return The input string converted into an instance of {@link java.util.Calendar}.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public Calendar parseDateTime(String arg0, boolean lenient) {
        XsDateTimeFormat format = new XsDateTimeFormat();
        ParsePosition pos = new ParsePosition(0);
        Calendar cal = (Calendar) format.parseObject(arg0, pos, lenient);
        if (cal == null) {
            throw new IllegalArgumentException(
                    "Failed to parse dateTime " + arg0 + " at:" + arg0.substring(pos.getErrorIndex()));
        }
        return cal;
    }

    @Override
    public Calendar parseDateTime(String arg0) {
        return parseDateTime(arg0, false);
    }

    @Override
    public byte[] parseBase64Binary(String arg0) {
        try {
            return Base64Binary.decode(arg0);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to parse " + arg0 + ": " + e.getMessage());
        }
    }

    @Override
    public byte[] parseHexBinary(String arg0) {
        return HexBinary.decode(arg0);
    }

    private static final long MAX_UNSIGNED_INT = (long) Integer.MAX_VALUE * 2 + 1;

    @Override
    public long parseUnsignedInt(String arg0) {
        long l = Long.parseLong(arg0);
        if (l < 0) {
            throw new IllegalArgumentException("Failed to parse UnsignedInt " + arg0 + ": result is negative");
        }
        if (l > MAX_UNSIGNED_INT) {
            throw new IllegalArgumentException(
                    "Failed to parse UnsignedInt " + arg0 + ": result exceeds maximum value " + MAX_UNSIGNED_INT);
        }
        return l;
    }

    private static final int MAX_UNSIGNED_SHORT = Short.MAX_VALUE * 2 + 1;

    @Override
    public int parseUnsignedShort(String arg0) {
        int i = Integer.parseInt(arg0);
        if (i < 0) {
            throw new IllegalArgumentException("Failed to parse UnsignedShort " + arg0 + ": result is negative");
        }
        if (i > MAX_UNSIGNED_SHORT) {
            throw new IllegalArgumentException(
                    "Failed to parse UnsignedShort " + arg0 + ": result exceeds maximum value " + MAX_UNSIGNED_SHORT);
        }
        return i;
    }

    @Override
    public Calendar parseTime(String arg0) {
        XsTimeFormat format = new XsTimeFormat();
        ParsePosition pos = new ParsePosition(0);
        Calendar cal = (Calendar) format.parseObject(arg0, pos);
        if (cal == null) {
            throw new IllegalArgumentException(
                    "Failed to parse time " + arg0 + " at:" + arg0.substring(pos.getErrorIndex()));
        }
        return cal;
    }

    @Override
    public Calendar parseDate(String arg0) {
        XsDateFormat format = new XsDateFormat();
        ParsePosition pos = new ParsePosition(0);
        Calendar cal = (Calendar) format.parseObject(arg0, pos);
        if (cal == null) {
            throw new IllegalArgumentException(
                    "Failed to parse date " + arg0 + " at:" + arg0.substring(pos.getErrorIndex()));
        }
        return cal;
    }

    @Override
    public String parseAnySimpleType(String arg0) {
        return arg0;
    }

    public Duration parseDuration(String pDuration) {
        return Duration.valueOf(pDuration);
    }

    @Override
    public String printString(String arg0) {
        return arg0;
    }

    @Override
    public String printInteger(BigInteger arg0) {
        return arg0.toString();
    }

    @Override
    public String printInt(int arg0) {
        return Integer.toString(arg0);
    }

    @Override
    public String printLong(long arg0) {
        return Long.toString(arg0);
    }

    @Override
    public String printShort(short arg0) {
        return Short.toString(arg0);
    }

    @Override
    public String printDecimal(BigDecimal arg0) {
        return arg0.toString();
    }

    @Override
    public String printFloat(float arg0) {
        return Float.toString(arg0);
    }

    @Override
    public String printDouble(double arg0) {
        return Double.toString(arg0);
    }

    @Override
    public String printBoolean(boolean arg0) {
        return (arg0 ? Boolean.TRUE : Boolean.FALSE).toString();
    }

    @Override
    public String printByte(byte arg0) {
        return Byte.toString(arg0);
    }

    @Override
    public String printQName(QName arg0, NamespaceContext arg1) {
        String prefix = arg1.getPrefix(arg0.getNamespaceURI());
        if (prefix == null) {
            throw new IllegalArgumentException("The namespace URI " + arg0.getNamespaceURI() + " is not bound.");
        } else if (XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)) {
            return arg0.getLocalPart();
        } else {
            return prefix + ":" + arg0.getLocalPart();
        }
    }

    @Override
    public String printDateTime(Calendar arg0) {
        return new XsDateTimeFormat().format(arg0);
    }

    @Override
    public String printBase64Binary(byte[] arg0) {
        return Base64Binary.encode(arg0);
    }

    @Override
    public String printHexBinary(byte[] arg0) {
        return HexBinary.encode(arg0);
    }

    @Override
    public String printUnsignedInt(long arg0) {
        return Long.toString(arg0);
    }

    @Override
    public String printUnsignedShort(int arg0) {
        return Integer.toString(arg0);
    }

    @Override
    public String printTime(Calendar arg0) {
        return new XsTimeFormat().format(arg0);
    }

    @Override
    public String printDate(Calendar arg0) {
        Object hint = Hints.getSystemDefault(Hints.LOCAL_DATE_TIME_HANDLING);
        if (Boolean.TRUE.equals(hint)) {
            return new XsLocalDateFormat().format(arg0);
        }
        return new XsDateFormat().format(arg0);
    }

    @Override
    public String printAnySimpleType(String arg0) {
        return arg0;
    }

    public String printDuration(Duration pDuration) {
        return pDuration.toString();
    }
}

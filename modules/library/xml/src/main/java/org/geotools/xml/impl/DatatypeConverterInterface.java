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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

/**
 * The <code>DatatypeConverterInterface</code> is a helper class for implementation of custom <code>
 * print()</code> and <code>parse()</code> methods, as specified by
 * <samp>jaxb:javaType/@printMethod</samp> and <samp>jaxb:javaType/@parseMethod</samp>. However, the
 * JAXB user won't be accessing this class directly. The JAXB provider is required to create an
 * instance of this class and pass it to the JAXB runtime by invoking {@link
 * javax.xml.bind.DatatypeConverter#setDatatypeConverter(DatatypeConverterInterface)}. The JAXB user
 * may access this instance via the static methods of {@link javax.xml.bind.DatatypeConverter}.
 *
 * <p>The interface provides methods <code>parseFoo()</code> and <code>printFoo()</code> for any XML
 * Schema type <samp>foo</samp>. The <code>parseFoo()</code> method must accept any lexically valid
 * value and convert it into the corresponding canonical value. An error in the conversion routine
 * must be mapped to a {@link javax.xml.bind.ParseConversionEvent}.
 *
 * <p>The <code>printFoo(foo pValue)</code> method must convert the value <samp>pValue</samp> into
 * an arbitrary lexical representation. It is recommended to use the default representation.
 *
 * @author JSR-31
 * @since JAXB 1.0
 * @see javax.xml.bind.DatatypeConverter
 * @see javax.xml.bind.ParseConversionEvent
 * @see javax.xml.bind.PrintConversionEvent
 */
public interface DatatypeConverterInterface {
    /**
     * Parses the lexical representation and converts it into a String.
     *
     * @param pLexicalXSDString The input string being parsed.
     * @return The unmodified input string.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public String parseString(String pLexicalXSDString);

    /**
     * Parses the lexical representation of the given integer value (arbitrary precision) and
     * converts it into an instance of {@link java.math.BigInteger}.
     *
     * @param pLexicalXSDInteger The input string being parsed.
     * @return The input string converted into an instance of {@link BigInteger}.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public BigInteger parseInteger(String pLexicalXSDInteger);

    /**
     * Parses the lexical representation of the given 32 bit integer value and converts it into a
     * primitive <code>int</code> value.
     *
     * @param pLexicalXSDInt The input string being parsed.
     * @return The input string converted into a primitive <code>int</code>.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public int parseInt(String pLexicalXSDInt);

    /**
     * Parses the lexical representation of the given 64 bit integer value and converts it into a
     * primitive <code>long</code> value.
     *
     * @param pLexicalXSDLong The input string being parsed.
     * @return The input string converted into a primitive <code>long</code>.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public long parseLong(String pLexicalXSDLong);

    /**
     * Parses the lexical representation of the given 16 bit integer value and converts it into a
     * primitive <code>short</code> value.
     *
     * @param pLexicalXSDShort The input string being parsed.
     * @return The input string converted into a primitive <code>short</code>.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public short parseShort(String pLexicalXSDShort);

    /**
     * Parses the lexical representation of the given decimal value (arbitrary precision) and
     * converts it into an instance of {@link java.math.BigDecimal}.
     *
     * @param pLexicalXSDDecimal The input string being parsed.
     * @return The input string converted into an instance of {@link java.math.BigDecimal}.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public BigDecimal parseDecimal(String pLexicalXSDDecimal);

    /**
     * Parses the lexical representation of the given 32 bit floating point value and converts it
     * into a primitive <code>float</code> value.
     *
     * @param pLexicalXSDFloat The input string being parsed.
     * @return The input string converted into a primitive <code>float</code>.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public float parseFloat(String pLexicalXSDFloat);

    /**
     * Parses the lexical representation of the given 64 bit floating point value and converts it
     * into a primitive <code>double</code> value.
     *
     * @param pLexicalXSDDouble The input string being parsed.
     * @return The input string converted into a primitive <code>double</code>.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public double parseDouble(String pLexicalXSDDouble);

    /**
     * Parses the lexical representation of the given boolean value and converts it into a primitive
     * <code>boolean</code> value.
     *
     * @param pLexicalXSDBoolean The input string being parsed.
     * @return The input string converted into a primitive <code>boolean</code>.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public boolean parseBoolean(String pLexicalXSDBoolean);

    /**
     * Parses the lexical representation of the given 8 bit integer value and converts it into a
     * primitive <code>byte</code> value.
     *
     * @param pLexicalXSDByte The input string being parsed.
     * @return The input string converted into a primitive <code>byte</code>.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public byte parseByte(String pLexicalXSDByte);

    /**
     * Parses the lexical representation of the given qualified name and converts it into an
     * instance of {@link javax.xml.namespace.QName}. The {@link javax.xml.namespace.QName} consists
     * of a namespace URI and a local name.
     *
     * @param pLexicalXSDQName The input string being parsed, an optional namespace prefix, followed
     *     by the local name, if any. If a prefix is present, they are separated by a colon.
     * @param pNamespaceContext The namespace context is used to query mappings between prefixes and
     *     namespace URI's.
     * @return The input string converted into an instance of {@link javax.xml.namespace.QName}.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public QName parseQName(String pLexicalXSDQName, NamespaceContext pNamespaceContext);

    /**
     * Parses the lexical representation of the given dateTime value and converts it into an
     * instance of {@link java.util.Calendar}. Valid lexical representations of a dateTime value
     * include
     *
     * <pre>
     *   YYYY-MM-DDThh:mm:ss
     *   YYYY-MM-DDThh:mm:ss.sss
     *   YYYY-MM-DDThh:mm:ssZ
     *   YYYY-MM-DDThh:mm:ss-01:00
     * </pre>
     *
     * The former examples are all specified in UTC time. The last example uses a negatice offset of
     * one hour to UTC.
     *
     * @param pLexicalXSDDateTime The input string being parsed.
     * @return The input string converted into an instance of {@link java.util.Calendar}.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public Calendar parseDateTime(String pLexicalXSDDateTime);

    /**
     * Parses the lexical representation of the given byte array, which is encoded in base 64.
     *
     * @param pLexicalXSDBase64Binary The input string being parsed, a base 64 encoded array of
     *     bytes.
     * @return The decoded byte array.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public byte[] parseBase64Binary(String pLexicalXSDBase64Binary);

    /**
     * Parses the lexical representation of the given byte array, which is encoded in hex digits.
     *
     * @param pLexicalXSDHexBinary The input string being parsed, an array of bytes encoded in hex
     *     digits.
     * @return The decoded byte array.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public byte[] parseHexBinary(String pLexicalXSDHexBinary);

    /**
     * Parses the lexical representation of the given 32 bit unsignet integer value and converts it
     * into a primitive <code>long</code> value.
     *
     * @param pLexicalXSDUnsignedInt The input string being parsed.
     * @return The input string converted into a primitive <code>long</code>.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public long parseUnsignedInt(String pLexicalXSDUnsignedInt);

    /**
     * Parses the lexical representation of the given 16 bit unsignet integer value and converts it
     * into a primitive <code>int</code> value.
     *
     * @param pLexicalXSDUnsignedShort The input string being parsed.
     * @return The input string conve rted into a primitive <code>int</code>.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public int parseUnsignedShort(String pLexicalXSDUnsignedShort);

    /**
     * Parses the lexical representation of the given time value and converts it into an instance of
     * {@link java.util.Calendar}. Valid lexical representations of a time value include
     *
     * <pre>
     *   hh:mm:ss
     *   hh:mm:ss.sss
     *   hh:mm:ssZ
     *   hh:mm:ss-01:00
     * </pre>
     *
     * The former examples are all specified in UTC time. The last example uses a negatice offset of
     * one hour to UTC.
     *
     * @param pLexicalXSDTime The input string being parsed.
     * @return The input string converted into an instance of {@link java.util.Calendar}.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public Calendar parseTime(String pLexicalXSDTime);

    /**
     * Parses the lexical representation of the given date value and converts it into an instance of
     * {@link java.util.Calendar}. Valid lexical representations of a date value include
     *
     * <pre>
     *   YYYY-MM-DD
     *   YYYY-MM-DDZ
     *   YYYY-MM-DD-01:00
     * </pre>
     *
     * The former examples are all specified in UTC time. The last example uses a negatice offset of
     * one hour to UTC.
     *
     * @param pLexicalXSDDate The input string being parsed.
     * @return The input string converted into an instance of {@link java.util.Calendar}.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public Calendar parseDate(String pLexicalXSDDate);

    /**
     * Returns the lexical representation of the input string, which is the unmodified input string.
     *
     * @param pLexicalXSDAnySimpleType An input string in lexical representation.
     * @return The unmodified input string.
     * @see javax.xml.bind.ParseConversionEvent
     */
    public String parseAnySimpleType(String pLexicalXSDAnySimpleType);

    /**
     * Returns a lexical representation of the given input string, which is the unmodified input
     * string.
     *
     * @param pValue The input string.
     * @return The unmodified input string.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printString(String pValue);

    /**
     * Returns a lexical representation of the given instance of {@link BigInteger}, which is an
     * integer in arbitrary precision.
     *
     * @param pValue The integer value being converted.
     * @return A lexical representation of the input value.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printInteger(BigInteger pValue);

    /**
     * Returns a lexical representation of the given primitive 32 bit integer.
     *
     * @param pValue The <code>int</code> value being converted.
     * @return A lexical representation of the input value.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printInt(int pValue);

    /**
     * Returns a lexical representation of the given primitive 64 bit integer.
     *
     * @param pValue The <code>long</code> value being converted.
     * @return A lexical representation of the input value.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printLong(long pValue);

    /**
     * Returns a lexical representation of the given primitive 16 bit integer.
     *
     * @param pValue The <code>short</code> value being converted.
     * @return A lexical representation of the input value.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printShort(short pValue);

    /**
     * Returns a lexical representation of the given instance of {@link BigDecimal}, which is a
     * decimal number in arbitrary precision.
     *
     * @param pValue The decimal value being converted.
     * @return A lexical representation of the input value.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printDecimal(BigDecimal pValue);

    /**
     * Returns a lexical representation of the given primitive 32 bit floating point number.
     *
     * @param pValue The <code>float</code> value being converted.
     * @return A lexical representation of the input value.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printFloat(float pValue);

    /**
     * Returns a lexical representation of the given primitive 64 bit floating point number.
     *
     * @param pValue The <code>double</code> value being converted.
     * @return A lexical representation of the input value.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printDouble(double pValue);

    /**
     * Returns a lexical representation of the given primitive boolean value.
     *
     * @param pValue The <code>boolean</code> value being converted.
     * @return A lexical representation of the input value.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printBoolean(boolean pValue);

    /**
     * Returns a lexical representation of the given primitive 8 bit integer.
     *
     * @param pValue The <code>byte</code> value being converted.
     * @return A lexical representation of the input value.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printByte(byte pValue);

    /**
     * Returns a lexical representation of the given qualified name, which is a combination of
     * namespace URI and local name. The lexical representation is an optional prefix, which is
     * currently mapped to namespace URI of the qualified name, followed by a colon and the local
     * name. If the namespace URI is the current default namespace URI, then the prefix and the
     * colon may be omitted.
     *
     * @param pValue The qualified name being converted.
     * @param pNamespaceContext A mapping of prefixes to namespace URI's which may be used to
     *     determine a valid prefix.
     * @return A lexical representation of the qualified name.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printQName(QName pValue, NamespaceContext pNamespaceContext);

    /**
     * Returns a lexical representation of the given dateTime value. Valid lexical representations
     * include:
     *
     * <pre>
     *   YYYY-MM-DDThh:mm:ss
     *   YYYY-MM-DDThh:mm:ss.sss
     *   YYYY-MM-DDThh:mm:ssZ
     *   YYYY-MM-DDThh:mm:ss-01:00
     * </pre>
     *
     * The former examples are all specified in UTC time. The last example uses a negatice offset of
     * one hour to UTC.
     *
     * @param pValue The dateTime value being converted
     * @return A lexical representation of the input value.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printDateTime(Calendar pValue);

    /**
     * Returns a lexical representation of the given byte array. The lexical representation is
     * obtained by application of the base 64 encoding.
     *
     * @param pValue The byte array being converted.
     * @return The converted byte array.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printBase64Binary(byte[] pValue);

    /**
     * Returns a lexical representation of the given byte array. The lexical representation is
     * obtained by encoding any byte as two hex digits.
     *
     * @param pValue The byte array being converted.
     * @return The converted byte array.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printHexBinary(byte[] pValue);

    /**
     * Returns a lexical representation of the given primitive, unsigned 32 bit integer.
     *
     * @param pValue The <code>long</code> value being converted.
     * @return A lexical representation of the input value.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public java.lang.String printUnsignedInt(long pValue);

    /**
     * Returns a lexical representation of the given primitive, unsigned 16 bit integer.
     *
     * @param pValue The <code>short</code> value being converted.
     * @return A lexical representation of the input value.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public java.lang.String printUnsignedShort(int pValue);

    /**
     * Returns a lexical representation of the given time value. Valid lexical representations
     * include:
     *
     * <pre>
     *   hh:mm:ss
     *   hh:mm:ss.sss
     *   hh:mm:ssZ
     *   hh:mm:ss-01:00
     * </pre>
     *
     * The former examples are all specified in UTC time. The last example uses a negatice offset of
     * one hour to UTC.
     *
     * @param pValue The time value being converted
     * @return A lexical representation of the input value.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printTime(Calendar pValue);

    /**
     * Returns a lexical representation of the given date value. Valid lexical representations
     * include:
     *
     * <pre>
     *   YYYY-MM-DD
     *   YYYY-MM-DDZ
     *   YYYY-MM-DD-01:00
     * </pre>
     *
     * The former examples are all specified in UTC time. The last example uses a negatice offset of
     * one hour to UTC.
     *
     * @param pValue The date value being converted
     * @return A lexical representation of the input value.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printDate(Calendar pValue);

    /**
     * Returns a lexical representation of the given input string, which is the unmodified input
     * string.
     *
     * @param pValue The input string.
     * @return The unmodified input string.
     * @see javax.xml.bind.PrintConversionEvent
     */
    public String printAnySimpleType(String pValue);
}

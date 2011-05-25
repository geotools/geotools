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


/** <p>The <code>DatatypeConverterInterface</code> is a helper class for
 * implementation of custom <code>print()</code> and <code>parse()</code>
 * methods, as specified by <samp>jaxb:javaType/@printMethod</samp> and
 * <samp>jaxb:javaType/@parseMethod</samp>. However, the JAXB user won't
 * be accessing this class directly. The JAXB provider is required to
 * create an instance of this class and pass it to the JAXB runtime by
 * invoking
 * {@link javax.xml.bind.DatatypeConverter#setDatatypeConverter(DatatypeConverterInterface)}.
 * The JAXB user may access this instance via the static methods of
 * {@link javax.xml.bind.DatatypeConverter}.</p>
 * <p>The interface provides methods <code>parseFoo()</code> and
 * <code>printFoo()</code> for any XML Schema type <samp>foo</samp>.
 * The <code>parseFoo()</code> method must accept any lexically valid
 * value and convert it into the corresponding canonical value. An error
 * in the conversion routine must be mapped to a
 * {@link javax.xml.bind.ParseConversionEvent}.</p>
 * <p>The <code>printFoo(foo pValue)</code> method must convert the value
 * <samp>pValue</samp> into an arbitrary lexical representation. It is
 * recommended to use the default representation.</p>
 *  
 * @author JSR-31
 * @since JAXB 1.0
 * @see javax.xml.bind.DatatypeConverter
 * @see javax.xml.bind.ParseConversionEvent
 * @see javax.xml.bind.PrintConversionEvent
 *
 *
 * @source $URL$
 */
public interface DatatypeConverterInterface {
  /** <p>Parses the lexical representation and converts it into a String.</p>
   *
   * @param pLexicalXSDString The input string being parsed.
   * @return The unmodified input string.
   * @see javax.xml.bind.ParseConversionEvent
   */
  public String parseString(String pLexicalXSDString);

  /** <p>Parses the lexical representation of the given integer value
   * (arbitrary precision) and converts it into an instance of
   * {@link java.math.BigInteger}.</p>
   * @param pLexicalXSDInteger The input string being parsed.
   * @return The input string converted into an instance of {@link BigInteger}.
   * @see javax.xml.bind.ParseConversionEvent
   */
  public BigInteger parseInteger(String pLexicalXSDInteger);

  /** <p>Parses the lexical representation of the given 32 bit integer value
   * and converts it into a primitive <code>int</code> value.</p>
   * @param pLexicalXSDInt The input string being parsed.
   * @return The input string converted into a primitive <code>int</code>.
   * @see javax.xml.bind.ParseConversionEvent
   */ 
  public int parseInt(String pLexicalXSDInt);

  /** <p>Parses the lexical representation of the given 64 bit integer value
   * and converts it into a primitive <code>long</code> value.</p>
   * @param pLexicalXSDLong The input string being parsed.
   * @return The input string converted into a primitive <code>long</code>.
   * @see javax.xml.bind.ParseConversionEvent
   */ 
  public long parseLong(String pLexicalXSDLong);

  /** <p>Parses the lexical representation of the given 16 bit integer value
   * and converts it into a primitive <code>short</code> value.</p>
   * @param pLexicalXSDShort The input string being parsed.
   * @return The input string converted into a primitive <code>short</code>.
   * @see javax.xml.bind.ParseConversionEvent
   */ 
  public short parseShort(String pLexicalXSDShort);

  /** <p>Parses the lexical representation of the given decimal value
   * (arbitrary precision) and converts it into an instance of
   * {@link java.math.BigDecimal}.</p>
   * @param pLexicalXSDDecimal The input string being parsed.
   * @return The input string converted into an instance of {@link java.math.BigDecimal}.
   * @see javax.xml.bind.ParseConversionEvent
   */ 
  public BigDecimal parseDecimal(String pLexicalXSDDecimal);

  /** <p>Parses the lexical representation of the given 32 bit floating
   * point value and converts it into a primitive <code>float</code> value.</p>
   * @param pLexicalXSDFloat The input string being parsed.
   * @return The input string converted into a primitive <code>float</code>.
   * @see javax.xml.bind.ParseConversionEvent
   */ 
  public float parseFloat(String pLexicalXSDFloat);

  /** <p>Parses the lexical representation of the given 64 bit floating
   * point value and converts it into a primitive <code>double</code> value.</p>
   * @param pLexicalXSDDouble The input string being parsed.
   * @return The input string converted into a primitive <code>double</code>.
   * @see javax.xml.bind.ParseConversionEvent
   */ 
  public double parseDouble(String pLexicalXSDDouble);

  /** <p>Parses the lexical representation of the given boolean value
   * and converts it into a primitive <code>boolean</code> value.</p>
   * @param pLexicalXSDBoolean The input string being parsed.
   * @return The input string converted into a primitive <code>boolean</code>.
   * @see javax.xml.bind.ParseConversionEvent
   */ 
  public boolean parseBoolean(String pLexicalXSDBoolean);

  /** <p>Parses the lexical representation of the given 8 bit integer value
   * and converts it into a primitive <code>byte</code> value.</p>
   * @param pLexicalXSDByte The input string being parsed.
   * @return The input string converted into a primitive <code>byte</code>.
   * @see javax.xml.bind.ParseConversionEvent
   */ 
  public byte parseByte(String pLexicalXSDByte);

  /** <p>Parses the lexical representation of the given qualified name
   * and converts it into an instance of {@link javax.xml.namespace.QName}.
   * The {@link javax.xml.namespace.QName} consists of a namespace URI
   * and a local name.</p>
   * @param pLexicalXSDQName The input string being parsed, an optional
   *   namespace prefix, followed by the local name, if any. If a prefix
   *   is present, they are separated by a colon.
   * @param pNamespaceContext The namespace context is used to query
   *   mappings between prefixes and namespace URI's.
   * @return The input string converted into an instance of
   *   {@link javax.xml.namespace.QName}.
   * @see javax.xml.bind.ParseConversionEvent
   */ 
  public QName parseQName(String pLexicalXSDQName,
                          NamespaceContext pNamespaceContext);

  /** <p>Parses the lexical representation of the given dateTime value
   * and converts it into an instance of {@link java.util.Calendar}.
   * Valid lexical representations of a dateTime value include
   * <pre>
   *   YYYY-MM-DDThh:mm:ss
   *   YYYY-MM-DDThh:mm:ss.sss
   *   YYYY-MM-DDThh:mm:ssZ
   *   YYYY-MM-DDThh:mm:ss-01:00
   * </pre>
   * The former examples are all specified in UTC time. The last example
   * uses a negatice offset of one hour to UTC.</p>
   * @param pLexicalXSDDateTime The input string being parsed.
   * @return The input string converted into an instance of
   *   {@link java.util.Calendar}.
   * @see javax.xml.bind.ParseConversionEvent
   */
  public Calendar parseDateTime(String pLexicalXSDDateTime);

  /** <p>Parses the lexical representation of the given byte array, which
   * is encoded in base 64.</p>
   * @param pLexicalXSDBase64Binary The input string being parsed, a
   *   base 64 encoded array of bytes.
   * @return The decoded byte array.
   * @see javax.xml.bind.ParseConversionEvent
   */
  public byte[] parseBase64Binary(String pLexicalXSDBase64Binary);

  /** <p>Parses the lexical representation of the given byte array, which
   * is encoded in hex digits.</p>
   * @param pLexicalXSDHexBinary The input string being parsed, an
   *    array of bytes encoded in hex digits.
   * @return The decoded byte array.
   * @see javax.xml.bind.ParseConversionEvent
   */
  public byte[] parseHexBinary(String pLexicalXSDHexBinary);

  /** <p>Parses the lexical representation of the given 32 bit
   * unsignet integer value and converts it into a primitive <code>long</code>
   * value.</p>
   * @param pLexicalXSDUnsignedInt The input string being parsed.
   * @return The input string converted into a primitive <code>long</code>.
   * @see javax.xml.bind.ParseConversionEvent
   */ 
  public long parseUnsignedInt(String pLexicalXSDUnsignedInt);

  /** <p>Parses the lexical representation of the given 16 bit
   * unsignet integer value and converts it into a primitive <code>int</code>
   * value.</p>
   * @param pLexicalXSDUnsignedShort The input string being parsed.
   * @return The input string conve
   * rted into a primitive <code>int</code>.
   * @see javax.xml.bind.ParseConversionEvent
   */ 
  public int parseUnsignedShort(String pLexicalXSDUnsignedShort);

  /** <p>Parses the lexical representation of the given time value
   * and converts it into an instance of {@link java.util.Calendar}.
   * Valid lexical representations of a time value include
   * <pre>
   *   hh:mm:ss
   *   hh:mm:ss.sss
   *   hh:mm:ssZ
   *   hh:mm:ss-01:00
   * </pre>
   * The former examples are all specified in UTC time. The last example
   * uses a negatice offset of one hour to UTC.</p>
   * @param pLexicalXSDTime The input string being parsed.
   * @return The input string converted into an instance of
   *   {@link java.util.Calendar}.
   * @see javax.xml.bind.ParseConversionEvent
   */
  public Calendar parseTime(String pLexicalXSDTime);

  /** <p>Parses the lexical representation of the given date value
   * and converts it into an instance of {@link java.util.Calendar}.
   * Valid lexical representations of a date value include
   * <pre>
   *   YYYY-MM-DD
   *   YYYY-MM-DDZ
   *   YYYY-MM-DD-01:00
   * </pre>
   * The former examples are all specified in UTC time. The last example
   * uses a negatice offset of one hour to UTC.</p>
   * @param pLexicalXSDDate The input string being parsed.
   * @return The input string converted into an instance of
   *   {@link java.util.Calendar}.
   * @see javax.xml.bind.ParseConversionEvent
   */
  public Calendar parseDate(String pLexicalXSDDate);

  /** <p>Returns the lexical representation of the input string, which is
   * the unmodified input string.</p>
   * @param pLexicalXSDAnySimpleType An input string in lexical representation.
   * @return The unmodified input string.
   * @see javax.xml.bind.ParseConversionEvent
   */
  public String parseAnySimpleType(String pLexicalXSDAnySimpleType);

  /** <p>Returns a lexical representation of the given input string, which
   * is the unmodified input string.</p>
   * @param pValue The input string.
   * @return The unmodified input string.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printString(String pValue);

  /** <p>Returns a lexical representation of the given instance of
   * {@link BigInteger}, which is an integer in arbitrary precision.</p>
   * @param pValue The integer value being converted.
   * @return A lexical representation of the input value.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printInteger(BigInteger pValue);

  /** <p>Returns a lexical representation of the given primitive
   * 32 bit integer.</p>
   * @param pValue The <code>int</code> value being converted.
   * @return A lexical representation of the input value.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printInt(int pValue);

  /** <p>Returns a lexical representation of the given primitive
   * 64 bit integer.</p>
   * @param pValue The <code>long</code> value being converted.
   * @return A lexical representation of the input value.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printLong(long pValue);

  /** <p>Returns a lexical representation of the given primitive
   * 16 bit integer.</p>
   * @param pValue The <code>short</code> value being converted.
   * @return A lexical representation of the input value.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printShort(short pValue);

  /** <p>Returns a lexical representation of the given instance of
   * {@link BigDecimal}, which is a decimal number in arbitrary
   * precision.</p>
   * @param pValue The decimal value being converted.
   * @return A lexical representation of the input value.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printDecimal(BigDecimal pValue);

  /** <p>Returns a lexical representation of the given primitive
   * 32 bit floating point number.</p>
   * @param pValue The <code>float</code> value being converted.
   * @return A lexical representation of the input value.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printFloat(float pValue);

  /** <p>Returns a lexical representation of the given primitive
   * 64 bit floating point number.</p>
   * @param pValue The <code>double</code> value being converted.
   * @return A lexical representation of the input value.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printDouble(double pValue);

  /** <p>Returns a lexical representation of the given primitive
   * boolean value.</p>
   * @param pValue The <code>boolean</code> value being converted.
   * @return A lexical representation of the input value.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printBoolean(boolean pValue);

  /** <p>Returns a lexical representation of the given primitive
   * 8 bit integer.</p>
   * @param pValue The <code>byte</code> value being converted.
   * @return A lexical representation of the input value.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printByte(byte pValue);

  /** <p>Returns a lexical representation of the given qualified
   * name, which is a combination of namespace URI and local name.
   * The lexical representation is an optional prefix, which is
   * currently mapped to namespace URI of the qualified name,
   * followed by a colon and the local name. If the namespace URI
   * is the current default namespace URI, then the prefix and
   * the colon may be omitted.</p>
   * @param pValue The qualified name being converted.
   * @param pNamespaceContext A mapping of prefixes to namespace
   *   URI's which may be used to determine a valid prefix.
   * @return A lexical representation of the qualified name.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printQName(QName pValue,
                            NamespaceContext pNamespaceContext);

  /** <p>Returns a lexical representation of the given dateTime
   * value. Valid lexical representations include:
   * <pre>
   *   YYYY-MM-DDThh:mm:ss
   *   YYYY-MM-DDThh:mm:ss.sss
   *   YYYY-MM-DDThh:mm:ssZ
   *   YYYY-MM-DDThh:mm:ss-01:00
   * </pre>
   * The former examples are all specified in UTC time. The last example
   * uses a negatice offset of one hour to UTC.</p>
   * @param pValue The dateTime value being converted
   * @return A lexical representation of the input value.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printDateTime(Calendar pValue);

  /** <p>Returns a lexical representation of the given byte array.
   * The lexical representation is obtained by application of the
   * base 64 encoding.</p>
   * @param pValue The byte array being converted.
   * @return The converted byte array.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printBase64Binary(byte[] pValue);

  /** <p>Returns a lexical representation of the given byte array.
   * The lexical representation is obtained by encoding any byte
   * as two hex digits.</p>
   * @param pValue The byte array being converted.
   * @return The converted byte array.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printHexBinary(byte[] pValue);

  /** <p>Returns a lexical representation of the given primitive,
   * unsigned 32 bit integer.</p>
   * @param pValue The <code>long</code> value being converted.
   * @return A lexical representation of the input value.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public java.lang.String printUnsignedInt(long pValue);

  /** <p>Returns a lexical representation of the given primitive,
   * unsigned 16 bit integer.</p>
   * @param pValue The <code>short</code> value being converted.
   * @return A lexical representation of the input value.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public java.lang.String printUnsignedShort(int pValue);

  /** <p>Returns a lexical representation of the given time
   * value. Valid lexical representations include:
   * <pre>
   *   hh:mm:ss
   *   hh:mm:ss.sss
   *   hh:mm:ssZ
   *   hh:mm:ss-01:00
   * </pre>
   * The former examples are all specified in UTC time. The last example
   * uses a negatice offset of one hour to UTC.</p>
   * @param pValue The time value being converted
   * @return A lexical representation of the input value.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printTime(Calendar pValue);

  /** <p>Returns a lexical representation of the given date
   * value. Valid lexical representations include:
   * <pre>
   *   YYYY-MM-DD
   *   YYYY-MM-DDZ
   *   YYYY-MM-DD-01:00
   * </pre>
   * The former examples are all specified in UTC time. The last example
   * uses a negatice offset of one hour to UTC.</p>
   * @param pValue The date value being converted
   * @return A lexical representation of the input value.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printDate(Calendar pValue);

  /** <p>Returns a lexical representation of the given input
   * string, which is the unmodified input string.</p>
   * @param pValue The input string.
   * @return The unmodified input string.
   * @see javax.xml.bind.PrintConversionEvent
   */
  public String printAnySimpleType(String pValue);
}
